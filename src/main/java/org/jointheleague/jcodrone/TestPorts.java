package org.jointheleague.jcodrone;

import org.jointheleague.jcodrone.protocol.light.LightMode;
import org.jointheleague.jcodrone.protocol.light.LightModeDrone;

public class TestPorts {
    public static void main(String argx[]) {
        try (CoDrone drone = new CoDrone()){
            drone.connect();
            Thread.sleep(2000);
            drone.flyDirect(new DirectControl((byte) 0, (byte) 0, (byte) 0, (byte) 0));
            Thread.sleep(200);
//            drone.sendCommand(CommandType.FLIGHT_EVENT, FlightEvent.TAKEOFF.value());
            Thread.sleep(2000);
            LightMode mode = new LightModeBuilder().setColor("BLUE").setInterval((short) 5).setMode(LightModeDrone.ARM_HOLD).build();
            drone.lightMode(mode);
            Thread.sleep(1000);
            mode = new LightModeBuilder().setColor("green").setInterval((short) 200).setMode(LightModeDrone.EYE_DIMMING).build();
            drone.lightMode(mode);
            Thread.sleep(3000);
//            drone.sendCommand(CommandType.FLIGHT_EVENT, FlightEvent.LANDING.value());
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
