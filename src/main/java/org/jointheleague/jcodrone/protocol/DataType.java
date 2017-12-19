package org.jointheleague.jcodrone.protocol;

import java.util.HashMap;
import java.util.Map;

public enum DataType {
    NONE((byte) 0x00),

    // BLE + Serial
    PING((byte) 0x01),  // check
    ACK((byte) 0x02),  // reply
    ERROR((byte) 0x03),
    REQUEST((byte) 0x04),
    PASSCODE((byte) 0x05),  // new passcode for pairing

    CONTROL((byte) 0x10),
    COMMAND((byte) 0x11),
    COMMAND_2((byte) 0x12),
    COMMAND_3((byte) 0x13),

    // Light
    LIGHT_MODE((byte) 0x20),  // set LED Mode
    LIGHT_MODE_2((byte) 0x21),  // set LED Mode
    LIGHT_MODE_COMMAND((byte) 0x22),  // set LED Mode Commend
    LIGHT_MODE_COMMAND_IR((byte) 0x23), // set LED Mode Commend IR
    LIGHT_MODE_COLOR((byte) 0x24),  // set LED Mode RGB color
    LIGHT_MODE_COLOR_2((byte) 0x25),  // set LED Mode RGB color

    LIGHT_EVENT((byte) 0x26),  // LED event
    LIGHT_EVENT_2((byte) 0x27),  // LED event
    LIGHT_EVENT_COMMAND((byte) 0x28),  // LED event Commend
    LIGHT_EVENT_COMMAND_IR((byte) 0x29),  // LED event Commend IR
    LIGHT_EVENT_COLOR((byte) 0x2A),  // LED event RGB color
    LIGHT_EVENT_COLOR_2((byte) 0x2B),  // LED event RGB color

    LIGHT_MODE_DEFAULT_COLOR((byte) 0x2C),  // set LED default color
    LIGHT_MODE_DEFAULT_COLOR_2((byte) 0x2D),  // set LED default color

    // status
    ADDRESS((byte) 0x30),  // IEEE Address
    STATE((byte) 0x31),  // drone's state(flight mode, cordinate, battery level)
    ATTITUDE((byte) 0x32),  // attitude(Angle)
    GYRO_BIAS((byte) 0x33),
    TRIM_ALL((byte) 0x34),  // trim for roll pitch yaw throttle wheel
    TRIM_FLIGHT((byte) 0x35),  // trim for roll pitch yaw throttle
    TRIM_DRIVE((byte) 0x36),  // trim for wheel

    COUNT_FLIGHT((byte) 0x37),  // count about flight
    COUNT_DRIVE((byte) 0x38),  // count about drive

    // IR
    IR_MESSAGE((byte) 0x40),  // IR data transfer or receive

    // Sensor and control
    IMU((byte) 0x50),  // IMU Raw
    PRESSURE((byte) 0x51),
    IMAGE_FLOW((byte) 0x52),  // ImageFlow
    BUTTON((byte) 0x53),
    BATTERY((byte) 0x54),
    MOTOR((byte) 0x55),  // moter control value and current value for control
    TEMPERATURE((byte) 0x56),
    RANGE((byte) 0x57),  // bottom Ir range sensor

    // Firmware update
    UPDATE_LOOKUP_TARGET((byte) 0x90),
    UPDATE_INFORMATION((byte) 0x91),
    UPDATE((byte) 0x92),
    UPDATE_LOCATION_CORRECT((byte) 0x93),

    // LINK board
    LINK_STATE((byte) 0xE0),
    LINK_EVENT((byte) 0xE1),
    LINK_EVENT_ADDRESS((byte) 0xE2),  // event + address
    LINK_RSSI((byte) 0xE3),  // rssi signal power which is connected with link board
    LINK_DISCOVERED_DEVICE((byte) 0xE4),
    LINK_PASSCODE((byte) 0xE5),  // set passcode for pairing

    MESSAGE((byte) 0xF0),  // string message

    END_OF_TYPE((byte) 0xFF),;

    private static final Map<Byte, DataType> BYTE_DATA_TYPE_MAP =
            new HashMap<>();

    static {
        for (DataType type : DataType.values()) {
            BYTE_DATA_TYPE_MAP.put(type.value(), type);
        }
    }

    private final byte type;

    DataType(byte type) {
        this.type = type;
    }

    public byte value() {
        return type;
    }

    public static DataType fromByte(byte b) {
        return BYTE_DATA_TYPE_MAP.get(b);
    }
}
