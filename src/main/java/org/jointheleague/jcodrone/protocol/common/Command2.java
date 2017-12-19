package org.jointheleague.jcodrone.protocol.common;

public class Command2 extends CommandN {

    private static final int NUMBER_OF_COMMANDS = 2;

    public Command2(Command[] commands) {
        super(commands);
    }

    @Override
    public int getNumberOfCommands() {
        return NUMBER_OF_COMMANDS;
    }
}
