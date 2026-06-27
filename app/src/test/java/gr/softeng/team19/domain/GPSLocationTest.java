package gr.softeng.team19.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.osmdroid.util.GeoPoint;

import java.time.LocalDateTime;

public class GPSLocationTest {

    private GPSLocation location;


    @Before
    public void setUp() throws Exception {
        location = new GPSLocation(38.2378, 23.7275);
    }
    @Test
    public void constructor_initializesCorrectly() {
        assertEquals((Double) 38.2378, location.getLatitude());
        assertEquals((Double) 23.7275, location.getLongitude());
        assertNotNull(location.getDetectionTime());

        // Νέο Assert: Έλεγχος αν το GeoPoint αρχικοποιήθηκε σωστά στον constructor
        assertNotNull(location.getPoint());
        assertEquals(38.2378, location.getPoint().getLatitude(), 0.0001);
        assertEquals(23.7275, location.getPoint().getLongitude(), 0.0001);
    }

    @Test
    public void setLatitude_updatesCorrectly() {
        location.setLatitude(38.0);
        assertEquals((Double) 38.0, location.getLatitude());

        // Νέο Assert: GeoPoint ενημερώθηκε αυτόματα
        assertEquals(38.0, location.getPoint().getLatitude(), 0.0001);
    }

    @Test
    public void setLongitude_updatesCorrectly() {
        location.setLongitude(40.0);
        assertEquals((Double) 40.0, location.getLongitude());

        // Νέο Assert: Έ GeoPoint ενημερώθηκε αυτόματα
        assertEquals(40.0, location.getPoint().getLongitude(), 0.0001);
    }

    @Test
    public void setPoint_updatesCorrectly() {
        GeoPoint newPoint = new GeoPoint(35.0, 25.0);
        location.setPoint(newPoint);
        assertEquals(newPoint, location.getPoint());
    }

    @Test
    public void setLatitude_updatesGeoPointEvenIfPointIsNull() {
        location.setPoint(null);
        location.setLatitude(34.5);

        assertNotNull(location.getPoint());
        assertEquals(34.5, location.getPoint().getLatitude(), 0.0001);
    }
    @Test
    public void setDetectionTime_updatesCorrectly(){
        LocalDateTime newTime = LocalDateTime.now().minusDays(1);
        location.setDetectionTime(newTime);
        assertEquals(newTime, location.getDetectionTime());
    }

    @Test
    public void distanceTo_returnsPositiveValue(){
        GPSLocation otherLocation = new GPSLocation(40.5678, 20.7890);
        double distance = location.distanceTo(otherLocation);
        assertTrue(distance > 0);
    }
    @Test
    public void testToString(){
        String output = location.toString();
        assertTrue(output.contains("Latitude"));
        assertTrue(output.contains("Longitude"));
        assertTrue(output.contains("Detected at"));
    }



}