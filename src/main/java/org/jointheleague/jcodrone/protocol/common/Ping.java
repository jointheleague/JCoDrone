package org.jointheleague.jcodrone.protocol.common;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Ping implements Serializable {
    private final int systemTime;

    public Ping(int systemTime) {
        this.systemTime = systemTime;
    }

    public static int getSize() {
        return 4;
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(systemTime);
        return buffer.array();
    }

    public static Ping parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        int systemTime = buffer.getInt();
        return new Ping(systemTime);
    }
}
