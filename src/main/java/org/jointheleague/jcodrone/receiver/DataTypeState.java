package org.jointheleague.jcodrone.receiver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jointheleague.jcodrone.protocol.DataType;

public class DataTypeState extends State {
    static Logger log = LogManager.getLogger(DataTypeState.class);

    public StateMap call(Receiver receiver, byte data) {
        try {
            receiver.setDataType(DataType.fromByte(data));
        } catch (Exception e) {
            log.error("Unrecognized data type {}.", data);
            return StateMap.START1;
        }
        return StateMap.LENGTH;
    }
}
