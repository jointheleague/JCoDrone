package org.jointheleague.jcodrone.protocol.light;

import org.jointheleague.jcodrone.LightModeBuilder;
import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LightModeColors extends LightMode {
    private final Colors color;

    public LightModeColors(LightModeDrone mode, Colors color, short interval) {
        super(interval, mode);
        this.color = color;
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
        buffer.put(mode.value());
        buffer.put(color.value());
        buffer.put((byte) interval);
        return buffer.array();
    }

    public static LightModeColors parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        LightModeDrone mode = LightModeDrone.fromByte(buffer.get());
        Colors color = Colors.fromByte(buffer.get());
        short interval = buffer.get();
        return new LightModeColors(mode, color, interval);
    }
}
