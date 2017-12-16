package org.jointheleague.JCodrone.System;

public enum ModeLinkBroadcast {
    NONE(0x00),

    MUTE(0x01),  // block data request
    ACTIVE(0x02),  // data transport request and some auto thing
    PASSIVE(0x03), // reply just request things - no data transport when status is change, 연결 등의 상황에서는 진행 상황 전송

    END_OF_TYPE(0x07);

    private final int mode;

    ModeLinkBroadcast(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }
}
