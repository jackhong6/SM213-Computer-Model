package arch.sm213.machine.student;

import arch.sm213.machine.student.MainMemory;
import machine.AbstractMainMemory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainMemoryTest {

    MainMemory memory;

    @Before
    public void setUp() {
        memory = new MainMemory(12);
    }

    @Test
    public void testIsAccessAlignedTrue() {
        assertTrue(memory.isAccessAligned(0,4));
        assertTrue(memory.isAccessAligned(4,4));
        assertTrue(memory.isAccessAligned(12,4));
        assertTrue(memory.isAccessAligned(13,1));
        assertTrue(memory.isAccessAligned(12,6));

        assertFalse(memory.isAccessAligned(1,4));
        assertFalse(memory.isAccessAligned(2,4));
        assertFalse(memory.isAccessAligned(7,4));
        assertFalse(memory.isAccessAligned(13,7));
        assertFalse(memory.isAccessAligned(5,8));
    }

    @Test
    public void testBytesToInteger() {
        byte b0 = 0;
        byte b80 = (byte) 0x80;
        byte bff = (byte) 0xff;
        byte b1 = 1;

        assertEquals(memory.bytesToInteger(b0,b0,b0,b1),1);
        assertEquals(memory.bytesToInteger(bff,b1,b1,b1), -16711423);
        assertEquals(memory.bytesToInteger(b0,b0,b80,b0), 32768);
        assertEquals(memory.bytesToInteger(b0,b0,b0,b80), 128);

    }

    @Test
    public void testIntegerToBytes() {
        byte b0 = 0;
        byte b80 = (byte) 0x80;
        byte bff = (byte) 0xff;
        byte b1 = 1;

        assertEquals(memory.integerToBytes(1)[0], b0);
        assertEquals(memory.integerToBytes(1)[1], b0);
        assertEquals(memory.integerToBytes(1)[2], b0);
        assertEquals(memory.integerToBytes(1)[3], b1);

        assertEquals(memory.integerToBytes(-16711423)[0], bff);
        assertEquals(memory.integerToBytes(-16711423)[1], b1);
        assertEquals(memory.integerToBytes(-16711423)[2], b1);
        assertEquals(memory.integerToBytes(-16711423)[3], b1);

        assertEquals(memory.integerToBytes(32768)[0], b0);
        assertEquals(memory.integerToBytes(32768)[1], b0);
        assertEquals(memory.integerToBytes(32768)[2], b80);
        assertEquals(memory.integerToBytes(32768)[3], b0);

        assertEquals(memory.integerToBytes(128)[0], b0);
        assertEquals(memory.integerToBytes(128)[1], b0);
        assertEquals(memory.integerToBytes(128)[2], b0);
        assertEquals(memory.integerToBytes(128)[3], b80);
    }

    @Test
    public void testGet() throws AbstractMainMemory.InvalidAddressException {
        int memorySize = memory.length();
        byte[] bytes = new byte[memorySize];
        for (int i = 0; i < memorySize; i++) {
            bytes[i] = (byte) i;
        }
        memory.set(0,bytes);
        byte[] myBytes = memory.get(0,memorySize);

        assertEquals(myBytes[0],0);
        assertEquals(myBytes[1],1);
        assertEquals(myBytes[2],2);
        assertEquals(myBytes[3],3);
        assertEquals(myBytes[4],4);
        assertEquals(myBytes[5],5);
        assertEquals(myBytes[6],6);
        assertEquals(myBytes[7],7);
        assertEquals(myBytes[8],8);
        assertEquals(myBytes[9],9);
        assertEquals(myBytes[10],10);
        assertEquals(myBytes[11],11);
    }

    @Test
    public void testGetUpperBoundary() throws AbstractMainMemory.InvalidAddressException {
        int memorySize = memory.length();
        byte[] myBytes = new byte[1];
        myBytes[0] = 7;
        memory.set(memorySize - 1, myBytes);
        assertEquals(memory.get(memorySize - 1, 1)[0], 7);
    }

    @Test (expected = AbstractMainMemory.InvalidAddressException.class)
    public void testGetExceptionAddressTooSmall() throws AbstractMainMemory.InvalidAddressException {
        memory.get(-1,4);
    }

    @Test (expected = AbstractMainMemory.InvalidAddressException.class)
    public void testGetExceptionAddressTooBig() throws AbstractMainMemory.InvalidAddressException {
        memory.get(memory.length(),1);
    }

    @Test
    public void testSet() throws AbstractMainMemory.InvalidAddressException {
        byte[] bytes = new byte[3];
        for (int i = 0; i < 3; i++) {
            bytes[i] = (byte) i;
        }
        memory.set(0,bytes);
        memory.set(6,bytes);
        memory.set(9,bytes);

        assertEquals(memory.get(0,3)[0],0);
        assertEquals(memory.get(0,3)[1],1);
        assertEquals(memory.get(0,3)[2],2);

        assertEquals(memory.get(6,3)[0],0);
        assertEquals(memory.get(6,3)[1],1);
        assertEquals(memory.get(6,3)[2],2);

        assertEquals(memory.get(9,3)[0],0);
        assertEquals(memory.get(9,3)[1],1);
        assertEquals(memory.get(9,3)[2],2);

    }

    @Test (expected = AbstractMainMemory.InvalidAddressException.class)
    public void testSetExceptionAddressTooSmall() throws AbstractMainMemory.InvalidAddressException {
        byte[] myBytes = new byte[1];
        memory.set(-1,myBytes);
    }

    @Test (expected = AbstractMainMemory.InvalidAddressException.class)
    public void testSetExceptionAddressTooBig() throws AbstractMainMemory.InvalidAddressException {
        byte[] myBytes = new byte[1];
        memory.set(memory.length(),myBytes);
    }
}