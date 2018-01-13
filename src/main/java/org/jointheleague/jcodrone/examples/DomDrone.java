package org.jointheleague.jcodrone.examples;

import org.jointheleague.jcodrone.CoDrone;
import org.jointheleague.jcodrone.CoDroneNotFoundException;
import org.jointheleague.jcodrone.LightModeBuilder;
import org.jointheleague.jcodrone.MessageNotSentException;
import org.jointheleague.jcodrone.protocol.light.LightMode;
import org.jointheleague.jcodrone.protocol.light.LightModeDrone;

public class DomDrone {

    public static final int TIME_UNIT = 250;

    public static void main(String args[]) {
        try (CoDrone drone = new CoDrone()) {
            drone.connect();
//            Thread.sleep(2000);
//            LightMode mode= new LightModeBuilder().setColor("BLUE").setMode(LightModeDrone.EYE_HOLD).build();
//            drone.lightMode(mode);
//            Thread.sleep(2000);
//            mode= new LightModeBuilder().setColor("orange").setMode(LightModeDrone.ARM_NONE).build();
//            drone.lightMode(mode);
//            Thread.sleep(2000);
            drone.land();
            Thread.sleep(2000);

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
            Thread.sleep(2000);
            drone.land();
        } catch (CoDroneNotFoundException | InterruptedException | MessageNotSentException e) {
            e.printStackTrace();
        }
    }
}
