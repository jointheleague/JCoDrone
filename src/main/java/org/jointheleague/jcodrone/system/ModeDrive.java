package org.jointheleague.jcodrone.system;

import java.util.HashMap;
import java.util.Map;

public enum ModeDrive {
    // for drive mode(drive kit)
    NONE((byte) 0x00),
    READY((byte) 0x01),  // ready
    START((byte) 0x02),  // start
    DRIVE((byte) 0x03),  // drive
    STOP((byte) 0x04),   // force stop(kill stwich)
    ACCIDENT((byte) 0x05),  // accident (change to ready mode automatically)
    ERROR((byte) 0x06),  // error
    END_OF_TYPE((byte) 0x07);

    private static final Map<Byte, ModeDrive> BYTE_MODE_DRIVE_MAP =
            new HashMap<>();

    static {
        for (ModeDrive mode : ModeDrive.values()) {
            BYTE_MODE_DRIVE_MAP.put(mode.value(), mode);
        }
    }

    private final byte mode;

    ModeDrive(byte mode) {
        this.mode = mode;
    }

    public byte value() {
        return mode;
    }

    public static ModeDrive fromByte(byte b) {
        return BYTE_MODE_DRIVE_MAP.get(b);
    }
}
