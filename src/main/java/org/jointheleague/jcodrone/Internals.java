package org.jointheleague.jcodrone;

import org.jointheleague.jcodrone.protocol.information.*;

public class Internals {
    private final CoDrone coDrone;
    private Battery battery;
    private Button button;
    private CountDrive countDrive;
    private CountFlight countFlight;
    private IRMessage irMessage;
    private Motor motor;
    private State state;
    private TrimAll trimAll;

    public Internals(CoDrone coDrone) {
        this.coDrone = coDrone;
    }

    public Battery getBattery() {
        return battery;
    }

    public void setBattery(Battery battery) {
        this.battery = battery;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public CountDrive getCountDrive() {
        return countDrive;
    }

    public void setCountDrive(CountDrive countDrive) {
        this.countDrive = countDrive;
    }

    public CountFlight getCountFlight() {
        return countFlight;
    }

    public void setCountFlight(CountFlight countFlight) {
        this.countFlight = countFlight;
    }

    public IRMessage getIrMessage() {
        return irMessage;
    }

    public void setIrMessage(IRMessage irMessage) {
        this.irMessage = irMessage;
    }

    public Motor getMotor() {
        return motor;
    }

    public void setMotor(Motor motor) {
        this.motor = motor;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public TrimAll getTrimAll() {
        return trimAll;
    }

    public void setTrimAll(TrimAll trimAll) {
        this.trimAll = trimAll;
    }

    public TrimDrive getTrimDrive() {
        return trimAll.getTrimDrive();
    }

    public void setTrimDrive(TrimDrive trimDrive) {
        if (trimAll == null) {
            trimAll = new TrimAll(null, trimDrive);
        } else {
            trimAll.setTrimDrive(trimDrive);
        }
    }

    public TrimFlight getTrimFlight() {
        return trimAll.getTrimFlight();
    }

    public void setTrimFlight(TrimFlight trimFlight) {
        if (trimAll == null) {
            trimAll = new TrimAll(trimFlight, null);
        } else {
            trimAll.setTrimFlight(trimFlight);
        }
    }
}
