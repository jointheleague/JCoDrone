package org.jointheleague.jcodrone.receiver;

import org.jointheleague.jcodrone.protocol.DataType;
import org.jointheleague.jcodrone.protocol.Header;

import java.nio.ByteBuffer;

import static java.time.Instant.now;

public class Receiver {
    private Section sectionOld;
    private Section section;
    private int index;
    private Header header;
    private long timeReceiveStart;
    private long timeReceiveComplete;
    private ByteBuffer buffer;
    private int crc16received;
    private int crc16calculated;
    private String message;
    private StateLoading state;

    public Receiver() {
        this.state = StateLoading.READY;
        this.sectionOld = Section.END;
        this.section = Section.START;
        this.index = 0;

        this.header = new Header();
        this.timeReceiveStart = 0;
        this.timeReceiveComplete = 0;

        this.crc16received = 0;
        this.crc16calculated = 0;
    }

    public StateLoading call(byte[] data) {
        long now = now().getEpochSecond();

        this.message = null;

        // First Step
        if (this.state == StateLoading.FAILURE) {
            this.state = StateLoading.READY;
        }

        // Second Step
        if (this.state == StateLoading.READY) {
            this.section = Section.START;
            this.index = 0;
        } else if (this.state == StateLoading.RECEIVING) {
            if ((this.timeReceiveStart + 600) < now) {
                this.state = StateLoading.FAILURE;
                this.message = "Error / Receiver / StateLoading.Receiving / Time over.";
                return this.state;
            }
        } else if (this.state == StateLoading.LOADED) {
            return this.state;
        }

        // Third Step
        if (this.section != this.sectionOld) {
            this.index = 0;
            this.sectionOld = this.section;
        }

        // Fourth Step
        if (this.section == Section.START) {
            if (this.index == 0) {
                if (data[0] == 0x0A) {
                    this.state = StateLoading.RECEIVING;
                } else {
                    this.state = StateLoading.FAILURE;
                    return this.state;
                }
                this.timeReceiveStart = now;
            } else if (this.index == 1) {
                if (data[0] != 0x55) {
                    this.state = StateLoading.FAILURE;
                    return this.state;
                } else {
                    this.section = Section.HEADER;
                }
            } else {
                this.state = StateLoading.FAILURE;
                this.message = "Error / Receiver / Section.Start / Index over.";
                return this.state;
            }

        } else if (this.section == Section.HEADER) {

            if (this.index == 0) {
                this.header = new Header();

                try {
                    this.header.setDataType(DataType.fromByte(data));
                } catch (Exception e) {
                    this.state = StateLoading.FAILURE;
                    this.message = String.format(
                            "Error / Receiver / Section.Header / DataType Error. [%02X]",
                            data[0]);
                    return this.state;
                }

                this.crc16calculated = CRC16.calc(data, 0);

            } else if (this.index == 1) {
                this.header.setLength(data);
                this.crc16calculated = CRC16.calc(data, this.crc16calculated);

                if (this.header.getLength() > 128) {
                    this.state = StateLoading.FAILURE;
                    this.message = String.format(
                            "Error / Receiver / Section.Header / Data length is longer than 128. %d",
                            this.header.getLength());
                    return this.state;
                } else if (this.header.getLength() == 0) {
                    this.section = Section.END;
                } else {
                    this.section = Section.DATA;
                    this.buffer = ByteBuffer.allocate(2048);
                }

            } else {
                this.state = StateLoading.FAILURE;
                this.message = "Error / Receiver / Section.Header / Index over.";
                return this.state;
            }

        } else if (this.section == Section.DATA) {
            this.buffer.put(data);
            this.crc16calculated = CRC16.calc(data, this.crc16calculated);

            if (this.index == (this.header.getLength() - 1)) {
                this.section = Section.END;
            }

        } else if (this.section == Section.END) {
            if (this.index == 0) {
                int result = 0;
                for (int i = 0; i < 4; i++) {
                    result = (result << 8) - Byte.MIN_VALUE + (int) data[i];
                }
                this.crc16received = result;

            } else if (this.index == 1) {
                this.crc16received = (data[0] << 8) | this.crc16received;

                if (this.crc16received == this.crc16calculated) {
                    this.timeReceiveComplete = now;
                    this.state = StateLoading.LOADED;
                    this.message = String.format(
                            "Success / Receiver / Section.End / Receive complete / %s / [receive: %04X]",
                            this.header.getDataType(), this.crc16received);
                    return this.state;
                } else {
                    this.state = StateLoading.FAILURE;
                    this.message = String.format(
                            "Error / Receiver / Section.End / CRC Error / %s / [receive: %04X, calculate: %04X]",
                            this.header.getDataType(), this.crc16received, this.crc16calculated);
                    return this.state;
                }

            } else {
                this.state = StateLoading.FAILURE;
                this.message = "Error / Receiver / Section.End / Index over.";
                return this.state;
            }

        } else {
            this.state = StateLoading.FAILURE;
            this.message = "Error / Receiver / Section over.";
            return this.state;
        }

        // Fifth Step
        if (this.state == StateLoading.RECEIVING) {
            this.index += 1;
        }

        return this.state;
    }

    public void checked() {
        this.state = StateLoading.READY;
    }
}
