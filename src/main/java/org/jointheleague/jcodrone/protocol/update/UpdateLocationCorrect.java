package org.jointheleague.jcodrone.protocol.update;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;

public class UpdateLocationCorrect implements Serializable {
    private final short indexBlockNext;

    public UpdateLocationCorrect(short indexBlockNext) {
        this.indexBlockNext = indexBlockNext;
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
        buffer.putShort(indexBlockNext);
        return buffer.array();
    }

    public static UpdateLocationCorrect parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        short indexBlockNext = buffer.getShort();
        return new UpdateLocationCorrect(indexBlockNext);
    }
}
