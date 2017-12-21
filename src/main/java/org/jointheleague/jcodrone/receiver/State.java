package org.jointheleague.jcodrone.receiver;

abstract class State {
    public State() {
    }

    public abstract StateMap call(Receiver receiver, byte data);
}
