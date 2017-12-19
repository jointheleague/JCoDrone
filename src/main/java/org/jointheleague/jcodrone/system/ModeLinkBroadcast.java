package org.jointheleague.jcodrone.system;

import java.util.HashMap;
import java.util.Map;

public enum ModeLinkBroadcast {
    NONE((byte) 0x00),

    MUTE((byte) 0x01),  // block data request
    ACTIVE((byte) 0x02),  // data transport request and some auto thing
    PASSIVE((byte) 0x03), // reply just request things - no data transport when status is change, 연결 등의 상황에서는 진행 상황 전송

    END_OF_TYPE((byte) 0x07);

    private static final Map<Byte, ModeLinkBroadcast> byteToModeLinkBroadcast = new HashMap<>();

    static {
        for (ModeLinkBroadcast mode : ModeLinkBroadcast.values()) {
            byteToModeLinkBroadcast.put(mode.value(), mode);
        }
    }

    private final byte mode;

    ModeLinkBroadcast(byte mode) {
        this.mode = mode;
    }

    public byte value() {
        return mode;
    }

    public static ModeLinkBroadcast fromByte(byte b) {
        return byteToModeLinkBroadcast.get(b);
    }
}
