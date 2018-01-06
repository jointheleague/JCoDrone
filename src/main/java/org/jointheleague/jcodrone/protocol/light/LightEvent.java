package org.jointheleague.jcodrone.protocol.light;

import org.jointheleague.jcodrone.protocol.Serializable;

public abstract class LightEvent implements Serializable {
    protected final LightModeDrone mode;
    protected final int interval;
    protected final int repeat;

    @SuppressWarnings("WeakerAccess")
    public LightEvent(int interval, int repeat, LightModeDrone mode) {
        this.interval = interval;
        this.repeat = repeat;
        this.mode = mode;
    }

    public abstract byte getInstanceSize();

    @Override
    public abstract byte[] toArray();
}
