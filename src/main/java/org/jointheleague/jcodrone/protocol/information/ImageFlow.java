package org.jointheleague.jcodrone.protocol.information;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;

public class ImageFlow implements Serializable {
    private final int positionX;
    private final int positionY;

    public ImageFlow(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public static byte getSize() {
        return 8;
    }

    public byte getInstanceSize() {
        return getSize();
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.putInt(positionX);
        buffer.putInt(positionY);
        return buffer.array();
    }

    public static ImageFlow parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        int positionX = buffer.getInt();
        int positionY = buffer.getInt();
        return new ImageFlow(positionX, positionY);
    }
}
