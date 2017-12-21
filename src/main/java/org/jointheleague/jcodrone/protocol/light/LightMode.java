package org.jointheleague.jcodrone.protocol.light;

import org.jointheleague.jcodrone.protocol.Serializable;

public abstract class LightMode implements Serializable {
    protected final LightModeDrone mode;
    protected final short interval;

    public LightMode(short interval, LightModeDrone mode) {
        this.interval = interval;
        this.mode = mode;
    }

    @Override
    public abstract byte[] toArray();
}
