package org.jointheleague.jcodrone.protocol.light;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LightModeColor2 extends LightMode2 implements Serializable {
    private final LightModeColor mode1;
    private final LightModeColor mode2;

    public LightModeColor2(LightModeColor mode1, LightModeColor mode2) {
        this.mode1 = mode1;
        this.mode2 = mode2;
    }

    public static byte getSize() {
        return (byte) (2 * LightModeColor.getSize());
    }

    public byte getInstanceSize() {
        return getSize();
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(mode1.toArray());
        buffer.put(mode2.toArray());
        return buffer.array();
    }

    public static LightModeColor2 parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] buffer1 = new byte[LightModeColor.getSize()];
        byte[] buffer2 = new byte[LightModeColor.getSize()];
        buffer.get(buffer1, 0, LightModeColor.getSize());
        buffer.get(buffer2, 0, LightModeColor.getSize());
        LightModeColor mode1 = LightModeColor.parse(buffer1);
        LightModeColor mode2 = LightModeColor.parse(buffer2);
        return new LightModeColor2(mode1, mode2);
    }
}
