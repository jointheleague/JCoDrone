package org.jointheleague.jcodrone.system;

import java.util.HashMap;
import java.util.Map;

public enum Headless {
    NONE((byte) 0x00),
    HEADLESS((byte) 0x01), // Headless
    NORMAL((byte) 0x02),   // Normal
    END_OF_TYPE((byte) 0x03);

    private static final Map<Byte, Headless> BYTE_HEADLESS_MAP =
            new HashMap<>();

    static {
        for (Headless headless : Headless.values()) {
            BYTE_HEADLESS_MAP.put(headless.value(), headless);
        }
    }

    private final byte headless;

    Headless(byte headless) {
        this.headless = headless;
    }

    public byte value() {
        return headless;
    }

    public static Headless fromByte(byte b) {
        return BYTE_HEADLESS_MAP.get(b);
    }
}
