package org.jointheleague.jcodrone.protocol.information;

import org.jointheleague.jcodrone.CoDrone;
import org.jointheleague.jcodrone.Internals;
import org.jointheleague.jcodrone.Link;
import org.jointheleague.jcodrone.Sensors;
import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;
import org.jointheleague.jcodrone.system.*;

import java.nio.ByteBuffer;
import java.util.Arrays;

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

    public static byte getSize() {
        return 7;
    }

    public byte getInstanceSize() {
        return getSize();
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

    @Override
    public void handle(CoDrone coDrone, Link link, Sensors sensors, Internals internals) {
        internals.setState(this);
    }

    public boolean isFlightMode() {
        return Arrays.asList(
                new ModeVehicle[]{
                        ModeVehicle.FLIGHT_FPV,
                        ModeVehicle.FLIGHT_NO_GUARD,
                        ModeVehicle.FLIGHT_GUARD}
        ).contains(modeVehicle);
    }
}
