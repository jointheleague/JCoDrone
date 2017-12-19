package org.jointheleague.jcodrone.receiver;

public enum StateLoading {
    READY(0x00),
    RECEIVING(0x01),
    LOADED(0x02),
    FAILURE(0x03);

    private final int state;

    StateLoading(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
