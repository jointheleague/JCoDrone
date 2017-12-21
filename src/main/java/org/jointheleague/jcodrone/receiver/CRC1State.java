package org.jointheleague.jcodrone.receiver;

import static java.time.Instant.now;

public class CRC1State extends State {
    @Override
    public StateMap call(Receiver receiver, byte data) {
        receiver.setCRC1(data);
        return StateMap.CRC2;
    }
}
