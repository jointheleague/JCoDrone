package org.jointheleague.jcodrone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jointheleague.jcodrone.protocol.common.Control;
import org.jointheleague.jcodrone.system.FlightEvent;

public class Flight {
    private static Logger log = LogManager.getLogger(Flight.class);

    public static void takeoff(CoDrone coDrone) {
        sendFlightEvent(coDrone, FlightEvent.TAKEOFF);
    }

    public static void land(CoDrone coDrone) {
        sendInFlightEvent(coDrone, FlightEvent.LANDING);
    }

    public static void flipFront(CoDrone coDrone) {
        sendInFlightEvent(coDrone, FlightEvent.FLIP_FRONT);
    }

    public static void flipRear(CoDrone coDrone) {
        sendInFlightEvent(coDrone, FlightEvent.FLIP_REAR);
    }

    public static void flipLeft(CoDrone coDrone) {
        sendInFlightEvent(coDrone, FlightEvent.FLIP_LEFT);
    }

    public static void flipRight(CoDrone coDrone) {
        sendInFlightEvent(coDrone, FlightEvent.FLIP_RIGHT);
    }

    public static void turnOver(CoDrone coDrone) {
        sendInFlightEvent(coDrone, FlightEvent.STOP);
    }

    public static void shoot(CoDrone coDrone) {
        sendInFlightEvent(coDrone, FlightEvent.STOP);
    }

    public static void underAttack(CoDrone coDrone) {
        sendInFlightEvent(coDrone, FlightEvent.STOP);
    }

    public static void stop(CoDrone coDrone) {
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

    private static void sendInFlightEvent(CoDrone coDrone, FlightEvent event) {
        if (true) { //coDrone.isFlying()) { TODO: Determine if drone is flying.
            sendFlightEvent(coDrone, event);
        } else {
            log.error("Event {} can only be sent while flying.", event);
        }
    }

    private static void sendFlightEvent(CoDrone coDrone, FlightEvent event) {
        if (coDrone.isFlightMode()) {
            coDrone.sendCommand(CommandType.FLIGHT_EVENT, event.value());
        } else {
            log.error("Flight events can only be sent in flight mode.");
        }
    }
}
