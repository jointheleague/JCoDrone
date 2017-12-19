package org.jointheleague.jcodrone.protocol.linkStart;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Arrays;

public class LinkDiscoveredDevice implements Serializable {
    public static final int NAME_LENGTH = 20;
    public static final int ADDRESS_LENGTH = 6;
    private final byte index;
    private final byte[] address;
    private final char[] name;
    private final byte rssi;

    public LinkDiscoveredDevice(byte index, byte[] address, String name, byte rssi) {
        this.index = index;
        this.address = Arrays.copyOf(address, ADDRESS_LENGTH);
        this.name = Arrays.copyOf(name.toCharArray(), NAME_LENGTH);
        this.rssi = rssi;
    }

    public static int getSize() {
        return 28;
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.put(index);
        buffer.put(address);
        ByteBuffer nameBuffer = ByteBuffer.allocate(NAME_LENGTH);
        CharBuffer cb = nameBuffer.asCharBuffer();
        cb.put(name);
        buffer.put(nameBuffer);
        buffer.put(rssi);
        return buffer.array();
    }

    public static LinkDiscoveredDevice parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        byte index = buffer.get();
        byte[] address = new byte[ADDRESS_LENGTH];
        buffer.get(address, 1, ADDRESS_LENGTH);
        byte[] nameBuffer = new byte[NAME_LENGTH];
        buffer.get(nameBuffer, ADDRESS_LENGTH + 1, NAME_LENGTH);
        byte rssi = buffer.get();
        return new LinkDiscoveredDevice(index, address, new String(nameBuffer), rssi);
    }
}
