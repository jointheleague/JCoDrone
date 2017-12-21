package org.jointheleague.jcodrone.protocol;

import org.jointheleague.jcodrone.CoDrone;

public interface Serializable {
    byte[] toArray();
    byte getInstanceSize();
    default void handle(CoDrone coDrone) {}
}
