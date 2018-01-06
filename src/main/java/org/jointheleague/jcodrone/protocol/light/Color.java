package org.jointheleague.jcodrone.protocol.light;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.jointheleague.jcodrone.protocol.Validator.isValidUnsignedByte;

public class Color implements Serializable {
    private final int r;
    private final int g;
    private final int b;

    public Color(int r, int g, int b) {
        if (!(isValidUnsignedByte(r) && isValidUnsignedByte(g) && isValidUnsignedByte(b))) {
            throw new IllegalArgumentException("Colors must be in the range of 0 to 255.");
        }
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
        int r = buffer.get();
        int g = buffer.get();
        int b = buffer.get();
        return new Color(r, g, b);
    }
}
