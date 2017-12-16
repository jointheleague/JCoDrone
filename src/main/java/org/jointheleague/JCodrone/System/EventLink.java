package org.jointheleague.JCodrone.System;

public enum EventLink {
    // bluetooth board
    NONE(0x00),
    SYSTEM_RESET(0x01),
    INITIALIZED(0x02),
    SCANNING(0x03),
    SCAN_STOP(0x04),
    FOUND_DRONE_SERVICE(0x05),
    CONNECTING(0x06),
    CONNECTED(0x07),
    CONNECTION_FAILD(0x08),
    CONNECTION_FAILD_NO_DEVICES(0x09),
    CONNECTION_FAILD_NOT_READY(0x0A),
    PAIRING_START(0x0B),
    PARING_SUCCESS(0x0C),
    PARING_FAILD(0x0D),
    BONDING_SUCCESS(0x0E),
    LOOKUP_ATTRIBUTE(0x0F),

    RSSI_POLLING_START(0x10),
    RSSI_POLLING_STOP(0x11),
    DISCOVER_SERVICE(0x12),
    DISCOVER_CHARACTERISTIC(0x13),
    DISCOVER_CHARACTERISTIC_DRONE_DATA(0x14),
    DISCOVER_CHARACTERISTIC_DRONE_CONFIG(0x15),
    DISCOVER_CHARACTERISTIC_UNKNOWN(0x16),
    DISCOVER_CCCD(0x17),
    READY_TO_CONTROL(0x18),
    DISCONNECTING(0x19),
    DISCONNECTED(0x1A),
    GAP_LINK_PARAM_UPDATE(0x1B),   // GAP_LINK_PARAM_UPDATE_EVENT
    RSP_READ_ERROR(0x1C),
    RSP_READ_SUCCESS(0x1D),
    RSP_WRITE_ERROR(0x1E),
    RSP_WRITE_SUCCESS(0x1F),
    SET_NOTIFY(0x20),            // Notify activate
    WRITE(0x21),                // write event

    END_OF_TYPE(0x22);

    private final int event;

    EventLink(int event) {
        this.event = event;
    }

    public int getEvent() {
        return event;
    }
}
