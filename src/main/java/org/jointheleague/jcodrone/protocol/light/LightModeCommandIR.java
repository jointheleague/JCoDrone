package org.jointheleague.jcodrone.protocol.light;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;
import org.jointheleague.jcodrone.protocol.common.Command;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LightModeCommandIR implements Serializable {
    private final LightModeColors mode;
    private final Command command;
    private final int IR;

    public LightModeCommandIR(LightModeColors mode, Command command, int ir) {
        this.mode = mode;
        this.command = command;
        IR = ir;
    }

    public static byte getSize() {
        return (byte) (LightModeColors.getSize() + Command.getSize() + 4);
    }

    public byte getInstanceSize() {
        return getSize();
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(mode.toArray());
        buffer.put(command.toArray());
        buffer.putInt(IR);
        return buffer.array();
    }

    public static LightModeCommandIR parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] modeBuffer = new byte[LightModeColors.getSize()];
        byte[] commandBuffer = new byte[Command.getSize()];
        buffer.get(modeBuffer, 0, LightModeColors.getSize());
        buffer.get(commandBuffer, 0, Command.getSize());
        LightModeColors mode = LightModeColors.parse(modeBuffer);
        Command command = Command.parse(commandBuffer);
        int IR = buffer.getInt(LightModeColors.getSize() + Command.getSize());
        return new LightModeCommandIR(mode, command, IR);
    }
}
