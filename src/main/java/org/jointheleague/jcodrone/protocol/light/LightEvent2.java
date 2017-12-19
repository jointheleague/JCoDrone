package org.jointheleague.jcodrone.protocol.light;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LightEvent2 implements Serializable {
    private final LightEvent event1;
    private final LightEvent event2;

    public LightEvent2(LightEvent event1, LightEvent event2) {
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

    public static LightEvent2 parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] buffer1 = new byte[LightEvent.getSize()];
        byte[] buffer2 = new byte[LightEvent.getSize()];
        buffer.get(buffer1, 0, LightEvent.getSize());
        buffer.get(buffer2, LightEvent.getSize(), LightEvent.getSize());
        LightEvent event1 = LightEvent.parse(buffer1);
        LightEvent event2 = LightEvent.parse(buffer2);
        return new LightEvent2(event1, event2);
    }
}
