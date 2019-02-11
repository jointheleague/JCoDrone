package org.jointheleague.jcodrone;

public class BluetoothBoardNotFoundException extends Exception {
    public BluetoothBoardNotFoundException() {
        super("Bluetooth board not found.");
    }
}
