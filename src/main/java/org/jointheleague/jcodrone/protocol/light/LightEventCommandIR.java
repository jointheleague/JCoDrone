package org.jointheleague.jcodrone.protocol.light;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;
import org.jointheleague.jcodrone.protocol.common.Command;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LightEventCommandIR implements Serializable {
    private final LightEvent event;
    private final Command command;
    private final int IR;

    public LightEventCommandIR(LightEvent event, Command command, int ir) {
        this.event = event;
        this.command = command;
        IR = ir;
    }

    public static int getSize() {
        return LightEvent.getSize() + Command.getSize() + 4;
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(event.toArray());
        buffer.put(command.toArray());
        buffer.putInt(IR);
        return buffer.array();
    }

    public static LightEventCommandIR parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] eventBuffer = new byte[LightEvent.getSize()];
        byte[] commandBuffer = new byte[Command.getSize()];
        buffer.get(eventBuffer, 0, LightEvent.getSize());
        buffer.get(commandBuffer, LightEvent.getSize(), Command.getSize());
        LightEvent event = LightEvent.parse(eventBuffer);
        Command command = Command.parse(commandBuffer);
        int IR = buffer.getInt(LightEvent.getSize() + Command.getSize());
        return new LightEventCommandIR(event, command, IR);
    }
}
