package org.jointheleague.jcodrone.protocol.common;

import org.jointheleague.jcodrone.protocol.DataType;
import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;

public class Ack implements Serializable {
    private final int systemTime;
    private final DataType dataType;

    public Ack(int time, DataType type) {
        this.systemTime = time;
        dataType = type;
    }

    public static byte getSize() {
        return 5;
    }

    public byte getInstanceSize() {
        return getSize();
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.putInt(systemTime);
        buffer.put(dataType.value());
        return buffer.array();
    }

    public static Ack parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        int systemTime = buffer.getInt();
        DataType type = DataType.fromByte(buffer.get());
        return new Ack(systemTime, type);
    }
}
