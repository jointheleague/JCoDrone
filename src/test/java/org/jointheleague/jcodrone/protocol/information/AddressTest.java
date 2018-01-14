package org.jointheleague.jcodrone.protocol.information;

import org.jointheleague.jcodrone.Address;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class AddressTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseDecimal() {
        Address address = Address.parse("01:02:03:04:05:06");
        byte[] bytes = address.toArray();

        assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 6}, bytes);
    }

    @Test
    public void parseUpperHex() {
        Address address = Address.parse("0A:0B:0C:0D:0E:0F");
        byte[] bytes = address.toArray();

        assertArrayEquals(new byte[]{10, 11, 12, 13, 14, 15}, bytes);
    }

    @Test
    public void parseLowerHex() {
        Address address = Address.parse("0a:0b:0c:0d:0e:0f");
        byte[] bytes = address.toArray();

        assertArrayEquals(new byte[]{10, 11, 12, 13, 14, 15}, bytes);
    }

    @Test
    public void parseFailure() throws NumberFormatException {
        thrown.expect(NumberFormatException.class);
        thrown.expectMessage("For input string: \"No\"");
        Address address = Address.parse("No:t :va:li:d!:!!");
        byte[] bytes = address.toArray();
    }

    @Test
    public void getStringDecimal() {
        Address address = new Address(new byte[]{1, 2, 3, 4, 5, 6});
        assertEquals("01:02:03:04:05:06", address.toString());
    }

    @Test
    public void getStringHex() {
        Address address = new Address(new byte[]{(byte) 0xDE, (byte) 0xAD, (byte) 0xBE, (byte) 0xEF, (byte) 0xCA, (byte) 0xFE});
        assertEquals("DE:AD:BE:EF:CA:FE", address.toString());
    }
}