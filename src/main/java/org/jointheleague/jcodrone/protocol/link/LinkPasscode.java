package org.jointheleague.jcodrone.protocol.link;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;

public class LinkPasscode implements Serializable {
    public final byte passcode;

    public LinkPasscode(byte passcode) {
        this.passcode = passcode;
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
        buffer.put(passcode);
        return buffer.array();
    }

    public static LinkPasscode parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        byte passcode = buffer.get();
        return new LinkPasscode(passcode);
    }
}
