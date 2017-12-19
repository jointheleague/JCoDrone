package org.jointheleague.jcodrone.system;

public enum DriveEvent {
    NONE(0x00),
    STOP(0x01),         // force stop
    SHOT(0x02),         // shoot IR missile
    UNDER_ATTACK(0x03), // be hitted by IR missile
    END_OF_TYPE(0x04);

    private final int event;

    DriveEvent(int event) {
        this.event = event;
    }

    public int getEvent() {
        return event;
    }
}
