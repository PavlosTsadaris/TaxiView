package gr.softeng.team19.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class DriverRatingTest {

    private DriverRating rating;
    private TaxiBooking mockBooking;


    @Before
    public void setUp() throws Exception{

        GPSLocation pickup = new GPSLocation(22.2222, 22.2222);
        Address destination = new Address("Panepistimiou", "Athens", 12, 10564);

        Customer customer = new Customer("user", "pass", "email@test.com", "Name", "Surname", "6900000000",
                java.time.LocalDate.of(2000, 1, 1), "123456", 22.2, 22.2, "Street", "City", 1, 10000);

        TaxiDriver driver = new TaxiDriver("driver", "pass", "dr@test.com", "DName", "DSurname", "6911111111",
                java.time.LocalDate.of(1990, 1, 1), "987654", 22.2, 22.2, "DStreet", "DCity", 2, 20000, "AAA1111", "Car", "Model");

        TaxiRideRequest mockRequest = new TaxiRideRequest(pickup, destination, customer);

        mockBooking = new TaxiBooking(pickup, destination, customer, driver, mockRequest);

        rating = new DriverRating(mockBooking);

    }

    @Test
    public void constructor_initializesCorrectly() {
        assertNotNull(rating.getRatingID());
        assertTrue(rating.getRatingID().startsWith("R"));

        assertEquals(0.0, rating.getAverageRating(), 0.001);
        assertTrue(rating.getServiceRatings().isEmpty());
        assertEquals("", rating.getCustomerComment());
    }

    @Test
    public void addServiceRating_updatesListAndAverage() {
        rating.addServiceRating(5.0);
        rating.addServiceRating(3.0);

        assertEquals(2, rating.getServiceRatings().size());
        assertEquals(Double.valueOf(5.0), rating.getServiceRatings().get(0));
        assertEquals(Double.valueOf(3.0), rating.getServiceRatings().get(1));

        assertEquals(4.0, rating.getAverageRating(), 0.001); // (5+3)/2
    }

    @Test
    public void addServiceRating_NegativeValue() {

        assertFalse(rating.addServiceRating(-1.0));
    }


    @Test
    public void fillDriverRating_OnlyValid() {
        ArrayList<Double> list = new ArrayList<>();
        list.add(5.0);
        list.add(-3.0);
        list.add(6.0);
        list.add(2.0);

        rating.fillDriverRating(list, "personal_comment");

        assertEquals(2.0, rating.getServiceRatings().size());
        assertEquals("personal_comment", rating.getCustomerComment());
        assertEquals(3.5, rating.getAverageRating(), 0.001);
    }

    @Test
    public void fillDriverRating_handlesEmptyList() {
        ArrayList<Double> list = new ArrayList<>();

        rating.fillDriverRating(list, "empty");

        assertEquals(0, rating.getServiceRatings().size());
        assertEquals("empty", rating.getCustomerComment());
        assertEquals(Double.valueOf(0.0), rating.getAverageRating());
    }

}