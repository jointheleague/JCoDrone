package org.jointheleague.JCodrone.System;

public enum ModeVehicle {
    // mode for CoDrone
    NONE(0x00),
    FLIGHT_GUARD(0x10),
    FLIGHT_NO_GUARD(0x11),
    FLIGHT_FPV(0x12),
    DRIVE(0x20),
    DRIVE_FPV(0x21),
    TEST(0x30),
    END_OF_TYPE(0x31);

    private final int mode;

    ModeVehicle(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }
}
