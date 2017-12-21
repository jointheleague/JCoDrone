package org.jointheleague.jcodrone.system;

public enum FlightEvent {
    NONE((byte) 0x00),

    TAKEOFF((byte) 0x01),
    FLIP_FRONT((byte) 0x02),   // Reserved
    FLIP_REAR((byte) 0x03),    // Reserved
    FLIP_LEFT((byte) 0x04),    // Reserved
    FLIP_RIGHT((byte) 0x05),   // Reserved
    STOP((byte) 0x06),         // force stop
    LANDING((byte) 0x07),
    TURN_OVER((byte) 0x08),      // upside down
    SHOT((byte) 0x09),         // shoot IR missile
    UNDER_ATTACK((byte) 0x0A), // hit by IR missile
    SQUARE((byte) 0x0B),
    CIRCLE_RIGHT((byte) 0x0C),
    CIRCLE_LEFT((byte) 0x0D),
    ROTATE_180((byte) 0x0E),
    END_OF_TYPE((byte) 0x0F);

    private final byte event;

    FlightEvent(byte event) {
        this.event = event;
    }

    public byte value() {
        return event;
    }
}
