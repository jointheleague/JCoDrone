package org.jointheleague.jcodrone.receiver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataState extends State {
    static Logger log = LogManager.getLogger(DataState.class);

    @Override
    public StateMap call(Receiver receiver, byte data) {
        if (receiver.putData(data)) {
            return StateMap.CRC1;
        } else {
            return StateMap.DATA;
        }

    }
}
