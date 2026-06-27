package gr.softeng.team19.domain;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import java.time.LocalDate;
import java.util.ArrayList;

public class TaxiBookingTest {
    private Customer customer;
    private TaxiDriver taxiDriver;
    private GPSLocation pickup;
    private Address destination;
    private TaxiBooking booking;

    private TaxiRideRequest request;


    @Before
    public void setUp() throws Exception{



        pickup = new GPSLocation(0.0, 0.0);
        destination = new Address("Chalkidos", "Chalkida", 10, 43456);

        customer = new Customer(
                "newUser", "pass", "user@mail.com",
                "Antonis", "Koulouris", "6954321456",
                LocalDate.of(2005, 1, 1),
                "87654321",
                37.9, 23.7,
                "Patision", "Athens", 23, 34521
        );

        taxiDriver = new TaxiDriver(
                "driverUser", "1234", "d@mail.com",
                "Nikos", "Papadopoulos", "6900000000",
                LocalDate.of(1990, 1, 1),
                "12345678",
                37.9, 23.7,
                "Akadimias", "Athens", 10, 10564,
                "ΙΒΧ1234", "Toyota", "Corolla"
        );
        request = new TaxiRideRequest(pickup, destination, customer);

        booking = new TaxiBooking(pickup, destination, customer, taxiDriver, request);
    }


    @Test
    public void constructor_initializesCorrectly() {

        assertNotNull(booking.getBookingID());
        assertEquals("WaitingForCustomer", booking.getStatus());
        assertEquals(pickup, booking.getPickupPoint());
        assertEquals(destination, booking.getDestination());
        assertEquals(customer, booking.getCustomer());
        assertEquals(taxiDriver, booking.getTaxiDriver());
        assertNull(booking.getDriverRating());

        assertNotNull(booking.getBookingTime());
        assertNotNull(booking.getDate());
    }


    @Test
    public void bookingID_startsWithB() {
        String id = booking.getBookingID();
        assertTrue(id.startsWith("B"));
    }

   @Test
   public void testDateIsToday() {


       TaxiBooking booking = new TaxiBooking(pickup, destination, customer, taxiDriver, request);

       assertEquals(LocalDate.now(), booking.getDate());
   }

    @Test
    public void startRoute_setsStatusAndNewRoute() {
        Route route = booking.startRoute();

        assertEquals("OngoingRoute", booking.getStatus());
        assertNotNull(route);
        assertEquals(pickup, route.getPickupPoint());


        // Check Customer booking updated in history
        assertTrue(customer.getBookingHistory().contains(booking));

        // Check Driver booking updated in history
        assertTrue(taxiDriver.getBookingHistory().contains(booking));
    }

    @Test
    public void endRoute_updateStatus_ReturnCost() {
        Route r = booking.startRoute();

        double cost = booking.endRoute(r, 0.0, 0.1);

        assertEquals("WaitingForPayment", booking.getStatus());
        assertTrue(cost > 0);

        // Customer +1 ride
        assertEquals(1, (int) customer.getCompletedRides());

        // Driver +1 ride
        assertEquals(1, (int) taxiDriver.getCompletedRides());

        // Customer-TaxiDriver has updated booking

        assertTrue(customer.getBookingHistory().contains(booking));
        assertTrue(taxiDriver.getBookingHistory().contains(booking));
    }

    @Test
    public void payRoute_CashPayment_updateCorrectly() {

        booking.endRoute(booking.startRoute(),0.0, 0.1);
        Payment p = booking.payRoute(20.0, Payment.PaymentMethod.PayByCash);

        assertNotNull(p);
        assertEquals(20.0, p.getAmount(), 0.001);
        assertEquals("PaymentCompleted", p.getStatus());

        // Booking should now be completed
        assertEquals("BookingCompleted", booking.getStatus());
    }

    @Test
    public void payRoute_cardPayment_updateCorrectly() {

        booking.endRoute(booking.startRoute(),0.0, 0.1);
        Payment p = booking.payRoute(20.0, Payment.PaymentMethod.PayByCard);

        assertNotNull(p);
        assertEquals(20.0, p.getAmount(), 0.001);
        assertEquals("ConnectingWithBank", p.getStatus());

        assertEquals("WaitingForPayment", booking.getStatus());

    }

    @Test
    public void createEvaluation() {

        ArrayList<Double> serviceRatings = new ArrayList<>();
        serviceRatings.add(5.0);
        serviceRatings.add(4.0);

        booking.createEvaluation(serviceRatings, "Great driver!", booking);

        // Booking should have a DriverRating object
        assertNotNull(booking.getDriverRating());
        assertEquals(4.5, booking.getDriverRating().getAverageRating(), 0.001);

        // Customer must store the rating
        assertEquals(1, customer.getDriverRatings().size());

        // Driver’s average rating should update to 4.5 (first booking)
        assertEquals(4.5, taxiDriver.getAverageRating(), 0.001);


        assertTrue(taxiDriver.getBookingHistory().contains(booking));
        assertTrue(customer.getBookingHistory().contains(booking));

    }

}
