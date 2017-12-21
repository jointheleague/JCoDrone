package org.jointheleague.jcodrone.protocol.common;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;

public class Passcode implements Serializable {
    private final int passcode;

    public Passcode(int passcode) {
        this.passcode = passcode;
    }

    public static byte getSize() {
        return 4;
    }

    public byte getInstanceSize() {
        return getSize();
    }

    @Override
    public byte[] toArray() {
        return ByteBuffer.allocate(getSize()).putInt(passcode).array();
    }

    public static Passcode parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        int passcode = buffer.getInt();
        return new Passcode(passcode);
    }
}
