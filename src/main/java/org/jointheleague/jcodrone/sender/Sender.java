package org.jointheleague.jcodrone.sender;

import com.fazecast.jSerialComm.SerialPort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jointheleague.jcodrone.protocol.DataType;
import org.jointheleague.jcodrone.protocol.Header;
import org.jointheleague.jcodrone.protocol.Serializable;
import org.jointheleague.jcodrone.receiver.CRC16;

import javax.xml.bind.DatatypeConverter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.LinkedBlockingQueue;

public class Sender extends Thread {
    private static Logger log = LogManager.getLogger(Sender.class);
    private final LinkedBlockingQueue<Serializable> commandQueue;
    private final SerialPort comPort;
    private boolean run = true;

    public Sender(SerialPort comPort, LinkedBlockingQueue<Serializable> commandQueue) {
        this.comPort = comPort;
        this.commandQueue = commandQueue;
    }

    public void start() {
        Thread thread = new Thread(this, "Sender");
        thread.start();
    }

    @Override
    public void run() {
        while (run) {
            try {
                Serializable command = commandQueue.take();
                transfer(command);
                Thread.sleep(60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    void transfer(Serializable command) {
        Header header = new Header(DataType.fromClass(command.getClass()), command.getInstanceSize());

        int crc16 = 0;
        crc16 = CRC16.calc(header.toArray(), crc16);
        crc16 = CRC16.calc(command.toArray(), crc16);

        byte[] headerArray = header.toArray();
        byte[] dataArray = command.toArray();

        ByteBuffer data = ByteBuffer.allocate(2 + headerArray.length + dataArray.length + 2);
        data.order(ByteOrder.LITTLE_ENDIAN);
        data.put((byte) 0x0A);
        data.put((byte) 0x55);
        data.put(headerArray);
        data.put(dataArray);
        data.putShort((short) crc16);

        comPort.writeBytes(data.array(), data.capacity());
        log.info("Sent: {}", DatatypeConverter.printHexBinary(data.array()));
    }

}
