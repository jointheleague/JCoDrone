package org.jointheleague.jcodrone.protocol.light;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LightModeColors2 implements Serializable {
    private final LightModeColors mode1;
    private final LightModeColors mode2;

    public LightModeColors2(LightModeColors mode1, LightModeColors mode2) {
        this.mode1 = mode1;
        this.mode2 = mode2;
    }

    public static byte getSize() {
        return (byte) (2 * LightModeColors.getSize());
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

    public static LightModeColors2 parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] buffer1 = new byte[LightModeColors.getSize()];
        byte[] buffer2 = new byte[LightModeColors.getSize()];
        buffer.get(buffer1, 0, LightModeColors.getSize());
        buffer.get(buffer2, LightModeColors.getSize(), LightModeColors.getSize());
        LightModeColors mode1 = LightModeColors.parse(buffer1);
        LightModeColors mode2 = LightModeColors.parse(buffer2);
        return new LightModeColors2(mode1, mode2);
    }
}
