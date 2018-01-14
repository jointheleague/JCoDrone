package org.jointheleague.jcodrone.protocol.link;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jointheleague.jcodrone.CoDrone;
import org.jointheleague.jcodrone.Internals;
import org.jointheleague.jcodrone.Link;
import org.jointheleague.jcodrone.Sensors;
import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;
import org.jointheleague.jcodrone.system.ModeLink;
import org.jointheleague.jcodrone.system.ModeLinkBroadcast;

import java.nio.ByteBuffer;

public class LinkState implements Serializable {
    private static Logger log = LogManager.getLogger(LinkState.class);

    private final ModeLink mode;
    private final ModeLinkBroadcast broadcast;

    public LinkState(ModeLink mode, ModeLinkBroadcast broadcast) {
        this.mode = mode;
        this.broadcast = broadcast;
    }

    public static byte getSize() {
        return 2;
    }

    public byte getInstanceSize() {
        return getSize();
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

    @Override
    public void handle(CoDrone coDrone, Link link, Sensors sensors, Internals internals) {
        link.setCurrentMode(this.mode);
        link.setCurrentBroadcastMode(this.broadcast);
        log.debug("Link Mode: {} Broadcast Mode: {} ", this.mode.name(), this.broadcast.name());
    }

    public ModeLinkBroadcast getBroadcastMode() {
        return broadcast;
    }
}
