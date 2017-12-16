package org.jointheleague.JCodrone.System;

public enum ImageType {
    NONE(0x00),

    // device image
    IMAGE_A(0x01),
    IMAGE_B(0x02),

    // firmware image
    RAW_IMAGE_A(0x03),
    RAW_IMAGE_B(0x04),

    // encrypted image
    ENCRYPTED_IMAGE_A(0x05),
    ENCRYPTED_IMAGE_B(0x06),

    // device image(CC253x / CC254x)
    IMAGE_SINGLE(0x07),

    // run Image
    RAW_IMAGE_SINGLE(0x08),
    ENCRYPTED_IMAGE_SINGLE(0x09),

    END_OF_TYPE(0x0A);

    private final int type;

    ImageType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
