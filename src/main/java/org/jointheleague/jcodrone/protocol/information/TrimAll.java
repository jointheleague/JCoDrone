package org.jointheleague.jcodrone.protocol.information;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;

public class TrimAll implements Serializable {
    private final TrimFlight trimFlight;
    private final TrimDrive trimDrive;

    public TrimAll(TrimFlight trimFlight, TrimDrive trimDrive) {
        this.trimFlight = trimFlight;
        this.trimDrive = trimDrive;
    }

    public static int getSize() {
        return TrimFlight.getSize() + TrimDrive.getSize();
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
        buffer.get(driveBuffer, TrimFlight.getSize(), TrimDrive.getSize());
        TrimFlight trimFlight = TrimFlight.parse(flightBuffer);
        TrimDrive trimDrive = TrimDrive.parse(driveBuffer);
        return new TrimAll(trimFlight, trimDrive);

    }
}
