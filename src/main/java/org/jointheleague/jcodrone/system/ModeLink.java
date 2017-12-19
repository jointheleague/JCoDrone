package org.jointheleague.jcodrone.system;

import java.util.HashMap;
import java.util.Map;

public enum ModeLink {
    NONE((byte) 0x00),

    BOOT((byte) 0x01),             // boot
    READY((byte) 0x02),            // ready(before connect)
    CONNECTING((byte) 0x03),       // connecting
    CONNECTED((byte) 0x04),        // finish connection(normal connection)
    DISCONNECTING((byte) 0x05),    // disconnecting
    READY_TO_RESET((byte) 0x06),   // reset ready(reset after 1sec)

    END_OF_TYPE((byte) 0x07);

    private static final Map<Byte, ModeLink> byteToModeLink = new HashMap<>();

    static {
        for (ModeLink mode : ModeLink.values()) {
            byteToModeLink.put(mode.value(), mode);
        }
    }

    private final byte mode;

    ModeLink(byte mode) {
        this.mode = mode;
    }

    public byte value() {
        return mode;
    }

    public static ModeLink fromByte(byte b) {
        return byteToModeLink.get(b);
    }
}
