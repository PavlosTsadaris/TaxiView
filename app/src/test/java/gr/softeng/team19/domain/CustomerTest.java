package gr.softeng.team19.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

public class CustomerTest {
    private GPSLocation pickupPoint1;
    private Address destination1;
    private TaxiRideRequest currentRequest;
    private TaxiDriver driver;
    private GPSLocation pickupPoint2;
    private Address destination2;
    private Customer customer;
    private TaxiBooking booking;


    @Before
    public void setUp() throws Exception{
         pickupPoint1 = new GPSLocation(11.1111,11.1111);
         pickupPoint2 = new GPSLocation(22.2222,22.2222);
         destination1 = new Address("Panepistimiou", "Athens", 12, 10564);
         destination2 = new Address("Street", "City", 2, 12345);


        driver = new TaxiDriver(
                "driver1", "pass", "takhs@email.com",
                "John","Doe","6981845678",
                LocalDate.of(1990,1,1),
                "1234",
                20.1,20.1,
                "Street","City",1,11111,
                "AAA1111","Car","Model"
        );
        customer = new Customer("username",
                "password",
                "email",
                "name",
                "surname",
                "6987980564",
                LocalDate.of(2005, 1, 1),
                "54769862349",
                22.2222,
                22.2222,
                "Panepistimiou",
                "Athens",
                10,
                10564);
        currentRequest = customer.callTaxi(destination1);
         booking = new TaxiBooking(pickupPoint1, destination1, customer, driver, currentRequest);
    }

    @Test
    public void  constructor_initializesCorrectly() {
        assertEquals("username", customer.getUserName());
        assertEquals("password", customer.getPassword());
        assertEquals("email", customer.getEmail());
        assertEquals("name", customer.getName());
        assertEquals("surname", customer.getSurname());
        assertEquals("6987980564",customer.getPhoneNumber());
        assertEquals(LocalDate.of(2005,1,1), customer.getBirthDate());
        assertEquals("54769862349", customer.getCreditCardNumber());
        assertEquals(Double.valueOf(22.2222), customer.getUserLocation().getLatitude());
        assertEquals(Double.valueOf(22.2222), customer.getUserLocation().getLongitude());
        assertEquals("Panepistimiou", customer.getAddress().getStreet());
        assertEquals("Athens",customer.getAddress().getCity());
        assertEquals(Integer.valueOf(10), customer.getAddress().getStreetNumber());
        assertEquals(Integer.valueOf(10564), customer.getAddress().getPostalCode());

    }

    @Test
    public void customerID_startsWithC() {
        String id = customer.getAccountID();
        assertTrue(id.startsWith("C"));
    }

    @Test
    public void customer_isanApplicationUser() {
        assertTrue(customer instanceof ApplicationUser);
    }


    @Test
    public void updateRideRequest() {

        TaxiRideRequest request2 = new TaxiRideRequest(pickupPoint2, destination2, customer);

        // Different id
        customer.updateRideRequest(request2);
        assertNotSame(request2, customer.getRideRequests().get(0));
        assertEquals(currentRequest, customer.getRideRequests().get(0));
        // Same id
        request2.setRequestID(currentRequest.getRequestID());
        customer.updateRideRequest(request2);
        assertSame(request2, customer.getRideRequests().get(0));

    }


    Address destination = new Address("Panepistimiou", "Athens", 12, 10564);
//
    @Test
    public void callTaxi() {


        // Check if the list has 2 elements (request2, currentrequest)
        assertEquals(2, customer.getRideRequests().size());
        assertSame(currentRequest, customer.getRideRequests().get(0));
    }

    @Test
    public void addBooking_behavesCorrectly() {


        assertEquals(1, customer.getBookingHistory().size());
        assertSame(booking, customer.getBookingHistory().get(0));

        customer.addBooking(booking);
        assertEquals(1, customer.getBookingHistory().size());
    }

    @Test
    public void addBooking(){


        // Add booking to customer
        customer.addBooking(booking);
        assertEquals(1, customer.getBookingHistory().size());
        assertTrue(customer.getBookingHistory().contains(booking));
        assertNull(customer.getBookingHistory().get(0).getDriverRating()); // No rating yet

        DriverRating rating = new DriverRating(booking);
        booking.setDriverRating(rating);
        customer.updateBooking(booking);
        assertTrue(customer.getBookingHistory().contains(booking));
        assertNotNull(customer.getBookingHistory().get(0).getDriverRating());

        TaxiBooking booking1 = new TaxiBooking(pickupPoint2, destination2, customer, driver,currentRequest);
        booking1.setStatus("RouteCompleted");
        customer.addBooking(booking1);
        assertEquals(2, customer.getBookingHistory().size());
        assertEquals("RouteCompleted",booking1.getStatus());
    }

    @Test
    public void updateBooking() {

        customer.updateBooking(booking);
        assertTrue(customer.getBookingHistory().contains(booking));
        //
        GPSLocation pickup1 = new GPSLocation(0.0, 0.0);
        destination = new Address("Chalkidos", "Chalkida", 10, 43456);
        Customer customer1 = new Customer( "username",
                "password",
                "email",
                "name",
                "surname",
                "6987980564",
                LocalDate.of(2005, 1, 1),
                "54769862349",
                22.2222,
                22.2222,
                "Panepistimiou",
                "Athens",
                10,
                10564);
        TaxiBooking booking1 = new TaxiBooking(pickup1, destination, customer1,driver,currentRequest);
        booking1.setBookingID("Another");
        assertFalse(customer.getBookingHistory().contains(booking1));

        // Route Completed
        booking.setStatus("RouteCompleted");
        customer.updateBooking(booking);
        assertTrue(customer.getBookingHistory().contains(booking));
        assertEquals(1, customer.getCompletedRides().intValue());

    }

}