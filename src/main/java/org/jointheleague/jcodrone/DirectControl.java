package org.jointheleague.jcodrone;

import java.util.Optional;

public class DirectControl {
    private byte roll = 0;
    private byte pitch = 0;
    private byte yaw = 0;
    private byte throttle = 0;

    public DirectControl(byte roll, byte pitch, byte yaw, byte throttle) {
        if (!inRange(roll) || !inRange(pitch) || !inRange(yaw) || !inRange(throttle))
            throw new IllegalArgumentException();
        this.roll = roll;
        this.pitch = pitch;
        this.yaw = yaw;
        this.throttle = throttle;
    }

    public void setRoll(byte roll) {
        if (!inRange(roll)) throw new IllegalArgumentException();
        this.roll = roll;
    }

    public void setPitch(byte pitch) {
        if (!inRange(pitch)) throw new IllegalArgumentException();
        this.pitch = pitch;
    }

    public void setYaw(byte yaw) {
        if (!inRange(yaw)) throw new IllegalArgumentException();
        this.yaw = yaw;
    }

    public void setThrottle(byte throttle) {
        if (!inRange(throttle)) throw new IllegalArgumentException();
        this.throttle = throttle;
    }

    public byte getRoll() {
        return roll;
    }

    public byte getPitch() {
        return pitch;
    }

    public byte getYaw() {
        return yaw;
    }

    public byte getThrottle() {
        return throttle;
    }

    boolean inRange(byte value) {
        return (-100 <= value && value <= 100);
    }
}
