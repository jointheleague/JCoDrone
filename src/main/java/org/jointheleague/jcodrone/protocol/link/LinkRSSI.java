package org.jointheleague.jcodrone.protocol.link;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;

public class LinkRSSI implements Serializable {
    private final byte rssi;

    public LinkRSSI(byte rssi) {
        this.rssi = rssi;
    }

    public static byte getSize() {
        return 1;
    }

    public byte getInstanceSize() {
        return getSize();
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.put(rssi);
        return buffer.array();
    }

    public static LinkRSSI parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        byte rssi = buffer.get();
        return new LinkRSSI(rssi);
    }

}
