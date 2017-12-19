package org.jointheleague.jcodrone.receiver;

public enum Section {
    START(0x00),
    HEADER(0x01),
    DATA(0x02),
    END(0x03);

    private final int selection;

    Section(int selection) {
        this.selection = selection;
    }

    public int getSelection() {
        return selection;
    }
}
