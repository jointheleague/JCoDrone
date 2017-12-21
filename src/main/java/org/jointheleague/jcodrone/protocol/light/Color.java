package org.jointheleague.jcodrone.protocol.light;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Color implements Serializable {
    private final short r;
    private final short g;
    private final short b;

    public Color(short r, short g, short b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static byte getSize() {
        return 3;
    }

    public byte getInstanceSize() {
        return getSize();
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) r);
        buffer.put((byte) g);
        buffer.put((byte) b);
        return buffer.array();
    }

    public static Color parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        short r = buffer.get();
        short g = buffer.get();
        short b = buffer.get();
        return new Color(r, g, b);
    }
}
