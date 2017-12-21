package org.jointheleague.jcodrone.protocol.message;

import org.jointheleague.jcodrone.protocol.InvalidDataSizeException;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public class Message implements Serializable {
    public final char[] message;

    public Message(char[] message) {
        this.message = message;
    }

    public Message(String message) {
        this.message = message.toCharArray();
    }

    public int getSize() {
        return message.length;
    }

    public byte getInstanceSize() {
        return (byte) getSize();
    }

    @Override
    public byte[] toArray() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        CharBuffer cb = buffer.asCharBuffer();
        cb.put(message);
        return buffer.array();
    }

    public static Message parse(byte[] data) throws InvalidDataSizeException {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        String message = buffer.toString();
        return new Message(message);
    }

}
