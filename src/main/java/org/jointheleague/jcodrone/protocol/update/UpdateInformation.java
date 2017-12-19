package org.jointheleague.jcodrone.protocol.update;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;
import org.jointheleague.jcodrone.system.DeviceType;
import org.jointheleague.jcodrone.system.ImageType;
import org.jointheleague.jcodrone.system.ModeUpdate;

import java.nio.ByteBuffer;

public class UpdateInformation implements Serializable {
    private final ModeUpdate mode;
    private final DeviceType deviceType;
    private final ImageType imageType;
    private final short version;
    private final byte year;
    private final byte month;
    private final byte day;

    public UpdateInformation(ModeUpdate mode, DeviceType deviceType, ImageType imageType, short version, byte year, byte month, byte day) {
        this.mode = mode;
        this.deviceType = deviceType;
        this.imageType = imageType;
        this.version = version;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public static int getSize() {
        return 11;
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.put(mode.value());
        buffer.putInt(deviceType.value());
        buffer.put(imageType.value());
        buffer.putShort(version);
        buffer.put(year);
        buffer.put(month);
        buffer.put(day);
        return buffer.array();
    }

    public static UpdateInformation parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        ModeUpdate mode = ModeUpdate.forValue(buffer.get());
        DeviceType deviceType = DeviceType.forValue(buffer.getInt());
        ImageType imageType = ImageType.forValue(buffer.get());

        short version = buffer.getShort();
        byte year = buffer.get();
        byte month = buffer.get();
        byte day = buffer.get();

        return new UpdateInformation(
                mode,
                deviceType,
                imageType,
                version,
                year,
                month,
                day
        );
    }
}
