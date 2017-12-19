package org.jointheleague.jcodrone.protocol.common;

public class Command3 extends CommandN {

    private static final int NUMBER_OF_COMMANDS = 3;

    public Command3(Command[] commands) {
        super(commands);
    }

    @Override
    public int getNumberOfCommands() {
        return NUMBER_OF_COMMANDS;
    }
}
