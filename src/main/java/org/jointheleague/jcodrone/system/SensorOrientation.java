package org.jointheleague.jcodrone.system;


import java.util.HashMap;
import java.util.Map;

public enum SensorOrientation {
    NONE((byte) 0x00),
    NORMAL((byte) 0x01),
    REVERSE_START((byte) 0x02),
    REVERSED((byte) 0x03),
    END_OF_TYPE((byte) 0x04);

    private static final Map<Byte, SensorOrientation> BYTE_SENSOR_ORIENTATION_MAP =
            new HashMap<>();

    static {
        for (SensorOrientation orientation : SensorOrientation.values()) {
            BYTE_SENSOR_ORIENTATION_MAP.put(orientation.value(), orientation);
        }
    }

    private final byte orientation;

    SensorOrientation(byte orientation) {
        this.orientation = orientation;
    }

    public byte value() {
        return orientation;
    }

    public static SensorOrientation fromByte(byte b) {
        return BYTE_SENSOR_ORIENTATION_MAP.get(b);
    }
}
