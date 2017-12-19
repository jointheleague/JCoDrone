package org.jointheleague.jcodrone.protocol.light;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LightMode2 implements Serializable {
    private final LightMode mode1;
    private final LightMode mode2;

    public LightMode2(LightMode mode1, LightMode mode2) {
        this.mode1 = mode1;
        this.mode2 = mode2;
    }

    public static int getSize() {
        return 2 * LightMode.getSize();
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(mode1.toArray());
        buffer.put(mode2.toArray());
        return buffer.array();
    }

    public static LightMode2 parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] buffer1 = new byte[LightMode.getSize()];
        byte[] buffer2 = new byte[LightMode.getSize()];
        buffer.get(buffer1, 0, LightMode.getSize());
        buffer.get(buffer2, LightMode.getSize(), LightMode.getSize());
        LightMode mode1 = LightMode.parse(buffer1);
        LightMode mode2 = LightMode.parse(buffer2);
        return new LightMode2(mode1, mode2);
    }
}
