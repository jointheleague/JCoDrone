package org.jointheleague.jcodrone.protocol.light;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LightEvent implements Serializable {
    private final LightModeDrone mode;
    private final Colors color;
    private final short interval;
    private final short repeat;

    public LightEvent(LightModeDrone mode, Colors color, short interval, short repeat) {
        this.mode = mode;
        this.color = color;
        this.interval = interval;
        this.repeat = repeat;
    }

    public static int getSize() {
        return 4;
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(mode.value());
        buffer.put(color.value());
        buffer.put((byte) interval);
        buffer.put((byte) repeat);
        return buffer.array();
    }

    public static LightEvent parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        LightModeDrone mode = LightModeDrone.fromByte(buffer.get());
        Colors color = Colors.fromByte(buffer.get());
        short interval = buffer.get();
        short repeat = buffer.get();
        return new LightEvent(mode, color, interval, repeat);

    }
}
