package org.jointheleague.jcodrone.protocol.information;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;
import org.jointheleague.jcodrone.system.Direction;

import java.nio.ByteBuffer;

public class IRMessage implements Serializable {
    private final Direction direction;
    private final int irData;

    public IRMessage(Direction direction, int irData) {
        this.direction = direction;
        this.irData = irData;
    }

    public static int getSize() {
        return 5;
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.put(direction.value());
        buffer.putInt(irData);
        return buffer.array();
    }

    public static IRMessage parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        Direction direction = Direction.fromByte(buffer.get());
        int irData = buffer.getInt();
        return new IRMessage(direction, irData);
    }
}
