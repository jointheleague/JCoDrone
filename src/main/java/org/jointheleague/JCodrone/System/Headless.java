package org.jointheleague.JCodrone.System;

public enum Headless {
    NONE(0x00),
    HEADLESS(0x01), // Headless
    NORMAL(0x02),   // Normal
    END_OF_TYPE(0x03);

    private final int headless;

    Headless(int headless) {
        this.headless = headless;
    }

    public int getHeadless() {
        return headless;
    }
}
