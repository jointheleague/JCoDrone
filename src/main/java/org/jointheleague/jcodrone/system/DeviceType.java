package org.jointheleague.jcodrone.system;

import java.util.HashMap;
import java.util.Map;

public enum DeviceType {
    NONE(0x00),
    DRONE_MAIN(0x01),
    DRONE_SUB(0x02),
    LINK(0x03),
    TESTER(0x04),
    END_OF_TYPE(0x05);

    private static final Map<Integer, DeviceType> INTEGER_DEVICE_TYPE_MAP =
            new HashMap<>();

    static {
        for (DeviceType type : DeviceType.values()) {
            INTEGER_DEVICE_TYPE_MAP.put(type.value(), type);
        }
    }

    private final int type;

    DeviceType(int type) {
        this.type = type;
    }

    public int value() {
        return type;
    }

    public static DeviceType forValue(int deviceType) {
        return INTEGER_DEVICE_TYPE_MAP.get(deviceType);
    }
}
