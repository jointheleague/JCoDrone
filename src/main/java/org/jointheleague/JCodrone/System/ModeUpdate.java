package org.jointheleague.JCodrone.System;

public enum ModeUpdate {
    NONE(0x00),

    READY(0x01),    // ready for modeupdate
    UPDATE(0x02),   // updating
    COMPLPETE(0x03),// finish update
    FAILD(0x04),    // faild (ex : update finish but body's CRC16), is not match)
    END_OF_TYPE(0x05),;

    private final int mode;

    ModeUpdate(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }
}
