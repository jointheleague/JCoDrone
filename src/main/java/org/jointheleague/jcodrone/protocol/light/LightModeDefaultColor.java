package org.jointheleague.jcodrone.protocol.light;

import org.jointheleague.jcodrone.protocol.Serializable;

public class LightModeDefaultColor extends LightModeColor {
    public LightModeDefaultColor(LightModeDrone mode, Color color, short interval) {
        super(mode, color, interval);
    }
}
