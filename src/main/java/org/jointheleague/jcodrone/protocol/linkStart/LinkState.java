package org.jointheleague.jcodrone.protocol.linkStart;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;
import org.jointheleague.jcodrone.system.ModeLink;
import org.jointheleague.jcodrone.system.ModeLinkBroadcast;

import java.nio.ByteBuffer;

public class LinkState implements Serializable {

    private final ModeLink mode;
    private final ModeLinkBroadcast broadcast;

    public LinkState(ModeLink mode, ModeLinkBroadcast broadcast) {
        this.mode = mode;
        this.broadcast = broadcast;
    }

    public static int getSize() {
        return 2;
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.put(mode.value());
        buffer.put(broadcast.value());
        return buffer.array();
    }

    public static LinkState parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        ModeLink mode = ModeLink.fromByte(buffer.get());
        ModeLinkBroadcast broadcast = ModeLinkBroadcast.fromByte(buffer.get());
        return new LinkState(mode, broadcast);
    }
}
