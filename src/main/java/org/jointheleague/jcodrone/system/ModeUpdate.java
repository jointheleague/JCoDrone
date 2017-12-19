package org.jointheleague.jcodrone.system;

import java.util.HashMap;
import java.util.Map;

public enum ModeUpdate {
    NONE((byte) 0x00),

    READY((byte) 0x01),    // ready for modeupdate
    UPDATE((byte) 0x02),   // updating
    COMPLPETE((byte) 0x03),// finish update
    FAILD((byte) 0x04),    // faild (ex : update finish but body's CRC16), is not match)
    END_OF_TYPE((byte) 0x05),;

    private static final Map<Byte, ModeUpdate> BYTE_MODE_UPDATE_MAP =
            new HashMap<>();

    static {
        for (ModeUpdate mode : ModeUpdate.values()) {
            BYTE_MODE_UPDATE_MAP.put(mode.value(), mode);
        }
    }

    private final byte mode;

    ModeUpdate(byte mode) {
        this.mode = mode;
    }

    public byte value() {
        return mode;
    }


    public static ModeUpdate forValue(byte b) {
        return BYTE_MODE_UPDATE_MAP.get(b);
    }
}
