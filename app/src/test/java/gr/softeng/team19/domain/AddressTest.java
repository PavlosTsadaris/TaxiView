package gr.softeng.team19.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AddressTest {


    private Address address;

    @Before
    public void setUp() throws Exception{
        address = new Address("Panepistimiou", "Athens", 10, 11234);
    }

    @Test
    public void constructor_initializesCorrectly() {
        assertEquals("Panepistimiou", address.getStreet());
        assertEquals("Athens", address.getCity());
        assertEquals(Integer.valueOf(10), address.getStreetNumber());
        assertEquals(Integer.valueOf(11234), address.getPostalCode());
    }

    @Test
    public void testToString() {
        String output = address.toString();
        assertTrue(output.contains("Panepistimiou"));
        assertTrue(output.contains("10"));
        assertTrue(output.contains("Athens"));
        assertTrue(output.contains("11234"));
    }

    @Test
    public void setStreet_updatesCorrectly() {
        address.setStreet("Patision");
        assertEquals("Patision", address.getStreet());
    }

    @Test
    public void constructor_allowsNegativeStreetNumber() {
        Address a = new Address("Kifisias", "Athens", -5, 12345);
        assertEquals(Integer.valueOf(-5), a.getStreetNumber());
    }

    @Test
    public void setPostalCode_acceptsNegativeValue() {
        address.setPostalCode(-11143);
        assertEquals(Integer.valueOf(-11143), address.getPostalCode());
    }
}