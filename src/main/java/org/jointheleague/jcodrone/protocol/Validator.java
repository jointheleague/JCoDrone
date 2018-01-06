package org.jointheleague.jcodrone.protocol;

public class Validator {
    public static boolean isValidUnsignedByte(int value) {
        return ((value & 0xFFFFFF00) == 0);
    }

    public static boolean isValidControl(int value) {
        return ((-100 <= value) && (value <= 100));
    }
}
