package org.jointheleague.jcodrone;

import org.jointheleague.jcodrone.protocol.information.*;

public class Sensors {
    private final CoDrone coDrone;
    private Attitude attitude;
    private GyroBias gyroBias;
    private ImageFlow imageFlow;
    private IMU imu;
    private Pressure pressure;
    private Range range;
    private Temperature temperature;

    public Sensors(CoDrone coDrone) {
        this.coDrone = coDrone;
    }

    public Attitude getAttitude() {
        return attitude;
    }

    public void setAttitude(Attitude attitude) {
        this.attitude = attitude;
    }

    public GyroBias getGyroBias() {
        return gyroBias;
    }

    public void setGyroBias(GyroBias gyroBias) {
        this.gyroBias = gyroBias;
    }

    public ImageFlow getImageFlow() {
        return imageFlow;
    }

    public void setImageFlow(ImageFlow imageFlow) {
        this.imageFlow = imageFlow;
    }

    public IMU getImu() {
        return imu;
    }

    public void setImu(IMU imu) {
        this.imu = imu;
    }

    public Pressure getPressure() {
        return pressure;
    }

    public void setPressure(Pressure pressure) {
        this.pressure = pressure;
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }
}
