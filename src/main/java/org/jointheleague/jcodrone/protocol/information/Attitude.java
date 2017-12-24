package org.jointheleague.jcodrone.protocol.information;

import org.jointheleague.jcodrone.CoDrone;
import org.jointheleague.jcodrone.Internals;
import org.jointheleague.jcodrone.Link;
import org.jointheleague.jcodrone.Sensors;
import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;

public class Attitude implements Serializable {
    private final short roll;
    private final short pitch;
    private final short yaw;

    public Attitude(short roll, short pitch, short yaw) {
        this.roll = roll;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public static byte getSize() {
        return 6;
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
        return buffer.array();
    }

    public static Attitude parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        short roll = buffer.getShort();
        short pitch = buffer.getShort();
        short yaw = buffer.getShort();
        return new Attitude(roll, pitch, yaw);
    }

    @Override
    public void handle(CoDrone coDrone, Link link, Sensors sensors, Internals internals) {
        sensors.setAttitude(this);
    }
}
