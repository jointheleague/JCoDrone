package org.jointheleague.jcodrone.protocol.light;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LightModeColor extends LightMode {
    private final Color color;

    public LightModeColor(LightModeDrone mode, Color color, short interval) {
        super(interval, mode);
        this.color = color;
    }

    public static byte getSize() {
        return (byte) (1 + Color.getSize() + 1);
    }

    public byte getInstanceSize() {
        return getSize();
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(mode.value());
        buffer.put(color.toArray());
        buffer.put((byte) interval);
        return buffer.array();
    }

    public static LightModeColor parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        LightModeDrone mode = LightModeDrone.fromByte(buffer.get());
        byte[] colorBuffer = new byte[Color.getSize()];
        buffer.get(colorBuffer, 1, Color.getSize());
        Color color = Color.parse(colorBuffer);
        short interval = buffer.get();
        short repeat = buffer.get();
        return new LightModeColor(mode, color, interval);

    }
}
