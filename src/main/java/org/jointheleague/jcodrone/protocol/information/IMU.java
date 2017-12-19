package org.jointheleague.jcodrone.protocol.information;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;

public class IMU implements Serializable {
    private final short accelX;
    private final short accelY;
    private final short accelZ;
    private final short gyroRoll;
    private final short gyroPitch;
    private final short gyroYaw;
    private final short angleRoll;
    private final short anglePitch;
    private final short angleYaw;

    public IMU(short accelX, short accelY, short accelZ, short gyroRoll, short gyroPitch, short gyroYaw, short angleRoll, short anglePitch, short angleYaw) {
        this.accelX = accelX;
        this.accelY = accelY;
        this.accelZ = accelZ;
        this.gyroRoll = gyroRoll;
        this.gyroPitch = gyroPitch;
        this.gyroYaw = gyroYaw;
        this.angleRoll = angleRoll;
        this.anglePitch = anglePitch;
        this.angleYaw = angleYaw;
    }

    public static int getSize() {
        return 18;
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.putShort(accelX);
        buffer.putShort(accelY);
        buffer.putShort(accelZ);
        buffer.putShort(gyroRoll);
        buffer.putShort(gyroPitch);
        buffer.putShort(gyroYaw);
        buffer.putShort(angleRoll);
        buffer.putShort(anglePitch);
        buffer.putShort(angleYaw);
        return buffer.array();
    }

    public static IMU parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        short accelX = buffer.getShort();
        short accelY = buffer.getShort();
        short accelZ = buffer.getShort();
        short gyroRoll = buffer.getShort();
        short gyroPitch = buffer.getShort();
        short gyroYaw = buffer.getShort();
        short angleRoll = buffer.getShort();
        short anglePitch = buffer.getShort();
        short angleYaw = buffer.getShort();
        return new IMU(accelX, accelY, accelZ, gyroRoll, gyroPitch, gyroYaw, angleRoll, anglePitch, angleYaw);
    }
}
