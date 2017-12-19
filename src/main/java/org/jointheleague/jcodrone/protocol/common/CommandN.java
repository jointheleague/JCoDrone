package org.jointheleague.jcodrone.protocol.common;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;

public class CommandN implements Serializable {
    private static final int NUMBER_OF_COMMANDS = 1;
    private final Command[] commands;

    public CommandN(Command[] commands) {
        this.commands = commands;
    }

    public static int getSize() {
        return NUMBER_OF_COMMANDS * Command.getSize();
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        for (int i = 0; i < getNumberOfCommands(); i++) {
            buffer.put(commands[i].toArray());
        }
        return buffer.array();
    }

    public static CommandN parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        byte[] command_bytes = new byte[getSize()];
        Command[] commands = new Command[2];

        int position = 0;
        for (int i = 0; i < NUMBER_OF_COMMANDS; i++) {
            buffer.get(command_bytes, position, getSize());
            commands[i] = Command.parse(command_bytes);
            position += getSize();
        }

        return new CommandN(commands);
    }

    public int getNumberOfCommands() {
        return NUMBER_OF_COMMANDS;
    }
}
