package org.jointheleague.jcodrone.system;

import java.util.HashMap;
import java.util.Map;

public enum ImageType {
    NONE((byte) 0x00),

    // device image
    IMAGE_A((byte) 0x01),
    IMAGE_B((byte) 0x02),

    // firmware image
    RAW_IMAGE_A((byte) 0x03),
    RAW_IMAGE_B((byte) 0x04),

    // encrypted image
    ENCRYPTED_IMAGE_A((byte) 0x05),
    ENCRYPTED_IMAGE_B((byte) 0x06),

    // device image(CC253x / CC254x)
    IMAGE_SINGLE((byte) 0x07),

    // run Image
    RAW_IMAGE_SINGLE((byte) 0x08),
    ENCRYPTED_IMAGE_SINGLE((byte) 0x09),

    END_OF_TYPE((byte) 0x0A);

    private static final Map<Byte, ImageType> BYTE_IMAGE_TYPE_MAP =
            new HashMap<>();

    static {
        for (ImageType imageType : ImageType.values()) {
            BYTE_IMAGE_TYPE_MAP.put(imageType.value(), imageType);
        }
    }

    private final byte type;

    ImageType(byte type) {
        this.type = type;
    }

    public byte value() {
        return type;
    }

    public static ImageType forValue(byte b) {
        return BYTE_IMAGE_TYPE_MAP.get(b);
    }
}
