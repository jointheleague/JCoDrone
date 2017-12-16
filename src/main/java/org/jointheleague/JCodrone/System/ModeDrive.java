package org.jointheleague.JCodrone.System;

public enum ModeDrive {
    // for drive mode(drive kit)
    NONE(0x00),
    READY(0x01),  // ready
    START(0x02),  // start
    DRIVE(0x03),  // drive
    STOP(0x04),   // force stop(kill stwich)
    ACCIDENT(0x05),  // accident (change to ready mode automatically)
    ERROR(0x06),  // error
    END_OF_TYPE(0x07);

    private final int mode;

    ModeDrive(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }
}
