package org.jointheleague.jcodrone;

public class DirectControlBuilder {
    private byte roll;
    private byte pitch;
    private byte yaw;
    private byte throttle;

    public DirectControlBuilder setRoll(byte roll) {
        this.roll = roll;
        return this;
    }

    public DirectControlBuilder setPitch(byte pitch) {
        this.pitch = pitch;
        return this;
    }

    public DirectControlBuilder setYaw(byte yaw) {
        this.yaw = yaw;
        return this;
    }

    public DirectControlBuilder setThrottle(byte throttle) {
        this.throttle = throttle;
        return this;
    }

    public DirectControl build() {
        return new DirectControl(roll, pitch, yaw, throttle);
    }
}