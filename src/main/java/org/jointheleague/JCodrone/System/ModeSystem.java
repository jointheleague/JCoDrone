package org.jointheleague.JCodrone.System;

public enum ModeSystem {
    NONE(0x00),

    BOOT(0x01),             // boot
    WAIT(0x02),             // wait for connection
    READY(0x03),            // ready
    RUNNING(0x04),          // main code run
    UPDATE(0x05),           // firmware update
    UPDATE_COMPLETE(0x06),  // finish firmware update
    ERROR(0x07),            // firmware update error
    END_OF_TYPE(0x08);

    private final int mode;

    ModeSystem(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }
}
