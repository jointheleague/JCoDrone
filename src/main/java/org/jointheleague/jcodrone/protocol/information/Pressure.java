package org.jointheleague.jcodrone.protocol.information;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;

public class Pressure implements Serializable {
    private final int d1;
    private final int d2;
    private final int temperature;
    private final int pressure;

    public Pressure(int d1, int d2, int temperature, int pressure) {
        this.d1 = d1;
        this.d2 = d2;
        this.temperature = temperature;
        this.pressure = pressure;
    }

    public static int getSize() {
        return 16;
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.putInt(d1);
        buffer.putInt(d2);
        buffer.putInt(temperature);
        buffer.putInt(pressure);
        return buffer.array();
    }

    public static Pressure parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        int d1 = buffer.getInt();
        int d2 = buffer.getInt();
        int temperature = buffer.getInt();
        int pressure = buffer.getInt();
        return new Pressure(d1, d2, temperature, pressure);
    }
}
