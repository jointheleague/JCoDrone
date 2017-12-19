package org.jointheleague.jcodrone.protocol.information;

public enum ButtonFlag {
    NONE((byte) 0x00),
    RESET((byte) 0x01);

    private final byte flag;

    ButtonFlag(byte flag) {
        this.flag = flag;
    }

    public byte value() {
        return flag;
    }
}
