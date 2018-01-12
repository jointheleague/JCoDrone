package org.jointheleague.jcodrone;

@SuppressWarnings("WeakerAccess")
public class MessageNotSentException extends Exception {
    public MessageNotSentException(String message) {
        super(message);
    }
}
