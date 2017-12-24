package org.jointheleague.jcodrone.protocol.information;

import org.jointheleague.jcodrone.CoDrone;
import org.jointheleague.jcodrone.Internals;
import org.jointheleague.jcodrone.Link;
import org.jointheleague.jcodrone.Sensors;
import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;

public class Battery implements Serializable {
    private final short adjustGradient;
    private final short adjustYIntercept;
    private final short gradient;
    private final short yIntercept;
    private final boolean flagBatteryCalibration;
    private final int batteryRaw;
    private final byte batteryPercent;
    private final short voltage;

    public Battery(short adjustGradient, short adjustYIntercept, short gradient, short yIntercept, boolean flagBatteryCalibration, int batteryRaw, byte batteryPercent, short voltage) {
        this.adjustGradient = adjustGradient;
        this.adjustYIntercept = adjustYIntercept;
        this.gradient = gradient;
        this.yIntercept = yIntercept;
        this.flagBatteryCalibration = flagBatteryCalibration;
        this.batteryRaw = batteryRaw;
        this.batteryPercent = batteryPercent;
        this.voltage = voltage;
    }

    public static byte getSize() {
        return 16;
    }

    public byte getInstanceSize() {
        return getSize();
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.putShort(adjustGradient);
        buffer.putShort(adjustYIntercept);
        buffer.putShort(gradient);
        buffer.putShort(yIntercept);
        buffer.put(flagBatteryCalibration ? (byte) 1 : (byte) 0);
        buffer.putInt(batteryRaw);
        buffer.put(batteryPercent);
        buffer.putShort(voltage);
        return buffer.array();
    }

    public static Battery parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        short adjustGradient = buffer.getShort();
        short adjustYIntercept = buffer.getShort();
        short gradient = buffer.getShort();
        short yIntercept = buffer.getShort();
        boolean flagBatteryCalibration = buffer.get() != 0;
        int batteryRaw = buffer.getInt();
        byte batteryPercent = buffer.get();
        short voltage = buffer.getShort();
        return new Battery(adjustGradient, adjustYIntercept, gradient, yIntercept, flagBatteryCalibration, batteryRaw, batteryPercent, voltage);
    }

    @Override
    public void handle(CoDrone coDrone, Link link, Sensors sensors, Internals internals) {
        internals.setBattery(this);
    }
}
