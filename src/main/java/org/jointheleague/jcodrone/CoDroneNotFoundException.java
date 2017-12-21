package org.jointheleague.jcodrone;

public class CoDroneNotFoundException extends Exception {
    public CoDroneNotFoundException() {
        super("CoDrone not found.");
    }

    public CoDroneNotFoundException(String deviceName) {
        super("CoDrone, "+deviceName+", not found.");
    }
}
