package org.jointheleague.jcodrone.protocol.information;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;

public class TrimFlight implements Serializable {
    private final short roll;
    private final short pitch;
    private final short yaw;
    private final short throttle;

    public TrimFlight(short roll, short pitch, short yaw, short throttle) {
        this.roll = roll;
        this.pitch = pitch;
        this.yaw = yaw;
        this.throttle = throttle;
    }

    public static byte getSize() {
        return 8;
    }

    public byte getInstanceSize() {
        return getSize();
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.putShort(roll);
        buffer.putShort(pitch);
        buffer.putShort(yaw);
        buffer.putShort(throttle);
        return buffer.array();
    }

    public static TrimFlight parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        short roll = buffer.getShort();
        short pitch = buffer.getShort();
        short yaw = buffer.getShort();
        short throttle = buffer.getShort();
        return new TrimFlight(roll, pitch, yaw, throttle);
    }
}
