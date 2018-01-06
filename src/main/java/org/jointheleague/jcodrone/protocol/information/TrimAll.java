package org.jointheleague.jcodrone.protocol.information;

import org.jointheleague.jcodrone.CoDrone;
import org.jointheleague.jcodrone.Internals;
import org.jointheleague.jcodrone.Link;
import org.jointheleague.jcodrone.Sensors;
import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;

public class TrimAll implements Serializable {
    private TrimFlight trimFlight;
    private TrimDrive trimDrive;

    public TrimAll(TrimFlight trimFlight, TrimDrive trimDrive) {
        this.trimFlight = trimFlight;
        this.trimDrive = trimDrive;
    }

    public static byte getSize() {
        return (byte) (TrimFlight.getSize() + TrimDrive.getSize());
    }

    public byte getInstanceSize() {
        return getSize();
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.put(trimFlight.toArray());
        buffer.put(trimDrive.toArray());
        return buffer.array();
    }

    public static TrimAll parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        byte[] flightBuffer = new byte[TrimFlight.getSize()];
        byte[] driveBuffer = new byte[TrimDrive.getSize()];
        buffer.get(flightBuffer, 0, TrimFlight.getSize());
        buffer.get(driveBuffer, 0, TrimDrive.getSize());
        TrimFlight trimFlight = TrimFlight.parse(flightBuffer);
        TrimDrive trimDrive = TrimDrive.parse(driveBuffer);
        return new TrimAll(trimFlight, trimDrive);

    }

    public TrimDrive getTrimDrive() {
        return trimDrive;
    }

    public void setTrimDrive(TrimDrive trimDrive) {
        this.trimDrive = trimDrive;
    }

    public TrimFlight getTrimFlight() {
        return trimFlight;
    }

    public void setTrimFlight(TrimFlight trimFlight) {
        this.trimFlight = trimFlight;
    }

    @Override
    public void handle(CoDrone coDrone, Link link, Sensors sensors, Internals internals) {
        internals.setTrimAll(this);
    }
}
