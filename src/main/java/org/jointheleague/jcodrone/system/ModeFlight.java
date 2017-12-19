package org.jointheleague.jcodrone.system;

import java.util.HashMap;
import java.util.Map;

public enum ModeFlight {
    NONE((byte) 0x00),
    READY((byte) 0x01),    // ready
    TAKE_OFF((byte) 0x02), // take off (change to flight mode automatically)
    FLIGHT((byte) 0x03),   // flight
    CLIP((byte) 0x04),     // not support
    STOP((byte) 0x05),     // force stop(kill switch)
    LANDING((byte) 0x06),  // Landing
    REVERSE((byte) 0x07),  // upside down
    ACCIDENT((byte) 0x08), // accident (change to ready mode automatically)
    ERROR((byte) 0x09),    // error
    END_OF_TYPE((byte) 0x0A);

    private static final Map<Byte, ModeFlight> BYTE_MODE_FLIGHT_MAP =
            new HashMap<>();

    static {
        for (ModeFlight mode : ModeFlight.values()) {
            BYTE_MODE_FLIGHT_MAP.put(mode.value(), mode);
        }
    }

    private final byte mode;

    ModeFlight(byte mode) {
        this.mode = mode;
    }

    public byte value() {
        return mode;
    }

    public static ModeFlight fromByte(byte b) {
        return BYTE_MODE_FLIGHT_MAP.get(b);
    }
}
