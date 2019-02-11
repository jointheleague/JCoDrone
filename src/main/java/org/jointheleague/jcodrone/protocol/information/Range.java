package org.jointheleague.jcodrone.protocol.information;

import org.jointheleague.jcodrone.CoDrone;
import org.jointheleague.jcodrone.Internals;
import org.jointheleague.jcodrone.Link;
import org.jointheleague.jcodrone.Sensors;
import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;

public class Range implements Serializable {
    private final short left;
    private final short front;
    private final short right;
    private final short rear;
    private final short top;
    private final short bottom;

    public Range(short left, short front, short right, short rear, short top, short bottom) {
        this.left = left;
        this.front = front;
        this.right = right;
        this.rear = rear;
        this.top = top;
        this.bottom = bottom;
    }

    public static byte getSize() {
        return 12;
    }

    public byte getInstanceSize() {
        return getSize();
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.putShort(left);
        buffer.putShort(front);
        buffer.putShort(right);
        buffer.putShort(rear);
        buffer.putShort(top);
        buffer.putShort(bottom);
        return buffer.array();
    }

    public static Range parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        short left = buffer.getShort();
        short front = buffer.getShort();
        short right = buffer.getShort();
        short rear = buffer.getShort();
        short top = buffer.getShort();
        short bottom = buffer.getShort();
        return new Range(left, front, right, rear, top, bottom);
    }

    @Override
    public void handle(CoDrone coDrone, Link link, Sensors sensors, Internals internals) {
        sensors.setRange(this);
    }

    public short getLeft() {
        return left;
    }

    public short getFront() {
        return front;
    }

    public short getRight() {
        return right;
    }

    public short getRear() {
        return rear;
    }

    public short getTop() {
        return top;
    }

    public short getBottom() {
        return bottom;
    }
}
