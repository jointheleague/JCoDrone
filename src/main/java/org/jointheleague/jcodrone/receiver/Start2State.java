package org.jointheleague.jcodrone.receiver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Start2State extends State {
    static Logger log = LogManager.getLogger(Start2State.class);

    @Override
    public StateMap call(Receiver receiver, byte data) {
        if (data == 0x55) {
            receiver.setCrc16calculated(CRC16.calc(data, receiver.getCrc16calculated()));
            return StateMap.DATA_TYPE;
        } else {
            log.error("Unexpected byte {} received while expecting second byte of message");
            return StateMap.START1;
        }
    }
}
