package org.jointheleague.JCodrone.System;

public enum ModeFlight {
    NONE(0x00),
    READY(0x01),    // ready
    TAKE_OFF(0x02), // take off (change to flight mode automatically)
    FLIGHT(0x03),   // flight
    CLIP(0x04),     // not support
    STOP(0x05),     // force stop(kill switch)
    LANDING(0x06),  // Landing
    REVERSE(0x07),  // upside down
    ACCIDENT(0x08), // accident (change to ready mode automatically)
    ERROR(0x09),    // error
    END_OF_TYPE(0x0A);

    private final int mode;

    ModeFlight(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }
}
