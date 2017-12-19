package org.jointheleague.jcodrone.protocol;

public class InvalidDataSizeException extends Exception {
    public InvalidDataSizeException(int expected, int received) {
        super(
                String.format(
                        "Invalid message data size, expected: %d, received: %d",
                        expected,
                        received)
        );
    }
}
