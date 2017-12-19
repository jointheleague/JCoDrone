package org.jointheleague.jcodrone.protocol.common;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;

public class Control implements Serializable {
    private final byte roll;
    private final byte pitch;
    private final byte yaw;
    private final byte throttle;

    public Control(byte roll, byte pitch, byte yaw, byte throttle) {
        this.roll = roll;
        this.pitch = pitch;
        this.yaw = yaw;
        this.throttle = throttle;
    }

    public static int getSize() {
        return 4;
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.put(roll);
        buffer.put(pitch);
        buffer.put(yaw);
        buffer.put(throttle);
        return buffer.array();
    }

    public static Control parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        byte roll = buffer.get();
        byte pitch = buffer.get();
        ;
        byte yaw = buffer.get();
        ;
        byte throttle = buffer.get();
        ;

        return new Control(roll, pitch, yaw, throttle);
    }
}
