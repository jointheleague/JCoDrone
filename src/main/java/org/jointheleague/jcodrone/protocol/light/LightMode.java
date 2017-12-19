package org.jointheleague.jcodrone.protocol.light;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LightMode implements Serializable {
    private final LightModeDrone mode;
    private final Colors color;
    private final short interval;

    public LightMode(LightModeDrone mode, Colors color, short interval) {
        this.mode = mode;
        this.color = color;
        this.interval = interval;
    }

    public static int getSize() {
        return 3;
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(mode.value());
        buffer.put(color.value());
        buffer.put((byte) interval);
        return buffer.array();
    }

    public static LightMode parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        LightModeDrone mode = LightModeDrone.fromByte(buffer.get());
        Colors color = Colors.fromByte(buffer.get());
        short interval = buffer.get();
        return new LightMode(mode, color, interval);

    }
}
