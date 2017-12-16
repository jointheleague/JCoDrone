package org.jointheleague.JCodrone.System;

public enum FlightEvent {
    NONE(0x00),

    TAKE_OFF(0x01),
    FLIP_FRONT(0x02),   // Reserved
    FLIP_REAR(0x03),    // Reserved
    FLIP_LEFT(0x04),    // Reserved
    FLIP_RIGHT(0x05),   // Reserved
    STOP(0x06),         // force stop
    LANDING(0x07),
    REVERSE(0x08),      // upside down
    SHOT(0x09),         // shoot IR missile
    UNDER_ATTACK(0x0A), // be hitted by IR missile
    END_OF_TYPE(0x0B);

    private final int event;

    FlightEvent(int event) {
        this.event = event;
    }

    public int getEvent() {
        return event;
    }
}
