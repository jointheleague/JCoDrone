package org.jointheleague.jcodrone.system;

import java.util.HashMap;
import java.util.Map;

public enum ModeVehicle {
    // mode for CoDrone
    NONE((byte) 0x00),
    FLIGHT_GUARD((byte) 0x10),
    FLIGHT_NO_GUARD((byte) 0x11),
    FLIGHT_FPV((byte) 0x12),
    DRIVE((byte) 0x20),
    DRIVE_FPV((byte) 0x21),
    TEST((byte) 0x30),
    END_OF_TYPE((byte) 0x31);

    private static final Map<Byte, ModeVehicle> BYTE_MODE_VEHICLE_MAP =
            new HashMap<>();

    static {
        for (ModeVehicle mode : ModeVehicle.values()) {
            BYTE_MODE_VEHICLE_MAP.put(mode.value(), mode);
        }
    }

    private final byte mode;

    ModeVehicle(byte mode) {
        this.mode = mode;
    }

    public byte value() {
        return mode;
    }

    public static ModeVehicle fromByte(byte b) {
        return BYTE_MODE_VEHICLE_MAP.get(b);
    }
}
