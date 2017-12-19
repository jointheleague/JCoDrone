package org.jointheleague.jcodrone.protocol.update;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Update implements Serializable {
    private static final int DATA_ARRAY_LENGTH = 16;
    private final short indexBlock;
    private final byte[] dataArray;

    public Update(short indexBlock, byte[] dataArray) {
        this.indexBlock = indexBlock;
        this.dataArray = Arrays.copyOf(dataArray, DATA_ARRAY_LENGTH);
    }

    public static int getSize() {
        return 18;
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.putShort(indexBlock);
        buffer.put(dataArray);
        return buffer.array();
    }

    public static Update parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        short indexBlock = buffer.getShort();
        byte[] dataArray = new byte[DATA_ARRAY_LENGTH];
        buffer.get(dataArray, 2, DATA_ARRAY_LENGTH);
        return new Update(indexBlock, dataArray);
    }
}
