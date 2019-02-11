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
import org.jointheleague.jcodrone.receiver.Receiver;
import org.jointheleague.jcodrone.sender.Sender;
import org.jointheleague.jcodrone.system.ModeVehicle;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A CoDrone object provides the state of the drone and the methods required to control the drone.
 */
public class CoDrone implements AutoCloseable, Killable {
    private static final int SEND_TIMEOUT = 500;
    private static final int COMMAND_RETRIES = 3;
    private static final String CP_2104_USB_TO_UART_BRIDGE_CONTROLLER = "CP2104 USB to UART Bridge Controller";

    private static Logger log = LogManager.getLogger(CoDrone.class);
    private static SerialPort[] ports = SerialPort.getCommPorts();

    private final Link link;
    private final Sensors sensors;
    private final Internals internals;
    private final KillSwitch killSwitch;

    private SerialPort comPort;

    private Receiver receiver;
    private InputStream inputStream;
    private boolean stopped = true;
    private Thread readThread;
    private final LinkedBlockingQueue<Serializable> commandQueue = new LinkedBlockingQueue<>();
    private DirectControl oldControl = new DirectControl(0, 0, 0, 0);

    /**
     * The default constructor establishes creates an unassigned drone object that can be connected to any
     * drone.
     */
    public CoDrone() {
        link = new Link(this);
        sensors = new Sensors(this);
        internals = new Internals(this);
        receiver = new Receiver(this, link, sensors, internals);
        killSwitch = new KillSwitch(this);
        log.info("CoDrone Setup");
    }

    private void open() throws BluetoothBoardNotFoundException {
        log.info("Connect without explicit port.");

        if (log.isDebugEnabled()) {
            log.debug("List of available ports: \n\t{}",
                    getPortNames().collect(Collectors.joining("\n\t")));
        }

        if (ports.length > 0) {
            Optional<String> defaultPort = Stream.of(ports)
                    .filter(p -> p.getDescriptivePortName().equals(CP_2104_USB_TO_UART_BRIDGE_CONTROLLER))
                    .findFirst()
                    .map(SerialPort::getSystemPortName);

            if (defaultPort.isPresent()) {
                open(defaultPort.get());
            } else {
                throw new BluetoothBoardNotFoundException();
            }
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
        Sender sender = new Sender(comPort, commandQueue);
        sender.start();
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
        log.info("Stop drone");
        for (int i = 0; i < COMMAND_RETRIES; i++) {
            try {
                this.stop();
                Thread.sleep(500);
                //TODO Check if drone stopped
                break;
            } catch (MessageNotSentException e) {
                log.error("Could not stop drone in close", e);
            } catch (InterruptedException e) {
                log.info("Sleep interrupted waiting for close", e);
            }
        }

        log.info("Disconnect bluetooth link");
        try {
            link.disconnect();
            Thread.sleep(500);
        } catch (InterruptedException e) {
            log.info("Sleep interrupted waiting for disconnect", e);
        }

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

        killSwitch.disable();
        log.info("Kill switch disabled");
    }


    private void makeConnection(String portName, String deviceName, Address address) throws BluetoothBoardNotFoundException, MessageNotSentException, InterruptedException, CoDroneNotFoundException {
        connectPort(portName);
        if (deviceName != null) {
            link(deviceName);
        } else {
            link(address);
        }
    }

    /**
     * Connect to the nearest drone connected to the first available CP2104 port enumerated by the operating system.
     *
     * @throws CoDroneNotFoundException Thrown if no drone is found. Check logs to determine what serial port was used.
     */
    @SuppressWarnings("unused")
    public void connectVia(String portName) throws CoDroneNotFoundException, MessageNotSentException, InterruptedException, BluetoothBoardNotFoundException {
        makeConnection(portName, null, null);
    }

    /**
     * Connect to the named drone via connected to the first available CP2104 port enumerated by the operating system.
     *
     * @throws CoDroneNotFoundException Thrown if no drone is found. Check logs to determine what serial port was used.
     */
    @SuppressWarnings("unused")
    public void connectVia(String portName, String deviceName) throws BluetoothBoardNotFoundException, MessageNotSentException, InterruptedException, CoDroneNotFoundException {
        makeConnection(portName, deviceName, null);
    }

    /**
     * Connects to the addressed drone via connected to the first available CP2104 port enumerated by the operating system.
     *
     * @throws BluetoothBoardNotFoundException Thrown if no drone is found. Check logs to determine what serial port was used.
     */
    @SuppressWarnings("unused")
    public void connectVia(String portName, Address address) throws BluetoothBoardNotFoundException, MessageNotSentException, InterruptedException, CoDroneNotFoundException {
        makeConnection(portName, null, address);
    }

    /**
     * Connect to the nearest drone connected to the first available CP2104 port enumerated by the operating system.
     *
     * @throws CoDroneNotFoundException Thrown if no drone is found. Check logs to determine what serial port was used.
     */
    public void connect() throws CoDroneNotFoundException, MessageNotSentException, InterruptedException, BluetoothBoardNotFoundException {
        makeConnection(null, null, null);
    }

    /**
     * Connect to the named drone via connected to the first available CP2104 port enumerated by the operating system.
     *
     * @throws CoDroneNotFoundException Thrown if no drone is found. Check logs to determine what serial port was used.
     */
    public void connect(String deviceName) throws BluetoothBoardNotFoundException, MessageNotSentException, InterruptedException, CoDroneNotFoundException {
        makeConnection(null, deviceName, null);
    }

    /**
     * Connects to the addressed drone via connected to the first available CP2104 port enumerated by the operating system.
     *
     * @throws BluetoothBoardNotFoundException Thrown if no drone is found. Check logs to determine what serial port was used.
     */
    @SuppressWarnings("unused")
    public void connect(Address address) throws BluetoothBoardNotFoundException, MessageNotSentException, InterruptedException, CoDroneNotFoundException {
        makeConnection(null, null, address);
    }

    /**
     * This connect method allows the connection to a specific drone over bluetooth via the controller connected
     * to the specified serial port. Additionally the controller can be reset before the connection is attempted.
     *
     * @param portName System name of the serial port connected to the controller.
     * @throws BluetoothBoardNotFoundException Thrown if the specified port can not be found.
     */
    private void connectPort(String portName) throws BluetoothBoardNotFoundException {
        if (comPort == null || !comPort.isOpen()) {
            if (portName == null || portName.isEmpty()) {
                open();
            } else {
                open(portName);
            }
        } else if (!portName.equalsIgnoreCase(comPort.getSystemPortName())) {
            close();
            open(portName);
        }
    }

    private void link(String deviceName) throws MessageNotSentException, CoDroneNotFoundException, InterruptedException {
        link.connect(deviceName);
    }

    private void link(Address address) throws MessageNotSentException, CoDroneNotFoundException, InterruptedException {
        link.connect(address);
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
    @SuppressWarnings("WeakerAccess")
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
    @SuppressWarnings("WeakerAccess")
    public void sendCommand(CommandType type, byte option) {
        Command command = new Command(type, option);

        sendMessage(command);
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
    @SuppressWarnings("unused")
    public void sendCommandWait(CommandType type) throws MessageNotSentException {
        sendCommandWait(type, (byte) 0);
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
    @SuppressWarnings("WeakerAccess")
    public void sendCommandWait(CommandType type, byte option) throws MessageNotSentException {
        Command command = new Command(type, option);

        sendMessageWait(command);
    }

    void sendMessage(Serializable message) {
        commandQueue.offer(message);
    }

    void sendMessageWait(Serializable message) throws MessageNotSentException {
        Header header = new Header(DataType.fromClass(message.getClass()), message.getInstanceSize());

        boolean messageSent = false;
        int tries = 0;
        while (!messageSent && tries < 3) {
            synchronized (receiver.getAckLock(header.getDataType())) {
                tries++;
                commandQueue.offer(message);
                long sentTime = System.nanoTime();
                while (!messageSent && (sentTime + SEND_TIMEOUT) > System.nanoTime()) {
                    try {
                        receiver.getAckLock(header.getDataType()).wait();
                        messageSent = true;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (!messageSent) {
            throw new MessageNotSentException("Maximum retries exceeded.");
        }
    }

    /**
     * Returns a list of ports and their descriptive names.
     *
     * @return a Stream of strings with ports and descriptive names
     */
    @SuppressWarnings("WeakerAccess")
    public Stream<String> getPortNames() {
        return Stream.of(ports)
                .map(p ->
                        String.format("%s: %s",
                                p.getSystemPortName(),
                                p.getDescriptivePortName()
                        )
                );
    }

    /**
     * Sends a command to set the drone's mode to flight mode with guard installed.
     */
    @SuppressWarnings("unused")
    public void setFlightMode() throws MessageNotSentException {
        sendCommandWait(CommandType.MODE_VEHICLE, ModeVehicle.FLIGHT_GUARD.value());
    }

    /**
     * Sends a command to set the drone's mode to flight mode with no guard installed.
     */
    @SuppressWarnings("unused")
    public void setFlightModeNoGuard() throws MessageNotSentException {
        sendCommandWait(CommandType.MODE_VEHICLE, ModeVehicle.FLIGHT_NO_GUARD.value());
    }

    /**
     * Sends a command to set the drone's mode to flight mode with camera installed.
     */
    @SuppressWarnings("unused")
    public void setFlightModeFPV() throws MessageNotSentException {
        sendCommandWait(CommandType.MODE_VEHICLE, ModeVehicle.FLIGHT_FPV.value());
    }

    /**
     * Sends a command to set the drone's mode to drive mode.
     */
    @SuppressWarnings("unused")
    public void setDriveMode() throws MessageNotSentException {
        sendCommandWait(CommandType.MODE_VEHICLE, ModeVehicle.DRIVE.value());
    }

    /**
     * Requests drone state
     */
    @SuppressWarnings("WeakerAccess")
    public void requestState() throws MessageNotSentException {
        Request.state(this);
    }

    /**
     * Requests drone range
     */
    @SuppressWarnings("WeakerAccess")
    public void requestRange() throws MessageNotSentException {
        Request.range(this);
    }

    /**
     * Requests drone IMU
     */
    @SuppressWarnings("WeakerAccess")
    public void requestIMU() throws MessageNotSentException {
        Request.IMU(this);
    }

    /**
     * Requests drone Image Flow
     */
    @SuppressWarnings("WeakerAccess")
    public void requestImageFlow() throws MessageNotSentException {
        Request.ImageFlow(this);
    }

    /**
     * Sends a command to make the drone take off.
     */
    @SuppressWarnings("unused")
    public void takeoff() throws MessageNotSentException {
        Flight.takeoff(this);
    }

    /**
     * Sends a command to make the drone land.
     */
    @SuppressWarnings("unused")
    public void land() throws MessageNotSentException {
        Flight.land(this);
    }

    /**
     * Sends a command to make the drone do a front flip in flight.
     */
    @SuppressWarnings("unused")
    public void flipFront() throws MessageNotSentException {
        Flight.flipFront(this);
    }

    /**
     * Sends a command to make the drone do a back flip in flight.
     */
    @SuppressWarnings("unused")
    public void flipRear() throws MessageNotSentException {
        Flight.flipRear(this);
    }

    /**
     * Sends a command to make the drone do a left flip in flight.
     */
    @SuppressWarnings("unused")
    public void flipLeft() throws MessageNotSentException {
        Flight.flipLeft(this);
    }

    /**
     * Sends a command to make the drone do a right flip in flight.
     */
    @SuppressWarnings("unused")
    public void flipRight() throws MessageNotSentException {
        Flight.flipRight(this);
    }

    /**
     * Sends a command to make the drone stop the motors. This command should be used to implement a kill switch.
     */
    @SuppressWarnings("WeakerAccess")
    public void stop() throws MessageNotSentException {
        Flight.stop(this);
    }

    /**
     * Sends a command to make the drone turn over. I have seen notes that imply this method is used when the drone
     * is not flying and is sitting upside down after a crash to right it. Testing is required.
     */
    @SuppressWarnings("unused")
    public void turnOver() throws MessageNotSentException {
        Flight.turnOver(this);
    }

    /**
     * This command is including in the protocol and possibly sends a preconfigured IR message.
     */
    @SuppressWarnings("unused")
    public void shoot() throws MessageNotSentException {
        Flight.shoot(this);
    }

    /**
     * It is unknown at this time the effect of sending this command.
     */
    @SuppressWarnings("unused")
    public void underAttack() throws MessageNotSentException {
        Flight.underAttack(this);
    }

    public void flyDirect(DirectControl control) {
        if (!control.equals(oldControl)) {
            Flight.flyDirect(this, control);
            oldControl.setControl(control);
        }
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
    public void lightModeWithCommandandIR(LightMode mode, CommandType command, byte commandOption, int irData) {
        if (mode instanceof LightModeColors) {
            LED.setLightModeCommandIR(this, (LightModeColors) mode, command, commandOption, irData);
        } else {
            throw new IllegalArgumentException("Commands can only be sent with colors specified by name.");
        }
    }

    @SuppressWarnings("unused")
    public void lightEvent(LightEvent event) {
        LED.setLightEvent(this, event);
    }

    @SuppressWarnings("unused")
    public void lightEventWithCommand(LightEvent event, CommandType command, byte commandOption) {
        if (event instanceof LightEventColors) {
            LED.setLightEventCommand(this, (LightEventColors) event, command, commandOption);
        } else {
            throw new IllegalArgumentException("Commands can only be sent with colors specified by name.");
        }
    }

    @SuppressWarnings("unused")
    public void lightEventWithCommandIR(LightEvent event, CommandType command, byte commandOption, int irData) {
        if (event instanceof LightEventColors) {
            LED.setLightEventCommandIR(this, (LightEventColors) event, command, commandOption, irData);
        } else {
            throw new IllegalArgumentException("Commands can only be sent with colors specified by name.");
        }
    }

    /**
     * Drone Information
     */
    @SuppressWarnings("unused")
    public Address getAddress() {
        return link.getAddress();
    }

    @SuppressWarnings("unused")
    public Attitude getAttitude() {
        return sensors.getAttitude();
    }

    @SuppressWarnings("unused")
    public Battery getBattery() {
        return internals.getBattery();
    }

    @SuppressWarnings("unused")
    public Button getButton() {
        return internals.getButton();
    }

    @SuppressWarnings("unused")
    public CountDrive getCountDrive() {
        return internals.getCountDrive();
    }

    @SuppressWarnings("unused")
    public CountFlight getCountFlight() {
        return internals.getCountFlight();
    }

    @SuppressWarnings("unused")
    public GyroBias getGyroBias() {
        return sensors.getGyroBias();
    }

    @SuppressWarnings("unused")
    public ImageFlow getImageFlow() throws MessageNotSentException {
        this.requestImageFlow();
        return sensors.getImageFlow();
    }

    @SuppressWarnings("unused")
    public IMU getImu() throws MessageNotSentException {
        this.requestIMU();
        return sensors.getImu();
    }

    @SuppressWarnings("unused")
    public IRMessage getIrMessage() {
        return internals.getIrMessage();
    }

    @SuppressWarnings("unused")
    public Motor getMotor() {
        return internals.getMotor();
    }

    @SuppressWarnings("unused")
    public Pressure getPressure() {
        return sensors.getPressure();
    }

    @SuppressWarnings("unused")
    public Range getRange() {
        try {
            this.requestRange();
        } catch (MessageNotSentException e) {
            e.printStackTrace();
        }
        return sensors.getRange();
    }

    @SuppressWarnings("unused")
    public State getState() {
        try {
            this.requestState();
        } catch (MessageNotSentException e) {
            log.warn("Unable to request state.", e);
        }
        return internals.getState();
    }

    @SuppressWarnings("unused")
    public Temperature getTemperature() {
        return sensors.getTemperature();
    }

    @SuppressWarnings("unused")
    public TrimAll getTrimAll() {
        return internals.getTrimAll();
    }

    @SuppressWarnings("unused")
    public TrimDrive getTrimDrive() {
        return internals.getTrimDrive();
    }

    @SuppressWarnings("unused")
    public TrimFlight getTrimFlight() {
        return internals.getTrimFlight();
    }

    @SuppressWarnings("unused")
    public boolean isFlightMode() {
        return (internals.getState() != null && internals.getState().isFlightMode());
    }

    @SuppressWarnings("unused")
    public void requestLinkState() throws InterruptedException {
        link.requestState();
    }

    public void kill() throws KillException {
        try {
            stop();
        } catch (MessageNotSentException e) {
            throw new KillException(e);
        }
    }
}
