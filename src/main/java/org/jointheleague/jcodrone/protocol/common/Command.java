package org.jointheleague.jcodrone.protocol.common;

import org.jointheleague.jcodrone.protocol.CommandType;
import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;

public class Command implements Serializable {
    private final CommandType commandType;
    private final byte option;

    public Command(CommandType commandType, byte option) {
        this.commandType = commandType;
        this.option = option;
    }

    public static int getSize() {
        return 2;
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.put(commandType.value());
        buffer.put(option);
        return buffer.array();
    }

    public static Command parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        CommandType type = CommandType.fromByte(buffer.get());
        byte option = buffer.get();
        return new Command(type, option);
    }
}
