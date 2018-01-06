package org.jointheleague.jcodrone.receiver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jointheleague.jcodrone.CoDrone;
import org.jointheleague.jcodrone.Internals;
import org.jointheleague.jcodrone.Link;
import org.jointheleague.jcodrone.Sensors;
import org.jointheleague.jcodrone.protocol.DataType;
import org.jointheleague.jcodrone.protocol.Serializable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

public class Receiver {
    static Logger log = LogManager.getLogger(Receiver.class);
    public final CoDrone coDrone;
    private final Link link;
    private final Sensors sensors;
    private final Internals internals;

    private StateMap state;
    private ByteBuffer buffer;
    private int crc16received;
    private int crc16calculated;
    private DataType dataType;
    private Timer timer;


    public Receiver(CoDrone coDrone, Link link, Sensors sensors, Internals internals) {
        this.coDrone = coDrone;
        this.link = link;
        this.sensors = sensors;
        this.internals = internals;
        this.state = StateMap.START1;
    }

    public void call(byte data) {
//        if (log.isDebugEnabled()) {
//            log.debug("Byte received {}", String.format("%02X", data));
//        }
        if (this.state == null || this.state.getState() == null) {
            log.warn("Message received before states ready.");
            return;
        }
        this.state = this.state.getState().call(this, data);
        if (!(this.state == StateMap.CRC1) && !(this.state == StateMap.CRC2)) {
            this.crc16calculated = CRC16.calc(data, this.crc16calculated);
        }
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public void setCrc16calculated(int crc16calculated) {
        this.crc16calculated = crc16calculated;
    }

    public void allocateDataBuffer(byte length) {
        this.buffer = ByteBuffer.allocate(length);
    }

    public boolean putData(byte data) {
        this.buffer.put(data);
        return (this.buffer.remaining() == 0);
    }

    public void setCRC1(byte CRC1) {
        this.crc16received = CRC1;
    }

    public void setCRC2(byte CRC2) {
        this.crc16received = (CRC2 << 8) | this.crc16received;
    }

    public boolean isCRCValid() {
        // TODO Fix CRC calculation
        return true;
        //return (crc16received == crc16calculated);
    }

    public void handleMessage() {
        Class<? extends Serializable> messageClass = dataType.getMessageClass();
        Method m = null;
        Serializable message;
        timer.cancel();
        try {
            m = messageClass.getMethod("parse", byte[].class);
            message = (Serializable) m.invoke(dataType.getMessageClass(), buffer.array());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Data type {} can not be parsed.", dataType, e);
            return;
        }
        message.handle(coDrone, link, sensors, internals);
    }

    public void startTimer() {
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (state != StateMap.START1) {
                    log.error("Message timeout during receive.");
                    state = StateMap.START1;
                }
            }
        }, 600);
    }

    public int getCrc16calculated() {
        return crc16calculated;
    }
}
