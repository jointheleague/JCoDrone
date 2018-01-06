package org.jointheleague.jcodrone.protocol.information;

import org.jointheleague.jcodrone.CoDrone;
import org.jointheleague.jcodrone.Internals;
import org.jointheleague.jcodrone.Link;
import org.jointheleague.jcodrone.Sensors;
import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;

public class Motor implements Serializable {
    private static final int COUNT_OF_BLOCKS = 4;
    private final MotorBlock[] motor;

    public Motor(MotorBlock[] motor) {
        this.motor = motor;
    }

    public static byte getSize() {
        return (byte) (MotorBlock.getSize() * COUNT_OF_BLOCKS);
    }

    public byte getInstanceSize() {
        return getSize();
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        for (int i = 0; i < COUNT_OF_BLOCKS; i++) {
            buffer.put(motor[i].toArray());
        }
        return buffer.array();
    }

    public static Motor parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        MotorBlock[] blocks = new MotorBlock[COUNT_OF_BLOCKS];
        for (int i = 0; i < COUNT_OF_BLOCKS; i++) {
            byte[] blockBuffer = new byte[MotorBlock.getSize()];
            buffer.get(blockBuffer, 0, MotorBlock.getSize());
            blocks[i] = MotorBlock.parse(blockBuffer);
        }
        return new Motor(blocks);
    }

    @Override
    public void handle(CoDrone coDrone, Link link, Sensors sensors, Internals internals) {
        internals.setMotor(this);
    }
}
