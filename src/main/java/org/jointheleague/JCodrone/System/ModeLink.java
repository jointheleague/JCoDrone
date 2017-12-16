package org.jointheleague.JCodrone.System;

public enum ModeLink {
    NONE(0x00),

    BOOT(0x01),             // boot
    READY(0x02),            // ready(before connect)
    CONNECTING(0x03),       // connecting
    CONNECTED(0x04),        // finish connection(normal connection)
    DISCONNECTING(0x05),    // disconnecting
    READY_TO_RESET(0x06),   // reset ready(reset after 1sec)

    END_OF_TYPE(0x07);

    private final int mode;

    ModeLink(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }
}
