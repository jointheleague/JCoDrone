package org.jointheleague.jcodrone.protocol.light;

import java.util.HashMap;
import java.util.Map;

public enum LightModeDrone {
    NONE((byte) 0x00),

    EYE_NONE((byte) 0x10),
    EYE_HOLD((byte) 0x11),
    EYE_MIX((byte) 0x12),
    EYE_FLICKER((byte) 0x13),
    EYE_FLICKER_DOUBLE((byte) 0x14),
    EYE_DIMMING((byte) 0x15),

    ARM_NONE((byte) 0x40),
    ARM_HOLD((byte) 0x41),
    ARM_MIX((byte) 0x42),
    ARM_FLICKER((byte) 0x43),
    ARM_FLICKER_DOUBLE((byte) 0x44),
    ARM_DIMMING((byte) 0x45),
    ARM_FLOW((byte) 0x46),
    ARM_FLOW_REVERSE((byte) 0x47),

    END_OF_TYPE((byte) 0x48);

    private static final Map<Byte, LightModeDrone> BYTE_LIGHT_MODE_DRONE_MAP =
            new HashMap<>();

    static {
        for (LightModeDrone mode : LightModeDrone.values()) {
            BYTE_LIGHT_MODE_DRONE_MAP.put(mode.value(), mode);
        }
    }

    private final byte mode;

    LightModeDrone(byte mode) {
        this.mode = mode;
    }

    public byte value() {
        return mode;
    }

    public static LightModeDrone fromByte(byte b) {
        return BYTE_LIGHT_MODE_DRONE_MAP.get(b);
    }
}
