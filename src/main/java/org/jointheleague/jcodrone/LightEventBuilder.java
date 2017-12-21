package org.jointheleague.jcodrone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jointheleague.jcodrone.protocol.light.*;

public class LightEventBuilder {
    private static Logger log = LogManager.getLogger(LightEventBuilder.class);

    private LightModeDrone mode = null;
    private Colors colors = null;
    private Color color = null;
    private short interval = 0;
    private short repeat = 0;

    private boolean modeSet = false;
    private boolean colorSet = false;
    private boolean colorsSet = false;

    public LightEventBuilder setMode(LightModeDrone mode) {
        this.mode = mode;
        modeSet = true;
        return this;
    }

    public LightEventBuilder setColor(Colors color) {
        if (colorSet) {
            this.colorSet = false;
            log.warn("Value of color is being overridden by setColors for light mode.");
        }
        this.colors = colors;
        this.colorsSet = true;
        return this;
    }

    public LightEventBuilder setColor(String color) {
        return this.setColor(Colors.valueOf(color.toLowerCase()));
    }

    public LightEventBuilder setColor(Color color) {
        if (colorsSet) {
            this.colorsSet = false;
            log.warn("Value of colors is being overridden by setColor for light mode.");
        }
        this.color = color;
        this.colorsSet = true;
        return this;
    }

    public LightEventBuilder setColor(short r, short g, short b) {
        return this.setColor(new Color(r, g, b));
    }

    public LightEventBuilder setInterval(short interval) {
        this.interval = interval;
        return this;
    }

    public LightEventBuilder setRepeat(short repeat) {
        this.repeat = repeat;
        return this;
    }


    public LightEvent build() {
        if (!isModeSet() || (!isColorsSet() && !isColorSet())) {
            throw new IllegalStateException("A light mode requires a mode and a color.");
        } else {
            if (isColorsSet()) {
                return new LightEventColors(mode, colors, interval, repeat);
            } else {
                return new LightEventColor(mode, color, interval, repeat);
            }
        }
    }

    private boolean isColorsSet() {
        return colorsSet;
    }

    private boolean isModeSet() {
        return modeSet;
    }

    private boolean isColorSet() {
        return colorSet;
    }
}