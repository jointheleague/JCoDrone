package org.jointheleague.JCodrone.System;

public enum ModeLinkDiscover {
    NONE(0x00),

    NAME(0x01),
    SERVICE(0x02),
    ALL(0x03),

    END_OF_TYPE(0x04);

    private final int mode;

    ModeLinkDiscover(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }
}
