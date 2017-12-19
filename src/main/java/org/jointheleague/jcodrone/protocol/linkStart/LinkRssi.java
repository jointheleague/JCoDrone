package org.jointheleague.jcodrone.protocol.linkStart;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;

public class LinkRssi implements Serializable {
    private final byte rssi;

    public LinkRssi(byte rssi) {
        this.rssi = rssi;
    }

    public static int getSize() {
        return 1;
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.put(rssi);
        return buffer.array();
    }

    public static LinkRssi parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        byte rssi = buffer.get();
        return new LinkRssi(rssi);
    }

}
