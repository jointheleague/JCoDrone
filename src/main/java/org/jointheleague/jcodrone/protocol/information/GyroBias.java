package org.jointheleague.jcodrone.protocol.information;

import org.jointheleague.jcodrone.CoDrone;
import org.jointheleague.jcodrone.Internals;
import org.jointheleague.jcodrone.Link;
import org.jointheleague.jcodrone.Sensors;

public class GyroBias extends Attitude {
    public GyroBias(short roll, short pitch, short yaw) {
        super(roll, pitch, yaw);
    } // TODO find implementation

    @Override
    public void handle(CoDrone coDrone, Link link, Sensors sensors, Internals internals) {
        sensors.setGyroBias(this);
    }
}
