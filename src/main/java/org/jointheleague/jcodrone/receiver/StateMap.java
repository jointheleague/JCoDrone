package org.jointheleague.jcodrone.receiver;

import java.util.zip.CRC32;

public enum StateMap {
    START1(new Start1State()),
    START2(new Start2State()),
    DATA_TYPE(new DataTypeState()),
    LENGTH(new LengthState()),
    CRC1(new CRC1State()),
    CRC2(new CRC2State()),
    DATA(new DataState());

    private final State state;

    StateMap(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }
}
