package org.jointheleague.jcodrone.protocol.common;

import org.jointheleague.jcodrone.protocol.DataType;
import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;

public class Request implements Serializable {
    private final DataType dataType;

    public Request(DataType type) {
        dataType = type;
    }

    public static int getSize() {
        return 1;
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.put(dataType.value());
        return buffer.array();
    }

    public static Request parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        DataType type = DataType.fromByte(buffer.get());
        return new Request(type);
    }
}
