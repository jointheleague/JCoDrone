package org.jointheleague.jcodrone.protocol.light;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;
import org.jointheleague.jcodrone.protocol.common.Command;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LightModeCommand implements Serializable {
    private final LightMode mode;
    private final Command command;

    public LightModeCommand(LightMode mode, Command command) {
        this.mode = mode;
        this.command = command;
    }

    public static int getSize() {
        return LightMode.getSize() + Command.getSize();
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(mode.toArray());
        buffer.put(command.toArray());
        return buffer.array();
    }

    public static LightModeCommand parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] modeBuffer = new byte[LightMode.getSize()];
        byte[] commandBuffer = new byte[Command.getSize()];
        buffer.get(modeBuffer, 0, LightMode.getSize());
        buffer.get(commandBuffer, LightMode.getSize(), Command.getSize());
        LightMode mode = LightMode.parse(modeBuffer);
        Command command = Command.parse(commandBuffer);
        return new LightModeCommand(mode, command);
    }
}
