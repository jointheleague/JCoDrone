package org.jointheleague.jcodrone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jointheleague.jcodrone.protocol.common.Control;
import org.jointheleague.jcodrone.system.FlightEvent;

public class Flight {
    private static Logger log = LogManager.getLogger(Flight.class);

    public static void takeoff(CoDrone coDrone) throws MessageNotSentException {
        sendFlightEvent(coDrone, FlightEvent.TAKEOFF);
    }

    public static void land(CoDrone coDrone) throws MessageNotSentException {
        sendInFlightEvent(coDrone, FlightEvent.LANDING);
    }

    public static void flipFront(CoDrone coDrone) throws MessageNotSentException {
        sendInFlightEvent(coDrone, FlightEvent.FLIP_FRONT);
    }

    public static void flipRear(CoDrone coDrone) throws MessageNotSentException {
        sendInFlightEvent(coDrone, FlightEvent.FLIP_REAR);
    }

    public static void flipLeft(CoDrone coDrone) throws MessageNotSentException {
        sendInFlightEvent(coDrone, FlightEvent.FLIP_LEFT);
    }

    public static void flipRight(CoDrone coDrone) throws MessageNotSentException {
        sendInFlightEvent(coDrone, FlightEvent.FLIP_RIGHT);
    }

    public static void turnOver(CoDrone coDrone) throws MessageNotSentException {
        sendInFlightEvent(coDrone, FlightEvent.STOP);
    }

    public static void shoot(CoDrone coDrone) throws MessageNotSentException {
        sendInFlightEvent(coDrone, FlightEvent.STOP);
    }

    public static void underAttack(CoDrone coDrone) throws MessageNotSentException {
        sendInFlightEvent(coDrone, FlightEvent.STOP);
    }

    public static void stop(CoDrone coDrone) throws MessageNotSentException {
        sendFlightEvent(coDrone, FlightEvent.STOP);
    }

    public static void flyDirect(CoDrone coDrone, DirectControl control) {
        Control message = new Control(
                control.getRoll(),
                control.getPitch(),
                control.getYaw(),
                control.getThrottle()
        );
        coDrone.sendMessage(message);
    }

    private static void sendInFlightEvent(CoDrone coDrone, FlightEvent event) throws MessageNotSentException {
        // The original intent was to prevent sending commands in the wrong mode.
        // For now this will be left to the implementation rather than the library.
        // TODO: prevent flying commands from being sent while the drone is not flying
//        if (coDrone.isFlying()) {
//            sendFlightEvent(coDrone, event);
//        } else {
//            log.error("Event {} can only be sent while flying.", event);
//        }
        sendFlightEvent(coDrone, event);
    }

    private static void sendFlightEvent(CoDrone coDrone, FlightEvent event) throws MessageNotSentException {
        // The original intent was to prevent sending commands in the wrong mode.
        // For now this will be left to the implementation rather than the library.
        // TODO: prevent flight commands from being sent when drone is in driving mode
//        if (coDrone.isFlightMode()) {
//            coDrone.sendCommandWait(CommandType.FLIGHT_EVENT, event.value());
//        } else {
//            log.error("Flight events can only be sent in flight mode.");
//        }
        coDrone.sendCommand(CommandType.FLIGHT_EVENT, event.value());
    }
}
