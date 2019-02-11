package org.jointheleague.jcodrone.examples;

import org.jointheleague.jcodrone.*;
import org.jointheleague.jcodrone.protocol.information.IMU;
import org.jointheleague.jcodrone.protocol.information.ImageFlow;
import org.jointheleague.jcodrone.protocol.information.Range;
import org.jointheleague.jcodrone.protocol.information.State;
import org.jointheleague.jcodrone.protocol.light.LightMode;
import org.jointheleague.jcodrone.protocol.light.LightModeDrone;

public class TestDrone {

    public static final int TIME_UNIT = 250;

    public static void main(String[] args) {
        try (CoDrone drone = new CoDrone()) {
            drone.connect("PETRONE 3888");

            while (true) {
                signal(drone);
                Thread.sleep(3000);
            }

            //automatedFlight(drone);

//            Thread.sleep(1000);
//            drone.land();
//            Thread.sleep(2000);
//            try {
//                drone.kill();
//            } catch (KillException e) {
//                e.printStackTrace();
//            }
        } catch (CoDroneNotFoundException | InterruptedException | MessageNotSentException e) {
            e.printStackTrace();
        }
    }

    private static void automatedFlight(CoDrone drone) throws MessageNotSentException, InterruptedException {
        DirectControl control = new DirectControl(0, 0, 0, 0);
        State state = drone.getState();
        Range range = drone.getRange();
        IMU imu = drone.getImu();
        ImageFlow flow = drone.getImageFlow();

        while (true) {
            if (state != null) {
                System.out.format("Flight Mode: %s\n",
                        state.isFlightMode() ? "Yes" : "No");
            }
            if (range != null) {
                System.out.format("%d\n", range.getBottom());
            }
            if (imu != null) {
                System.out.format("%d\n", imu.getAccelZ());
            }
            if (flow != null) {
                System.out.format("flow: %d %d\n", flow.getPositionX(), flow.getPositionY());
            }

//            drone.takeoff();
//            drone.flyDirect(control);
//            control.setThrottle(0);
//            Thread.sleep(2000);
//            drone.flyDirect(control);
            Thread.sleep(250);
            state = drone.getState();
            range = drone.getRange();
            imu = drone.getImu();
            flow = drone.getImageFlow();

        }
    }

    private static void signal(CoDrone drone) throws InterruptedException {
        String pattern = "...---...";
        for (byte a : pattern.getBytes()) {
            int interval = a == '.' ? TIME_UNIT : 3 * TIME_UNIT;
            System.out.format("%c %d", a, interval);
            LightMode mode2 = new LightModeBuilder()
                    .setColor("magenta")
                    .setMode(LightModeDrone.EYE_HOLD)
                    .build();
            drone.lightMode(mode2);
            Thread.sleep(interval);
            mode2 = new LightModeBuilder()
                    .setColor("magenta")
                    .setMode(LightModeDrone.EYE_NONE)
                    .build();
            drone.lightMode(mode2);
            Thread.sleep(TIME_UNIT);
        }
    }
}
