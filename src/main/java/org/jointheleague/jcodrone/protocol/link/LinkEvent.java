package org.jointheleague.jcodrone.protocol.link;

import org.jointheleague.jcodrone.CoDrone;
import org.jointheleague.jcodrone.Link;
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

    public static byte getSize() {
        return 2;
    }

    public byte getInstanceSize() {
        return getSize();
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

    public void handle(CoDrone coDrone) {
        Link link = coDrone.getLink();
        switch (event) {
            case SCANNING:
                link.clearDevices();
                link.setDiscoveringDevices(true);
                break;
            case SCAN_STOP:
                link.setDiscoveringDevices(false);
                break;
            case CONNECTED:
                link.setConnected(true);
                break;
            case DISCONNECTED:
                link.setConnected(false);
                break;
        }
    }
}
