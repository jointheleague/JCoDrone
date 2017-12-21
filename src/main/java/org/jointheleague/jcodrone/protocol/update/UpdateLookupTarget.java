package org.jointheleague.jcodrone.protocol.update;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;
import org.jointheleague.jcodrone.system.DeviceType;

import java.nio.ByteBuffer;

public class UpdateLookupTarget implements Serializable {
    private final DeviceType deviceType;

    public UpdateLookupTarget(DeviceType deviceType) {
        this.deviceType = deviceType;
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
        buffer.putInt(deviceType.value());
        return buffer.array();
    }

    public static DeviceType parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        int deviceType = buffer.getInt();
        return DeviceType.forValue(deviceType);
    }
}
