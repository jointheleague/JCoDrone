package org.jointheleague.jcodrone.protocol;

import org.jointheleague.jcodrone.protocol.common.*;
import org.jointheleague.jcodrone.protocol.information.*;
import org.jointheleague.jcodrone.protocol.light.*;
import org.jointheleague.jcodrone.protocol.link.*;
import org.jointheleague.jcodrone.protocol.message.Message;
import org.jointheleague.jcodrone.protocol.update.*;

import java.util.HashMap;
import java.util.Map;

public enum DataType {
    NONE((byte) 0x00, null),

    // BLE + Serial
    PING((byte) 0x01, Ping.class),  // check
    ACK((byte) 0x02, Ack.class),  // reply
    ERROR((byte) 0x03, null),
    REQUEST((byte) 0x04, Request.class),
    PASSCODE((byte) 0x05, Passcode.class),  // new passcode for pairing

    CONTROL((byte) 0x10, Control.class),
    COMMAND((byte) 0x11, Command.class),
    COMMAND_2((byte) 0x12, Command2.class),
    COMMAND_3((byte) 0x13, Command3.class),

    // LED
    LIGHT_MODE((byte) 0x20, LightModeColors.class),  // set LED Mode
    LIGHT_MODE_2((byte) 0x21, LightModeColors2.class),  // set LED Mode
    LIGHT_MODE_COMMAND((byte) 0x22, LightModeCommand.class),  // set LED Mode Commend
    LIGHT_MODE_COMMAND_IR((byte) 0x23, LightModeCommandIR.class), // set LED Mode Commend IR
    LIGHT_MODE_COLOR((byte) 0x24, LightModeColor.class),  // set LED Mode RGB color
    LIGHT_MODE_COLOR_2((byte) 0x25, LightModeColor2.class),  // set LED Mode RGB color

    LIGHT_EVENT((byte) 0x26, LightEventColors.class),  // LED event
    LIGHT_EVENT_2((byte) 0x27, LightEventColors2.class),  // LED event
    LIGHT_EVENT_COMMAND((byte) 0x28, LightEventCommand.class),  // LED event Commend
    LIGHT_EVENT_COMMAND_IR((byte) 0x29, LightEventCommandIR.class),  // LED event Commend IR
    LIGHT_EVENT_COLOR((byte) 0x2A, LightEventColor.class),  // LED event RGB color
    LIGHT_EVENT_COLOR_2((byte) 0x2B, LightEventColor2.class),  // LED event RGB color

    LIGHT_MODE_DEFAULT_COLOR((byte) 0x2C, LightModeDefaultColor.class),  // set LED default color
    LIGHT_MODE_DEFAULT_COLOR_2((byte) 0x2D, LightModeDefaultColor2.class),  // set LED default color

    // status
    ADDRESS((byte) 0x30, Address.class),  // IEEE Address
    STATE((byte) 0x31, State.class),  // drone's state(flight mode, cordinate, battery level)
    ATTITUDE((byte) 0x32, Attitude.class),  // attitude(Angle)
    GYRO_BIAS((byte) 0x33, GyroBias.class),
    TRIM_ALL((byte) 0x34, TrimAll.class),  // trim for roll pitch yaw throttle wheel
    TRIM_FLIGHT((byte) 0x35, TrimFlight.class),  // trim for roll pitch yaw throttle
    TRIM_DRIVE((byte) 0x36, TrimDrive.class),  // trim for wheel

    COUNT_FLIGHT((byte) 0x37, CountFlight.class),  // count about flight
    COUNT_DRIVE((byte) 0x38, CountDrive.class),  // count about drive

    // IR
    IR_MESSAGE((byte) 0x40, IRMessage.class),  // IR data transfer or receive

    // Sensor and control
    IMU((byte) 0x50, org.jointheleague.jcodrone.protocol.information.IMU.class),  // IMU Raw
    PRESSURE((byte) 0x51, Pressure.class),
    IMAGE_FLOW((byte) 0x52, ImageFlow.class),  // ImageFlow
    BUTTON((byte) 0x53, Button.class),
    BATTERY((byte) 0x54, Battery.class),
    MOTOR((byte) 0x55, Motor.class),  // moter control value and current value for control
    TEMPERATURE((byte) 0x56, Temperature.class),
    RANGE((byte) 0x57, Range.class),  // bottom Ir range sensor

    // Firmware update
    UPDATE_LOOKUP_TARGET((byte) 0x90, UpdateLookupTarget.class),
    UPDATE_INFORMATION((byte) 0x91, UpdateInformation.class),
    UPDATE((byte) 0x92, Update.class),
    UPDATE_LOCATION_CORRECT((byte) 0x93, UpdateLocationCorrect.class),

    // LINK board
    LINK_STATE((byte) 0xE0, LinkState.class),
    LINK_EVENT((byte) 0xE1, LinkEvent.class),
    LINK_EVENT_ADDRESS((byte) 0xE2, LinkEventAddress.class),  // event + address
    LINK_RSSI((byte) 0xE3, LinkRSSI.class),  // rssi signal power which is connected with link board
    LINK_DISCOVERED_DEVICE((byte) 0xE4, LinkDiscoveredDevice.class),
    LINK_PASSCODE((byte) 0xE5, LinkPasscode.class),  // set passcode for pairing

    MESSAGE((byte) 0xF0, Message.class),  // string message

    END_OF_TYPE((byte) 0xFF, null),;

    private static final Map<Byte, DataType> BYTE_DATA_TYPE_MAP =
            new HashMap<>();

    private static final Map<Class, DataType> CLASS_DATA_TYPE_MAP =
            new HashMap<>();

    static {
        for (DataType type : DataType.values()) {
            BYTE_DATA_TYPE_MAP.put(type.value(), type);
            CLASS_DATA_TYPE_MAP.put(type.getMessageClass(),type);
        }
    }

    private final byte type;
    private final Class<? extends Serializable> messageClass;

    DataType(byte type, Class<? extends Serializable> messageClass) {
        this.type = type;
        this.messageClass = messageClass;
    }

    public byte value() {
        return type;
    }

    public static DataType fromByte(byte b) {
        return BYTE_DATA_TYPE_MAP.get(b);
    }

    public static DataType fromClass(Class c) {
        return CLASS_DATA_TYPE_MAP.get(c);
    }

    public Class<? extends Serializable> getMessageClass() {
        return messageClass;
    }
}
