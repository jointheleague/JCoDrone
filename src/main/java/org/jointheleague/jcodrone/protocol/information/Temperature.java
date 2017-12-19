package org.jointheleague.jcodrone.protocol.information;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;

public class Temperature implements Serializable {
    private final int imu;
    private final int pressure;

    public Temperature(int imu, int pressure) {
        this.imu = imu;
        this.pressure = pressure;
    }

    public static int getSize() {
        return 8;
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.putInt(imu);
        buffer.putInt(pressure);
        return buffer.array();
    }

    public static Temperature parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        int imu = buffer.getInt();
        int pressure = buffer.getInt();
        return new Temperature(imu, pressure);
    }
}
