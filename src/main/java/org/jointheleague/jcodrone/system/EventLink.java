package org.jointheleague.jcodrone.system;

import java.util.HashMap;
import java.util.Map;

public enum EventLink {
    // bluetooth board
    NONE((byte) 0x00),
    SYSTEM_RESET((byte) 0x01),
    INITIALIZED((byte) 0x02),
    SCANNING((byte) 0x03),
    SCAN_STOP((byte) 0x04),
    FOUND_DRONE_SERVICE((byte) 0x05),
    CONNECTING((byte) 0x06),
    CONNECTED((byte) 0x07),
    CONNECTION_FAILD((byte) 0x08),
    CONNECTION_FAILD_NO_DEVICES((byte) 0x09),
    CONNECTION_FAILD_NOT_READY((byte) 0x0A),
    PAIRING_START((byte) 0x0B),
    PARING_SUCCESS((byte) 0x0C),
    PARING_FAILD((byte) 0x0D),
    BONDING_SUCCESS((byte) 0x0E),
    LOOKUP_ATTRIBUTE((byte) 0x0F),

    RSSI_POLLING_START((byte) 0x10),
    RSSI_POLLING_STOP((byte) 0x11),
    DISCOVER_SERVICE((byte) 0x12),
    DISCOVER_CHARACTERISTIC((byte) 0x13),
    DISCOVER_CHARACTERISTIC_DRONE_DATA((byte) 0x14),
    DISCOVER_CHARACTERISTIC_DRONE_CONFIG((byte) 0x15),
    DISCOVER_CHARACTERISTIC_UNKNOWN((byte) 0x16),
    DISCOVER_CCCD((byte) 0x17),
    READY_TO_CONTROL((byte) 0x18),
    DISCONNECTING((byte) 0x19),
    DISCONNECTED((byte) 0x1A),
    GAP_LINK_PARAM_UPDATE((byte) 0x1B),   // GAP_LINK_PARAM_UPDATE_EVENT
    RSP_READ_ERROR((byte) 0x1C),
    RSP_READ_SUCCESS((byte) 0x1D),
    RSP_WRITE_ERROR((byte) 0x1E),
    RSP_WRITE_SUCCESS((byte) 0x1F),
    SET_NOTIFY((byte) 0x20),            // Notify activate
    WRITE((byte) 0x21),                // write event

    END_OF_TYPE((byte) 0x22);

    private static final Map<Byte, EventLink> byteToEventLink = new HashMap<>();

    static {
        for (EventLink event : EventLink.values()) {
            byteToEventLink.put(event.value(), event);
        }
    }

    private final byte event;

    EventLink(byte event) {
        this.event = event;
    }

    public byte value() {
        return event;
    }

    public static EventLink fromByte(byte b) {
        return byteToEventLink.get(b);
    }

}
