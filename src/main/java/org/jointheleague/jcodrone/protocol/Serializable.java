package org.jointheleague.jcodrone.protocol;

import org.jointheleague.jcodrone.CoDrone;
import org.jointheleague.jcodrone.Internals;
import org.jointheleague.jcodrone.Link;
import org.jointheleague.jcodrone.Sensors;

public interface Serializable {
    byte[] toArray();
    byte getInstanceSize();

    default void handle(CoDrone coDrone, Link link, Sensors sensors, Internals internals) {
    }
}
