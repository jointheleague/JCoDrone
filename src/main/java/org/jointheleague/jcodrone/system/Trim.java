package org.jointheleague.jcodrone.system;

public enum Trim {
    // increase or decrease will be 1
    NONE(0x00),
    ROLL_INCREASE(0x01),
    ROLL_DECREASE(0x02),
    PITCH_INCREASE(0x03),
    PITCH_DECREASE(0x04),
    YAW_INCREASE(0x05),
    YAW_DECREASE(0x06),
    THROTTLE_INCREASE(0x07),
    THROTTLE_DECREASE(0x08),
    RESET(0x09),    // reset all trim

    END_OF_TYPE(0x0A);

    private final int channel;

    Trim(int channel) {
        this.channel = channel;
    }

    public int getChannel() {
        return channel;
    }
}
