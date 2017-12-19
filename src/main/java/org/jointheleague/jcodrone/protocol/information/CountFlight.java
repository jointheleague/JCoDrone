package org.jointheleague.jcodrone.protocol.information;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;

public class CountFlight implements Serializable {
    private final long timeFlight;
    private final int countTakeoff;
    private final int countLanding;
    private final int countAccident;

    public CountFlight(long timeFlight, int countTakeoff, int countLanding, int countAccident) {
        this.timeFlight = timeFlight;
        this.countTakeoff = countTakeoff;
        this.countLanding = countLanding;
        this.countAccident = countAccident;
    }

    public static int getSize() {
        return 14;
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.putLong(timeFlight);
        buffer.putShort((short) countTakeoff);
        buffer.putShort((short) countLanding);
        buffer.putShort((short) countAccident);
        return buffer.array();
    }

    public static CountFlight parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        long timeFlight = buffer.getLong();
        int countTakeoff = buffer.getShort();
        int countLanding = buffer.getShort();
        int countAccident = buffer.getShort();
        return new CountFlight(timeFlight, countTakeoff, countLanding, countAccident);
    }
}
