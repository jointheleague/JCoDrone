package org.jointheleague.jcodrone.protocol.information;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;

public class Address implements Serializable {
    private final byte[] address;
    private int size;

    public Address(byte[] address) {
        this.address = address;
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
        buffer.put(address);
        return buffer.array();
    }

    public static Address parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        byte[] address = new byte[6];
        buffer.get(address, 0, getSize());
        return new Address(address);
    }

}
