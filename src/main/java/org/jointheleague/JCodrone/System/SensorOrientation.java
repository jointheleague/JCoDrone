package org.jointheleague.JCodrone.System;

public enum SensorOrientation {
    NONE(0x00),
    NORMAL(0x01),
    REVERSE_START(0x02),
    REVERSED(0x03),
    END_OF_TYPE(0x04);

    private final int orientation;

    SensorOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getOrientation() {
        return orientation;
    }
}
