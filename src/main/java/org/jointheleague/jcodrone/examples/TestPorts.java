package org.jointheleague.jcodrone.examples;

import org.jointheleague.jcodrone.CoDrone;
import org.jointheleague.jcodrone.DirectControl;
import org.jointheleague.jcodrone.LightModeBuilder;
import org.jointheleague.jcodrone.protocol.light.LightMode;
import org.jointheleague.jcodrone.protocol.light.LightModeDrone;

public class TestPorts {
    public static void main(String argx[]) {
        try (CoDrone drone = new CoDrone()){
            drone.connect();
            Thread.sleep(2000);
            drone.flyDirect(new DirectControl((byte) 0, (byte) 0, (byte) 0, (byte) 0));
            Thread.sleep(200);
//            for(int i = 0; i < 1080; i++) {
//                Color c = Color.getHSBColor(i / 360.0f, 1.0f, 1.0f);
//                LightMode mode2 = new LightModeBuilder().setColor(c.getRed(), c.getGreen(), c.getBlue()).setMode(LightModeDrone.EYE_HOLD).build();
//                drone.lightMode(mode2);
//                Thread.sleep(60);
//            }
            drone.takeoff();
            Thread.sleep(2000);
            drone.flyDirect(new DirectControl((byte) 0, (byte) 0, (byte) 0, 30));
            Thread.sleep(2000);

            for (int i = 0; i < 3; i++) {
                LightMode mode = new LightModeBuilder().setColor("BLUE").setInterval(5).setMode(LightModeDrone.EYE_HOLD).build();
                drone.lightMode(mode);
                Thread.sleep(100);
                drone.flyDirect(new DirectControl((byte) 0, (byte) 0, (byte) 55, 40));
                Thread.sleep(2000);
                mode = new LightModeBuilder().setColor("MAGENTA").setInterval(5).setMode(LightModeDrone.EYE_HOLD).build();
                drone.lightMode(mode);
                Thread.sleep(100);
                drone.flyDirect(new DirectControl((byte) 0, (byte) 0, (byte) -55, -15));
                Thread.sleep(2000);
            }
//            drone.takeoff();
//            Thread.sleep(2000);
//            drone.flyDirect(new DirectControl((byte) 0, (byte) 0, (byte) 0, 30));
//            Thread.sleep(2000);
//
//            for (int i = 0; i < 3; i++) {
//                LightMode mode = new LightModeBuilder().setColor("BLUE").setInterval(5).setMode(LightModeDrone.EYE_HOLD).build();
//                drone.lightMode(mode);
//                Thread.sleep(100);
//                drone.flyDirect(new DirectControl((byte) 0, (byte) 0, (byte) 55, 30));
//                Thread.sleep(2000);
//                mode = new LightModeBuilder().setColor("RED").setInterval(5).setMode(LightModeDrone.EYE_HOLD).build();
//                drone.lightMode(mode);
//                Thread.sleep(100);
//                drone.flyDirect(new DirectControl((byte) 0, (byte) 0, (byte) -55, -15));
//                Thread.sleep(2000);
//            }
//            drone.flyDirect(new DirectControl((byte) 0, (byte) 0, (byte) 0, (byte) 0));
//            Thread.sleep(200);
//            LightMode mode = new LightModeBuilder().setColor("BLUE").setInterval(5).setMode(LightModeDrone.EYE_HOLD).build();
//            drone.lightMode(mode);
//            Thread.sleep(1000);
//            mode = new LightModeBuilder().setColor("green").setInterval(200).setMode(LightModeDrone.EYE_DIMMING).build();
//            drone.lightMode(mode);
//            Thread.sleep(3000);
//            drone.land();
//            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
