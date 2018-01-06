package org.jointheleague.jcodrone.receiver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CRC2State extends State {
    static Logger log = LogManager.getLogger(CRC2State.class);

    @Override
    public StateMap call(Receiver receiver, byte data) {
        receiver.setCRC2(data);
        if (!receiver.isCRCValid()) {
            log.error("Invalid CRC received for message.");
        } else {
            receiver.handleMessage();
        }
        return StateMap.START1;
    }
}
