package org.jointheleague.jcodrone.protocol.light;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LightEventColor2 implements Serializable {
    private final LightEventColor event1;
    private final LightEventColor event2;

    public LightEventColor2(LightEventColor event1, LightEventColor event2) {
        this.event1 = event1;
        this.event2 = event2;
    }

    public static int getSize() {
        return 2 * LightEvent.getSize();
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(event1.toArray());
        buffer.put(event2.toArray());
        return buffer.array();
    }

    public static LightEventColor2 parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] buffer1 = new byte[LightEventColor.getSize()];
        byte[] buffer2 = new byte[LightEventColor.getSize()];
        buffer.get(buffer1, 0, LightEventColor.getSize());
        buffer.get(buffer2, LightEventColor.getSize(), LightEventColor.getSize());
        LightEventColor event1 = LightEventColor.parse(buffer1);
        LightEventColor event2 = LightEventColor.parse(buffer2);
        return new LightEventColor2(event1, event2);
    }
}
