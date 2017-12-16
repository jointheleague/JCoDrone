package org.jointheleague.JCodrone.System;

public enum DeviceType {
    NONE(0x00),
    DRONE_MAIN(0x01),
    DRONE_SUB(0x02),
    LINK(0x03),
    TESTER(0x04),
    END_OF_TYPE(0x05);

    private final int type;

    DeviceType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
