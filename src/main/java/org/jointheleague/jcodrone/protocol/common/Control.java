package org.jointheleague.jcodrone.protocol.common;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;

public class Control implements Serializable {
    private final int roll;
    private final int pitch;
    private final int yaw;
    private final int throttle;

    public Control(int roll, int pitch, int yaw, int throttle) {
        this.roll = roll;
        this.pitch = pitch;
        this.yaw = yaw;
        this.throttle = throttle;
    }

    public static byte getSize() {
        return 4;
    }

    public byte getInstanceSize() {
        return getSize();
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.put((byte) roll);
        buffer.put((byte) pitch);
        buffer.put((byte) yaw);
        buffer.put((byte) throttle);
        return buffer.array();
    }

    public static Control parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        byte roll = buffer.get();
        byte pitch = buffer.get();
        byte yaw = buffer.get();
        byte throttle = buffer.get();

        return new Control(roll, pitch, yaw, throttle);
    }
}
