package org.jointheleague.jcodrone;

import java.security.InvalidParameterException;

public class InvalidMessageException extends InvalidParameterException {
    public InvalidMessageException(String message) {
        super(message);
    }
}
