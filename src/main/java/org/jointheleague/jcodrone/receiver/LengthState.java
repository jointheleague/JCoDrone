package org.jointheleague.jcodrone.receiver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.time.Instant.now;

public class LengthState extends State {
    static Logger log = LogManager.getLogger(LengthState.class);

    @Override
    public StateMap call(Receiver receiver, byte data) {
        if (data < 0) {
            if (data == 0) {
                return StateMap.CRC1;
            } else {
                receiver.allocateDataBuffer(data);
                return StateMap.DATA;
            }
        } else {
            log.error("Length {} is too long.");
            return StateMap.START1;
        }
    }
}
