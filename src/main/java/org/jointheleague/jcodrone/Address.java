package org.jointheleague.jcodrone;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;
import org.jointheleague.jcodrone.protocol.link.LinkDiscoveredDevice;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Address implements Serializable {
    private final byte[] address;
    private int size;

    public Address(byte[] address) {
        this.address = address;
    }

    public static byte getSize() {
        return 6;
    }

    public static Address parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        byte[] address = new byte[getSize()];
        buffer.get(address, 0, getSize());
        return new Address(address);
    }

    public static Address parse(String address) {
        byte[] bytes = Stream.of(address.split(":")).mapToInt(i -> Integer.parseInt(i, 16)).
                collect(ByteArrayOutputStream::new, (baos, i) -> baos.write((byte) i),
                        (baos1, baos2) -> baos1.write(baos2.toByteArray(), 0, baos2.size()))
                .toByteArray();

        return new Address(bytes);
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

    public byte[] getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return LinkDiscoveredDevice.intStream(address).mapToObj(i -> String.format("%02X", 0x0FF & i)).collect(Collectors.joining(":"));
    }
}
