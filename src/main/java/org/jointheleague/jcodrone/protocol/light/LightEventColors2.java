package org.jointheleague.jcodrone.protocol.light;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LightEventColors2 implements Serializable {
    private final LightEventColors event1;
    private final LightEventColors event2;

    public LightEventColors2(LightEventColors event1, LightEventColors event2) {
        this.event1 = event1;
        this.event2 = event2;
    }

    public static byte getSize() {
        return (byte) (2 * LightEventColors.getSize());
    }

    public byte getInstanceSize() {
        return getSize();
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(event1.toArray());
        buffer.put(event2.toArray());
        return buffer.array();
    }

    public static LightEventColors2 parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] buffer1 = new byte[LightEventColors.getSize()];
        byte[] buffer2 = new byte[LightEventColors.getSize()];
        buffer.get(buffer1, 0, LightEventColors.getSize());
        buffer.get(buffer2, 0, LightEventColors.getSize());
        LightEventColors event1 = LightEventColors.parse(buffer1);
        LightEventColors event2 = LightEventColors.parse(buffer2);
        return new LightEventColors2(event1, event2);
    }
}
