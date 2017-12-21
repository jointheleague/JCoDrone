package org.jointheleague.jcodrone.protocol.information;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;

public class MotorBlock implements Serializable {
    private final short forward;
    private final short reverse;

    public MotorBlock(short forward, short reverse) {
        this.forward = forward;
        this.reverse = reverse;
    }

    public static byte getSize() {
        return 4;
    }

    public byte getInstanceSize() {
        return getSize();
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.putShort(forward);
        buffer.putShort(reverse);
        return buffer.array();
    }

    public static MotorBlock parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        short forward = buffer.getShort();
        short reverse = buffer.getShort();
        return new MotorBlock(forward, reverse);
    }
}
