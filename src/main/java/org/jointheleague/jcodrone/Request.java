package org.jointheleague.jcodrone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jointheleague.jcodrone.protocol.DataType;

public class Request {
    private static Logger log = LogManager.getLogger(Request.class);

    public static void state(CoDrone coDrone) throws MessageNotSentException {
        sendRequest(coDrone, DataType.STATE);
    }

    public static void range(CoDrone coDrone) throws MessageNotSentException {
        sendRequest(coDrone, DataType.RANGE);
    }

    public static void IMU(CoDrone coDrone) throws MessageNotSentException {
        sendRequest(coDrone, DataType.IMU);
    }

    public static void ImageFlow(CoDrone coDrone) throws MessageNotSentException {
        sendRequest(coDrone, DataType.IMAGE_FLOW);
    }

    private static void sendRequest(CoDrone coDrone, DataType requestType) {
        coDrone.sendCommand(CommandType.REQUEST, requestType.value());
    }

}
