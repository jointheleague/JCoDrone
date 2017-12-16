package org.jointheleague.JCodrone.System;

public enum Direction {
    NONE(0x00),
    LEFT(0x01),
    FRONT(0x02),
    RIGHT(0x03),
    REAR(0x04),
    TOP(0x05),
    BOTTOM(0x06),
    END_OF_TYPE(0x07);

    private final int direction;

    Direction(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }
}
