package org.jointheleague.jcodrone.protocol.light;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;
import org.jointheleague.jcodrone.protocol.common.Command;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LightEventCommand implements Serializable {
    private final LightEventColors event;
    private final Command command;

    public LightEventCommand(LightEventColors event, Command command) {
        this.event = event;
        this.command = command;
    }

    public static byte getSize() {
        return (byte) (LightEventColors.getSize() + Command.getSize());
    }

    public byte getInstanceSize() {
        return getSize();
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(event.toArray());
        buffer.put(command.toArray());
        return buffer.array();
    }

    public static LightEventCommand parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] eventBuffer = new byte[LightEventColors.getSize()];
        byte[] commandBuffer = new byte[Command.getSize()];
        buffer.get(eventBuffer, 0, LightEventColors.getSize());
        buffer.get(commandBuffer, LightEventColors.getSize(), Command.getSize());
        LightEventColors event = LightEventColors.parse(eventBuffer);
        Command command = Command.parse(commandBuffer);
        return new LightEventCommand(event, command);
    }
}
