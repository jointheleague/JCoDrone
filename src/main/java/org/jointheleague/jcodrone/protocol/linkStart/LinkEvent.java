package org.jointheleague.jcodrone.protocol.linkStart;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;
import org.jointheleague.jcodrone.system.EventLink;

import java.nio.ByteBuffer;

public class LinkEvent implements Serializable {
    private final EventLink event;
    private final byte result;

    public LinkEvent(EventLink event, byte result) {
        this.event = event;
        this.result = result;
    }

    public static int getSize() {
        return 2;
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.put(event.value());
        buffer.put(result);
        return buffer.array();
    }

    public static LinkEvent parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        EventLink event = EventLink.fromByte(buffer.get());
        byte result = buffer.get();
        return new LinkEvent(event, result);
    }

}
