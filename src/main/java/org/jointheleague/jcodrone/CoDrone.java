package org.jointheleague.jcodrone;

import gnu.io.NRSerialPort;

public class CoDrone {
    private final boolean checkBackground;
    private final boolean showErrorMessage;
    private final boolean showLogMessage;
    private final boolean showTransferData;
    private final boolean showReceiveData;
    private NRSerialPort serialPort;

    public CoDrone(boolean checkBackground, boolean showErrorMessage, boolean showLogMessage, boolean showTransferData, boolean showReceiveData) {
        this.checkBackground = checkBackground;
        this.showErrorMessage = showErrorMessage;
        this.showLogMessage = showLogMessage;
        this.showTransferData = showTransferData;
        this.showReceiveData = showReceiveData;
    }

    public CoDrone() {
        this(true,
                false,
                false,
                false,
                false);
    }

    public CoDrone(boolean checkBackground) {
        this(checkBackground,
                false,
                false,
                false,
                false);
    }

    public boolean isOpen() {
        return isConnected();
    }

    public boolean isConnected() {
        return (this.serialPort != null && this.serialPort.isConnected());
    }

    public boolean open() {
        return this.open(null);
    }

    public boolean open(String portName) {
        if (portName == null) {
            String[] nodes = getCommPorts();
            if (nodes.length > 0) {
                portName = nodes[nodes.length - 1];
            } else {
                return false;
            }
        }

        this.serialPort = new NRSerialPort(portName, 115200);
        serial.connect();

        DataInputStream ins = new DataInputStream(serial.getInputStream());

        // read the first 10000 bytes a byte at a time
        for (int i = 0; i < 10000; i++) {
            int b = ins.read();
            if (b == -1) {
                System.out.println("got EOF - going to keep trying");
                continue;
            }
        }

        serial.disconnect();

    }

    public void close() {
        // TODO: Wait for the read thread to terminate and close the port
        if (isConnected()) {

        }
    }
}
