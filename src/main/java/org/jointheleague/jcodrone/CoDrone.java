package org.jointheleague.jcodrone;


import com.fazecast.jSerialComm.SerialPort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jointheleague.jcodrone.protocol.DataType;
import org.jointheleague.jcodrone.protocol.Header;
import org.jointheleague.jcodrone.protocol.Serializable;
import org.jointheleague.jcodrone.protocol.common.Command;
import org.jointheleague.jcodrone.protocol.information.*;
import org.jointheleague.jcodrone.protocol.light.LightEvent;
import org.jointheleague.jcodrone.protocol.light.LightEventColors;
import org.jointheleague.jcodrone.protocol.light.LightMode;
import org.jointheleague.jcodrone.protocol.light.LightModeColors;
import org.jointheleague.jcodrone.receiver.CRC16;
import org.jointheleague.jcodrone.receiver.Receiver;
import org.jointheleague.jcodrone.system.ModeVehicle;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A CoDrone object provides the state of the drone and the methods required to control the drone.
 */
public class CoDrone implements AutoCloseable {
    private static Logger log = LogManager.getLogger(CoDrone.class);
    private final Link link;
    private final Sensors sensors;
    private final Internals internals;
    private SerialPort comPort;
    private Receiver receiver;
    private InputStream inputStream;
    private boolean stopped = true;
    private Thread readThread;

    /**
     * The default constructor establishes creates an unassigned drone object that can be connected to any
     * drone.
     */
    public CoDrone() {
        link = new Link(this);
        sensors = new Sensors(this);
        internals = new Internals(this);
        receiver = new Receiver(this, link, sensors, internals);
        log.info("CoDrone Setup");
    }

    private void open() {
        log.info("Connect without explicit port.");
        SerialPort[] ports = SerialPort.getCommPorts();

        if (log.isDebugEnabled()) {
            log.debug("List of available ports: \n\t{}",
                    Stream.of(ports)
                            .map(p ->
                                    String.format("%s: %s",
                                            p.getSystemPortName(),
                                            p.getDescriptivePortName()
                                    )
                            ).collect(Collectors.joining("\n\t")));
        }

        if (ports.length > 0) {
            open(ports[ports.length - 1].getSystemPortName());
        }
    }

    private void open(String portName) {
        log.info("Connect to port: {}", portName);
        comPort = SerialPort.getCommPort(portName);
        comPort.setBaudRate(115200);
        comPort.setParity(SerialPort.NO_PARITY);
        comPort.setNumStopBits(SerialPort.ONE_STOP_BIT);
        comPort.setNumDataBits(8);
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
        inputStream = comPort.getInputStream();

        readThread = new Thread(() -> {
            int errorCount = 0;
            stopped = false;
            while (!stopped) {
                try {
                    receiver.call((byte) inputStream.read());
                    errorCount = 0;
                } catch (IOException e) {
                    errorCount++;
                    if (!stopped) {
                        log.error("Error reading from serial port.", e);
                        if (errorCount > 5) {
                            log.error("Repeated communication errors, closing port.");
                        }
                    }
                }
            }
        });
        readThread.start();
    }

    /**
     * Closes the serial connection to the controller.
     * <p>
     * This method will attempt to gracefully close the thread reading from the serial port before interrupting it to
     * release the port.
     */
    @Override
    public void close() {
        log.info("Closing serial port.");
        stopped = true;
        try {
            readThread.join(200);
        } catch (InterruptedException e) {
            log.info("Join to serial port reading thread was interrupted.");
        }
        if (readThread.isAlive()) {
            log.warn("Interrupting serial communication");
            readThread.interrupt();
        }
        comPort.closePort();
        log.info("Port {} closed", comPort.getSystemPortName());
    }

    /**
     * The default connection method connects to the nearest drone connected to the last available serial port
     * enumerated by the operating system.
     *
     * @throws CoDroneNotFoundException Thrown if no drone is found. Check logs to determine what serial port was used.
     */
    public void connect() throws CoDroneNotFoundException {
        connect(null, null, false);
    }

    /**
     * This connect method allows the connection to a specific drone over bluetooth via the controller connected
     * to the specified serial por. Additionally the controller can be reset before the connection is attempted.
     *
     * @param portName    System name of the serial port connected to the controller.
     * @param deviceName  The name of the drone to connect to.
     * @param resetSystem Resets the controller before attempting to connect to the drone.
     * @throws CoDroneNotFoundException Thrown if the specified port or drone can not be found.
     */
    @SuppressWarnings("WeakerAccess")
    public void connect(String portName, String deviceName, boolean resetSystem) throws CoDroneNotFoundException {
        if (deviceName != null && !deviceName.isEmpty() && deviceName.length() != 12) {
            throw new IllegalArgumentException(
                    String.format("Invalid device name length %s.", deviceName.length()));
        }

        if (comPort == null || !comPort.isOpen()) {
            if (portName == null || portName.isEmpty()) {
                open();
            } else {
                open(portName);
                //Thread.sleep(100);
            }
        } else if (!portName.equalsIgnoreCase(comPort.getSystemPortName())) {
            close();
            open(portName);
            //Thread.sleep(100);
        }

        // system reset
        if (resetSystem) {
            link.resetSystem();
        }

        link.connect(deviceName);
    }

    void transfer(Header header, Serializable data) {
        if ((header == null) || (data == null)) {
            log.error("Header or data was null.");
            throw new InvalidMessageException("Header or data is null.");
        }

        int crc16 = 0;
        crc16 = CRC16.calc(header.toArray(), crc16);
        crc16 = CRC16.calc(data.toArray(), crc16);

        byte[] headerArray = header.toArray();
        byte[] dataArray = data.toArray();

        ByteBuffer message = ByteBuffer.allocate(2 + headerArray.length + dataArray.length + 2);
        message.order(ByteOrder.LITTLE_ENDIAN);
        message.put((byte) 0x0A);
        message.put((byte) 0x55);
        message.put(headerArray);
        message.put(dataArray);
        message.putShort((short) crc16);

        comPort.writeBytes(message.array(), message.capacity());
        log.info("Sent: {}", DatatypeConverter.printHexBinary(message.array()));
    }


    /**
     * Sends a command to the drone.
     * <p>
     * Note: Most of the commands available to be sent are internally used. Care should be taken when directly sending
     * commands to determine what side effects may occur. Please refer to any separate drone documentation for the use
     * of commands.
     *
     * @param type The command from the CommandType enumeration.
     */
    public void sendCommand(CommandType type) {
        sendCommand(type, (byte) 0);
    }

    /**
     * Sends a command with a parameter to the drone.
     * <p>
     * Note: Most of the commands available to be sent are internally used. Care should be taken when directly sending
     * commands to determine what side effects may occur. Please refer to any separate drone documentation for the use
     * of commands.
     *
     * @param type The command from the CommandType enumeration.
     */
    public void sendCommand(CommandType type, byte option) {
        Command command = new Command(type, option);

        sendMessage(command);
    }

    @SuppressWarnings("unused")
    void sendMessage(Serializable message) {
        Header header = new Header(DataType.fromClass(message.getClass()), message.getInstanceSize());

        transfer(header, message);
    }

    /**
     * Sends a command to set the drone's mode to flight mode with guard installed.
     */
    @SuppressWarnings("unused")
    public void setFlightMode() {
        sendCommand(CommandType.MODE_VEHICLE, ModeVehicle.FLIGHT_GUARD.value());
    }

    /**
     * Sends a command to set the drone's mode to flight mode with no guard installed.
     */
    @SuppressWarnings("unused")
    public void setFlightModeNoGuard() {
        sendCommand(CommandType.MODE_VEHICLE, ModeVehicle.FLIGHT_NO_GUARD.value());
    }

    /**
     * Sends a command to set the drone's mode to flight mode with camera installed.
     */
    @SuppressWarnings("unused")
    public void setFlightModeFPV() {
        sendCommand(CommandType.MODE_VEHICLE, ModeVehicle.FLIGHT_FPV.value());
    }

    /**
     * Sends a command to set the drone's mode to drive mode.
     */
    @SuppressWarnings("unused")
    public void setDriveMode() {
        sendCommand(CommandType.MODE_VEHICLE, ModeVehicle.DRIVE.value());
    }

    /**
     * Sends a command to make the drone take off.
     */
    @SuppressWarnings("unused")
    public void takeoff() {
        Flight.takeoff(this);
    }

    /**
     * Sends a command to make the drone land.
     */
    @SuppressWarnings("unused")
    public void land() {
        Flight.land(this);
    }

    /**
     * Sends a command to make the drone do a front flip in flight.
     */
    @SuppressWarnings("unused")
    public void flipFront() {
        Flight.flipFront(this);
    }

    /**
     * Sends a command to make the drone do a back flip in flight.
     */
    @SuppressWarnings("unused")
    public void flipRear() {
        Flight.flipRear(this);
    }

    /**
     * Sends a command to make the drone do a left flip in flight.
     */
    @SuppressWarnings("unused")
    public void flipLeft() {
        Flight.flipLeft(this);
    }

    /**
     * Sends a command to make the drone do a right flip in flight.
     */
    @SuppressWarnings("unused")
    public void flipRight() {
        Flight.flipRight(this);
    }

    /**
     * Sends a command to make the drone stop the motors. This command should be used to implement a kill switch.
     */
    @SuppressWarnings("unused")
    public void stop() {
        Flight.stop(this);
    }

    /**
     * Sends a command to make the drone turn over. I have seen notes that imply this method is used when the drone
     * is not flying and is sitting upside down after a crash to right it. Testing is required.
     */
    @SuppressWarnings("unused")
    public void turnOver() {
        Flight.turnOver(this);
    }

    /**
     * This command is including in the protocol and possibly sends a preconfigured IR message.
     */
    @SuppressWarnings("unused")
    public void shoot() {
        Flight.shoot(this);
    }

    /**
     * It is unknown at this time the effect of sending this command.
     */
    @SuppressWarnings("unused")
    public void underAttack() {
        Flight.underAttack(this);
    }

    public void flyDirect(DirectControl control) {
        Flight.flyDirect(this, control);
    }
    /**
     * Sets a single light mode.
     *
     * @param mode A light mode created using the light mode builder.
     */
    @SuppressWarnings("unused")
    public void lightMode(LightMode mode) {
        LED.setMode(this, mode, false);
    }

    /**
     * Sets a single default light mode.
     *
     * @param mode A light mode created using the light mode builder.
     */
    @SuppressWarnings("unused")
    public void defaultLightMode(LightMode mode) {
        LED.setMode(this, mode, true);
    }

    /**
     * Sets two light modes.
     *
     * @param mode1 First light mode created using the light mode builder.
     * @param mode2 Second light mode created using the light mode builder.
     */
    @SuppressWarnings("unused")
    public void lightModes(LightMode mode1, LightMode mode2) {
        LED.setMode2(this, mode1, mode2, false);
    }

    /**
     * Sets two default light modes.
     *
     * @param mode1 First light mode created using the light mode builder.
     * @param mode2 Second light mode created using the light mode builder.
     */
    @SuppressWarnings("unused")
    public void defaultLightModes(LightMode mode1, LightMode mode2) {
        LED.setMode2(this, mode1, mode2, true);
    }

    /**
     * Sets a single light mode and sends a command with an option.
     *
     * @param mode          Light mode created using the light mode builder.
     * @param command       Command from the command type enumeration.
     * @param commandOption Parameter specific to the command sent.
     */
    @SuppressWarnings("unused")
    public void lightModeWithCommand(LightMode mode, CommandType command, byte commandOption) {
        if (mode instanceof LightModeColors) {
            LED.setModeCommand(this, (LightModeColors) mode, command, commandOption);
        } else {
            throw new IllegalArgumentException("Commands can only be sent with colors specified by name.");
        }
    }

    /**
     * Sets a single light mode, sends a command with an option and includes IR data.
     * @param mode Light mode created using the light mode builder.
     * @param command Command from the command type enumeration.
     * @param commandOption Parameter specific to the command sent.
     * @param irData Data to be sent for ir message.
     */
    @SuppressWarnings("unused")
    public void lightModeWithCommandandIR(LightMode mode, CommandType command, byte commandOption, short irData) {
        if (mode instanceof LightModeColors) {
            LED.setLightModeCommandIR(this, (LightModeColors) mode, command, commandOption, irData);
        } else {
            throw new IllegalArgumentException("Commands can only be sent with colors specified by name.");
        }
    }

    public void lightEvent(LightEvent event) {
        LED.setLightEvent(this, event);
    }

    public void lightEventWithCommand(LightEvent event, CommandType command, byte commandOption) {
        if (event instanceof LightEventColors) {
            LED.setLightEventCommand(this, (LightEventColors) event, command, commandOption);
        } else {
            throw new IllegalArgumentException("Commands can only be sent with colors specified by name.");
        }
    }

    public void lightEventWithCommandIR(LightEvent event, CommandType command, byte commandOption, short irData) {
        if (event instanceof LightEventColors) {
            LED.setLightEventCommandIR(this, (LightEventColors) event, command, commandOption, irData);
        } else {
            throw new IllegalArgumentException("Commands can only be sent with colors specified by name.");
        }
    }

    /**
     * Drone Information
     */
    public Address getAddress() {
        return link.getAddress();
    }

    public Attitude getAttitude() {
        return sensors.getAttitude();
    }

    public Battery getBattery() {
        return internals.getBattery();
    }

    public Button getButton() {
        return internals.getButton();
    }

    public CountDrive getCountDrive() {
        return internals.getCountDrive();
    }

    public CountFlight getCountFlight() {
        return internals.getCountFlight();
    }

    public GyroBias getGyroBias() {
        return sensors.getGyroBias();
    }

    public ImageFlow getImageFlow() {
        return sensors.getImageFlow();
    }

    public IMU getImu() {
        return sensors.getImu();
    }

    public IRMessage getIrMessage() {
        return internals.getIrMessage();
    }

    public Motor getMotor() {
        return internals.getMotor();
    }

    public Pressure getPressure() {
        return sensors.getPressure();
    }

    public Range getRange() {
        return sensors.getRange();
    }

    public State getState() {
        return internals.getState();
    }

    public Temperature getTemperature() {
        return sensors.getTemperature();
    }

    public TrimAll getTrimAll() {
        return internals.getTrimAll();
    }

    public TrimDrive getTrimDrive() {
        return internals.getTrimDrive();
    }

    public TrimFlight getTrimFlight() {
        return internals.getTrimFlight();
    }

    public boolean isFlightMode() {
        return (internals.getState() != null && internals.getState().isFlightMode());
    }
}
