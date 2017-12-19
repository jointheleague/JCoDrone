package org.jointheleague.jcodrone.protocol.information;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;
import org.jointheleague.jcodrone.system.*;

import java.nio.ByteBuffer;

public class State implements Serializable {
    private final ModeVehicle modeVehicle;

    private final ModeSystem modeSystem;
    private final ModeFlight modeFlight;
    private final ModeDrive modeDrive;

    private final SensorOrientation sensorOrientation;
    private final Headless headless;
    private final byte battery;

    public State(ModeVehicle modeVehicle, ModeSystem modeSystem, ModeFlight modeFlight, ModeDrive modeDrive, SensorOrientation sensorOrientation, Headless headless, byte battery) {
        this.modeVehicle = modeVehicle;
        this.modeSystem = modeSystem;
        this.modeFlight = modeFlight;
        this.modeDrive = modeDrive;
        this.sensorOrientation = sensorOrientation;
        this.headless = headless;
        this.battery = battery;
    }

    public static int getSize() {
        return 7;
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        buffer.put(modeVehicle.value());
        buffer.put(modeSystem.value());
        buffer.put(modeFlight.value());
        buffer.put(modeDrive.value());
        buffer.put(sensorOrientation.value());
        buffer.put(headless.value());
        buffer.put(battery);
        return buffer.array();
    }

    public static State parse(byte[] data) throws InvalidDataSizeException {
        if (data.length != getSize()) {
            throw new InvalidDataSizeException(getSize(), data.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        ModeVehicle modeVehicle = ModeVehicle.fromByte(buffer.get());
        ModeSystem modeSystem = ModeSystem.fromByte(buffer.get());
        ModeFlight modeFlight = ModeFlight.fromByte(buffer.get());
        ModeDrive modeDrive = ModeDrive.fromByte(buffer.get());
        SensorOrientation sensorOrientation = SensorOrientation.fromByte(buffer.get());
        Headless headless = Headless.fromByte(buffer.get());
        byte battery = buffer.get();
        return new State(
                modeVehicle,
                modeSystem,
                modeFlight,
                modeDrive,
                sensorOrientation,
                headless,
                battery
        );
    }
}
