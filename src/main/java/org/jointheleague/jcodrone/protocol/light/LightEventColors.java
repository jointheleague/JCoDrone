package org.jointheleague.jcodrone.protocol.light;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LightEventColors extends LightEvent {
    private final Colors color;

    public LightEventColors(LightModeDrone mode, Colors color, short interval, short repeat) {
        super(interval, repeat, mode);
        this.color = color;
    }

    public static byte getSize() {
        return 4;
    }

    @Override
    public byte getInstanceSize() {
        return getSize();
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

    public static LightEventColors parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        LightModeDrone mode = LightModeDrone.fromByte(buffer.get());
        Colors color = Colors.fromByte(buffer.get());
        short interval = buffer.get();
        short repeat = buffer.get();
        return new LightEventColors(mode, color, interval, repeat);

    }
}
