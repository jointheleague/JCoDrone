package org.jointheleague.jcodrone.protocol.information;

import org.jointheleague.jcodrone.CoDrone;
import org.jointheleague.jcodrone.Internals;
import org.jointheleague.jcodrone.Link;
import org.jointheleague.jcodrone.Sensors;
import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;

public class CountDrive implements Serializable {
    private final long timeDrive;
    private final int countAccident;

    public CountDrive(long timeDrive, int countAccident) {
        this.timeDrive = timeDrive;
        this.countAccident = countAccident;
    }

    public static byte getSize() {
        return 10;
    }

    public byte getInstanceSize() {
        return getSize();
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.putLong(timeDrive);
        buffer.putShort((short) countAccident);
        return buffer.array();
    }

    public static CountDrive parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        long timeDrive = buffer.getLong();
        int countAccident = buffer.getShort();
        return new CountDrive(timeDrive, countAccident);
    }

    @Override
    public void handle(CoDrone coDrone, Link link, Sensors sensors, Internals internals) {
        internals.setCountDrive(this);
    }
}
