package org.jointheleague.jcodrone;


import com.fazecast.jSerialComm.SerialPort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jointheleague.jcodrone.protocol.CommandType;
import org.jointheleague.jcodrone.protocol.DataType;
import org.jointheleague.jcodrone.protocol.Header;
import org.jointheleague.jcodrone.protocol.Serializable;
import org.jointheleague.jcodrone.protocol.common.Command;
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
import java.util.Deque;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CoDrone implements AutoCloseable {
    private static Logger log = LogManager.getLogger(CoDrone.class);
    private final Link link;
    private SerialPort comPort;
    private Receiver receiver;
    private Deque<Byte> buffer;
    private boolean discoveringDevices;
    private InputStream in;
    private InputStream inputStream;
    private boolean stopped = true;
    private Thread readThread;
    private boolean flightMode;
    private boolean flying;
    public CoDrone() {
        receiver = new Receiver(this);
        link = new Link(this);
        log.info("CoDrone Setup");
    }

    private void open() throws Exception {
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
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
        inputStream = comPort.getInputStream();

        readThread = new Thread(new Runnable() {
            @Override
            public void run() {
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
            }
        });
        readThread.start();
    }

    @Override
    public void close() throws Exception {
        log.info("Closing serial port.");
        stopped = true;
        readThread.join(200);
        if (readThread.isAlive()) {
            log.warn("Interrupting serial communication");
            readThread.interrupt();
        }
        comPort.closePort();
        log.info("Port {} closed", comPort.getSystemPortName());
    }

    public void connect() throws Exception {
        connect(null, null, false);
    }

    public void connect(String portName, String deviceName, boolean resetSystem) throws Exception {
        if (deviceName != null && !deviceName.isEmpty() && deviceName.length() != 12) {
            throw new IllegalAccessException(
                    String.format("Invalid device name length {}.", deviceName.length()));
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

    byte[] transfer(Header header, Serializable data) {
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
        return message.array();
    }

    byte[] sendCommand(CommandType type) {
        return sendCommand(type, (byte) 0);
    }

    byte[] sendCommand(CommandType type, byte option) {
        Header header = new Header(DataType.COMMAND, Command.getSize());
        Command command = new Command(type, option);

        return transfer(header, command);
    }

    public void sendMessage(Serializable message) {
        Header header = new Header(DataType.fromClass(message.getClass()), message.getInstanceSize());

        transfer(header, message);
    }

    private Link getLink() {
        return link;
    }

    /**
     * Flight Commands
     */

    public void setFlightMode() {
        sendCommand(CommandType.MODE_VEHICLE, ModeVehicle.FLIGHT_GUARD.value());
    }

    public void setDriveMode() {
        sendCommand(CommandType.MODE_VEHICLE, ModeVehicle.DRIVE.value());
    }

    public void takeoff() {
        Flight.takeoff(this);
    }

    public void land() {
        Flight.land(this);
    }

    public void flipFront() {
        Flight.flipFront(this);
    }

    public void flipRear() {
        Flight.flipRear(this);
    }

    public void flipLeft() {
        Flight.flipLeft(this);
    }

    public void flipRight() {
        Flight.flipRight(this);
    }

    public void stop() {
        Flight.stop(this);
    }

    public void turnOver() {
        Flight.turnOver(this);
    }

    public void shoot() {
        Flight.shoot(this);
    }

    public void underAttack() {
        Flight.underAttack(this);
    }

    public boolean isFlightMode() {
        return flightMode;
    }

    public boolean isFlying() {
        return flying;
    }

    /**
     * LED Commands
     */
    public void lightMode(LightMode mode) {
        LED.setMode(this, mode, false);
    }

    public void defaultLightMode(LightMode mode) {
        LED.setMode(this, mode, true);
    }

    public void lightModes(LightMode mode1, LightMode mode2) {
        LED.setMode2(this, mode1, mode2, false);
    }

    public void defaultLightModes(LightMode mode1, LightMode mode2) {
        LED.setMode2(this, mode1, mode2, true);
    }

    public void lightModeWithCommand(LightMode mode, CommandType command, byte commandOption) {
        if (mode instanceof LightModeColors) {
            LED.setModeCommand(this, (LightModeColors) mode, command, commandOption);
        } else {
            throw new IllegalArgumentException("Commands can only be sent with colors specified by name.");
        }
    }

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
}
