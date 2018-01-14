package org.jointheleague.jcodrone.protocol.link;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jointheleague.jcodrone.CoDrone;
import org.jointheleague.jcodrone.Internals;
import org.jointheleague.jcodrone.Link;
import org.jointheleague.jcodrone.Sensors;
import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LinkDiscoveredDevice implements Serializable {
    static Logger log = LogManager.getLogger(LinkDiscoveredDevice.class);

    public static final int NAME_LENGTH = 20;
    public static final int ADDRESS_LENGTH = 6;
    private final byte index;
    private final byte[] address;
    private final char[] name;
    private final byte rssi;

    public LinkDiscoveredDevice(byte index, byte[] address, String name, byte rssi) {
        this.index = index;
        this.address = Arrays.copyOf(address, ADDRESS_LENGTH);
        this.name = Arrays.copyOf(name.toCharArray(), NAME_LENGTH);
        this.rssi = rssi;
    }

    public static byte getSize() {
        return 28;
    }

    public byte getInstanceSize() {
        return getSize();
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.put(index);
        buffer.put(address);
        ByteBuffer nameBuffer = ByteBuffer.allocate(NAME_LENGTH);
        CharBuffer cb = nameBuffer.asCharBuffer();
        cb.put(name);
        buffer.put(nameBuffer);
        buffer.put(rssi);
        return buffer.array();
    }

    public static LinkDiscoveredDevice parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        byte index = buffer.get();
        byte[] address = new byte[ADDRESS_LENGTH];
        buffer.get(address, 0, ADDRESS_LENGTH);
        byte[] nameBuffer = new byte[NAME_LENGTH];
        buffer.get(nameBuffer, 0, NAME_LENGTH);
        byte rssi = buffer.get();
        return new LinkDiscoveredDevice(index, address, new String(nameBuffer), rssi);
    }

    public byte getRssi() {
        return rssi;
    }

    public byte getIndex() {
        return index;
    }

    public String getName() {
        return String.valueOf(name);
    }

    public static IntStream intStream(byte[] array) {
        return IntStream.range(0, array.length).map(idx -> array[idx]);
    }

    @Override
    public void handle(CoDrone coDrone, Link link, Sensors sensors, Internals internals) {
        log.debug("Device {} [{}] RSSI: {}", this.getNameString(), this.getFormattedAddress(), this.rssi);
        link.addDevice(this);
    }

    public String getNameString() {
        return new String(name).trim();
    }

    public String getFormattedAddress() {
        return intStream(address).mapToObj(x -> Integer.toHexString(0x0FF & x)).collect(Collectors.joining(":"));
    }

    public byte[] getAddress() {
        return address;
    }
}
