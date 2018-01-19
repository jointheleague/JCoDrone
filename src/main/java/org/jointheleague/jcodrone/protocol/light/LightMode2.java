package org.jointheleague.jcodrone.protocol.light;

public class LightMode2 {
    public static LightMode2 getLightMode2(LightMode mode1, LightMode mode2) {
        if (mode1 instanceof LightModeColor && mode2 instanceof LightModeColor) {
            return new LightModeColor2((LightModeColor) mode1, (LightModeColor) mode2);
        } else if (mode1 instanceof LightModeColors && mode2 instanceof LightModeColors) {
            return new LightModeColors2((LightModeColors) mode1, (LightModeColors) mode2);
        } else {
            throw new IllegalArgumentException("Modes must match.");
        }
    }
}
