package org.jointheleague.jcodrone.protocol;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Header implements Serializable {
    private DataType dataType;
    private byte length;

    public Header(DataType dataType, byte length) {
        this.dataType = dataType;
        this.length = length;
    }

    public Header() {
        this.dataType = DataType.NONE;
        this.length = 0;
    }

    public static byte getSize() {
        return 2;
    }

    public byte getInstanceSize() {
        return getSize();
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(dataType.value());
        buffer.put((byte) length);
        return buffer.array();
    }

    static Header parse(byte[] data) {
        if (data.length != getSize()) {
            return null;
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        DataType dataType = DataType.fromByte(buffer.get());
        byte length = buffer.get();
        return new Header(dataType, length);
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public void setLength(byte length) {
        this.length = length;
    }

    public byte getLength() {
        return length;
    }

    public DataType getDataType() {
        return dataType;
    }
}

