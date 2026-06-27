package gr.softeng.team19.domain;

import static org.junit.Assert.*;

import org.junit.Test;
public class GlobalIDTest {
//Tests gia na doume an to prefix einai swsto
    @Test
    public void testUserIDGeneration(){
        String id1 = GlobalID.getNextUserID();
        String id2 = GlobalID.getNextUserID();
        assertTrue(id1.startsWith("C"));
        assertTrue(id2.startsWith("C"));
        assertNotEquals(id1, id2);
    }
    @Test
    public void testRouteIDGeneration(){
        String id3 = GlobalID.getNextRouteID();
        String id4 = GlobalID.getNextRouteID();
        assertTrue(id3.startsWith("R"));
        assertTrue(id4.startsWith("R"));
        assertNotEquals(id3, id4);
    }

}