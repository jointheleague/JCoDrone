package org.jointheleague.jcodrone.protocol;

import java.util.HashMap;
import java.util.Map;

public enum CommandType {
    NONE((byte) 0x00),

    // setting
    MODE_VEHICLE((byte) 0x10),

    // control
    HEADLESS((byte) 0x20),  // headless mode
    TRIM((byte) 0x21),
    FLIGHT_EVENT((byte) 0x22),
    DRIVE_EVENT((byte) 0x23),
    STOP((byte) 0x24),  // killswitch

    RESET_HEADING((byte) 0x50),  // head reset
    CLEAR_GYRO_BIAS((byte) 0x51),  // clear trim and gyroBias
    CLEAR_TRIM((byte) 0x52),  // clear trim

    // Wireless Lan
    RESET_WIRELESS_LAN((byte) 0x70),
    WIRELESS_LAN_CONNECTED((byte) 0x70),
    WIRELESS_LAN_DISCONNECTED((byte) 0x70),

    // Bluetooth
    PAIRING_ACTIVATE((byte) 0x80),
    PAIRING_DEACTIVATE((byte) 0x81),
    ADVERTISING_START((byte) 0x82),
    ADVERTISING_STOP((byte) 0x83),
    TERMINATE_CONNECTION((byte) 0x84),
    CLEAR_BOND_LIST((byte) 0x85), //clear bond device info

    // request
    REQUEST((byte) 0x90),
    UPDATE_COMPLETE_SUB((byte) 0x90),
    CLEAR_UPDATE_AREA_MAIN((byte) 0x90),

    // LINK 모듈
    LINK_MODE_BROADCAST((byte) 0xE0),  // LINK 송수신 모드 전환
    LINK_SYSTEM_RESET((byte) 0xE1),  // 시스템 재시작
    LINK_DISCOVER_START((byte) 0xE2),  // 장치 검색 시작
    LINK_DISCOVER_STOP((byte) 0xE3),  // 장치 검색 중단
    LINK_CONNECT((byte) 0xE4),  // 지정한 인덱스의 장치 연결
    LINK_DISCONNECT((byte) 0xE5),  // 연결 해제
    LINK_RSSI_POLLING_START((byte) 0xE6),  // RSSI 수집 시작
    LINK_RSSI_POLLING_STOP((byte) 0xE7),  // RSSI 수집 중단

    END_OF_TYPE((byte) 0xFF);

    private static final Map<Byte, CommandType> BYTE_COMMAND_TYPE_MAP =
            new HashMap<>();

    static {
        for (CommandType type : CommandType.values()) {
            BYTE_COMMAND_TYPE_MAP.put(type.value(), type);
        }
    }

    private final byte type;

    CommandType(byte type) {
        this.type = type;
    }

    public byte value() {
        return type;
    }

    public static CommandType fromByte(byte b) {
        return BYTE_COMMAND_TYPE_MAP.get(b);
    }
}
