package org.jointheleague.jcodrone;

public class TestPorts {
    public static void main(String argx[]) {
        try (CoDrone drone = new CoDrone()){
            drone.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
