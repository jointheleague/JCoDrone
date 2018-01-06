package org.jointheleague.jcodrone.protocol.light;

import org.jointheleague.jcodrone.protocol.Serializable;

import static org.jointheleague.jcodrone.protocol.Validator.isValidUnsignedByte;

public abstract class LightMode implements Serializable {
    protected final LightModeDrone mode;
    final int interval;

    @SuppressWarnings("WeakerAccess")
    public LightMode(int interval, LightModeDrone mode) {
        if (!isValidUnsignedByte(interval)) {
            throw new IllegalArgumentException("Interval must be between 0 and 255.");
        }
        this.interval = interval;
        this.mode = mode;
    }

    @Override
    public abstract byte[] toArray();
}
