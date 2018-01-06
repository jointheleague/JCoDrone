package org.jointheleague.jcodrone;

import static org.jointheleague.jcodrone.protocol.Validator.isValidControl;

public class DirectControl {
    private int roll;
    private int pitch;
    private int yaw;
    private int throttle;

    @SuppressWarnings("WeakerAccess")
    public DirectControl(int roll, int pitch, int yaw, int throttle) {
        if (!(isValidControl(roll) && isValidControl(pitch) && isValidControl(yaw) && isValidControl(throttle))) {
            throw new IllegalArgumentException();
        }
        this.roll = roll;
        this.pitch = pitch;
        this.yaw = yaw;
        this.throttle = throttle;
    }

    public int getRoll() {
        return roll;
    }

    @SuppressWarnings("unused")
    public void setRoll(byte roll) {
        if (!isValidControl(roll)) {
            throw new IllegalArgumentException();
        }
        this.roll = roll;
    }

    public int getPitch() {
        return pitch;
    }

    @SuppressWarnings("unused")
    public void setPitch(byte pitch) {
        if (!isValidControl(pitch)) {
            throw new IllegalArgumentException();
        }
        this.pitch = pitch;
    }

    public int getYaw() {
        return yaw;
    }

    @SuppressWarnings("unused")
    public void setYaw(byte yaw) {
        if (!isValidControl(yaw)) {
            throw new IllegalArgumentException();
        }
        this.yaw = yaw;
    }

    public int getThrottle() {
        return throttle;
    }

    @SuppressWarnings("unused")
    public void setThrottle(byte throttle) {
        if (!isValidControl(throttle)) {
            throw new IllegalArgumentException();
        }
        this.throttle = throttle;
    }
}
