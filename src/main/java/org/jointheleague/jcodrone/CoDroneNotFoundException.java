package org.jointheleague.jcodrone;

public class CoDroneNotFoundException extends Exception {
    @SuppressWarnings("WeakerAccess")
    public CoDroneNotFoundException() {
        super("CoDrone not found.");
    }

    @SuppressWarnings("WeakerAccess")
    public CoDroneNotFoundException(String deviceName) {
        super("CoDrone, "+deviceName+", not found.");
    }
}
