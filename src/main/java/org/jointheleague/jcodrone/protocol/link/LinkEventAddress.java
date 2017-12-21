package org.jointheleague.jcodrone.protocol.link;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.system.EventLink;

import java.nio.ByteBuffer;

public class LinkEventAddress extends LinkEvent {

    private static final int ADDRESS_LENGTH = 6;
    private final byte[] address;

    public LinkEventAddress(EventLink event, byte result, byte[] address) {
        super(event, result);
        this.address = address;
    }

    public static byte getSize() {
        return 8;
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.put(super.toArray());
        buffer.put(address);
        return buffer.array();
    }

    public static LinkEventAddress parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        EventLink event = EventLink.fromByte(buffer.get());
        byte result = buffer.get();
        byte[] address = new byte[ADDRESS_LENGTH];
        buffer.get(address, 2, ADDRESS_LENGTH);
        return new LinkEventAddress(event, result, address);
    }
}
