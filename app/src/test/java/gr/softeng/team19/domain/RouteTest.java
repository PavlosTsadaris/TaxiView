package gr.softeng.team19.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.time.LocalTime;



public class RouteTest {


    private Route route;
    private TaxiBooking mockBooking;

    @Before
    public void setUp() throws Exception{


        GPSLocation pickup = new GPSLocation(0.0, 0.0);
        Address destination = new Address("Panepistimiou", "Athens", 12, 10564);

        Customer customer = new Customer("user", "pass", "email@test.com", "Name", "Surname", "6900000000",
                java.time.LocalDate.of(2000, 1, 1), "123456", 22.2, 22.2, "Street", "City", 1, 10000);

        TaxiDriver driver = new TaxiDriver("driver", "pass", "dr@test.com", "DName", "DSurname", "6911111111",
                java.time.LocalDate.of(1990, 1, 1), "987654", 22.2, 22.2, "DStreet", "DCity", 2, 20000, "AAA1111", "Car", "Model");

        TaxiRideRequest mockRequest = new TaxiRideRequest(pickup, destination, customer);

        mockBooking = new TaxiBooking(pickup, destination, customer, driver, mockRequest);

        route = new Route(pickup, mockBooking);
    }

    @Test
    public void constructor_initializesCorrectly() {

        String id = route.getRouteID();
        assertTrue(id.startsWith("R"));
        assertNotNull(route.getPickupPoint());
        assertNotNull(route.getStartTime());
    }

    @Test
    public void findTotalTime_returnsCorrectMin() {

        // Set end and start times
        LocalTime start = LocalTime.of(10, 0);
        LocalTime end = LocalTime.of(10, 30);

        double minutes = route.findTotalTime(end, start);

        assertEquals(30.0, minutes, 0.001);
    }


    @Test
    public void findTotalDistance_returnsCorrectKm() {

        // (0.0, 0.0) to (0.0, 0.1) ≈ 11km on Earth
        double expectedDistanceKm = 11.0;
        double tolerance = 0.5;           // ±0.5 km margin

        double distance = route.findTotalDistance(0.0, 0.1);

        assertEquals( expectedDistanceKm, distance, tolerance);
    }

    @Test
    public void findRouteCost_correctUpdate() {
        //Set an end time
        LocalTime end = route.getStartTime().plusMinutes(20);

        double result = route.findRouteCost(end, 0.0, 0.1);

        // Check if the alterations in the function were made
        assertNotNull(route.getEndTime());
        assertTrue(route.getTotalTime() > 0);
        assertTrue(route.getTotalDistance() > 0);

        //Check classes total Cost
        assertEquals(result, route.getCost(), 0.001);
    }

    @Test
    public void calculateCost_returnsCorrect() {
        // Calculate with numbers>0
        double cost = route.calculateCost(10, 2);

        assertEquals(11.0, cost, 0.001);
    }

    @Test
    public void calculateCost_appliesBaseFare() {
        // 0 distance, 0 time --> Cost = Base Fare
        double cost = route.calculateCost(0, 0);

        assertEquals(5.2, cost, 0.001);
    }

}