package org.jointheleague.jcodrone.system;

import java.util.HashMap;
import java.util.Map;

public enum ModeSystem {
    NONE((byte) 0x00),

    BOOT((byte) 0x01),             // boot
    WAIT((byte) 0x02),             // wait for connection
    READY((byte) 0x03),            // ready
    RUNNING((byte) 0x04),          // main code run
    UPDATE((byte) 0x05),           // firmware update
    UPDATE_COMPLETE((byte) 0x06),  // finish firmware update
    ERROR((byte) 0x07),            // firmware update error
    END_OF_TYPE((byte) 0x08);

    private static final Map<Byte, ModeSystem> BYTE_MODE_SYSTEM_MAP =
            new HashMap<>();

    static {
        for (ModeSystem mode : ModeSystem.values()) {
            BYTE_MODE_SYSTEM_MAP.put(mode.value(), mode);
        }
    }

    private final byte mode;

    ModeSystem(byte mode) {
        this.mode = mode;
    }

    public byte value() {
        return mode;
    }

    public static ModeSystem fromByte(byte b) {
        return BYTE_MODE_SYSTEM_MAP.get(b);
    }
}
