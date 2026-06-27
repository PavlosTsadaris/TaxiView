package gr.softeng.team19.domain;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;

public class TaxiRideRequestTest {

    private TaxiRideRequest request;
    GPSLocation pickupPoint = new GPSLocation(11.1111,11.1111);
    Address destination = new Address("Panepistimiou", "Athens", 10, 10564);
    Customer customer = new Customer("username",
            "password",
            "email",
            "name",
            "surname",
            "6912345678",
            LocalDate.of(2005, 1, 1),
            "12345678",
            22.2222,
            22.2222,
            "Panepistimiou",
            "Athens",
            10,
            10564);
    TaxiDriver driver1;
    TaxiDriver driver2;
    TaxiDriver driver3;
    TaxiDriver driver4;
    @Before
    public void setUp() throws Exception{
        driver1 = new TaxiDriver(
                "driver1",
                "pass1",
                "driver1@test.com",
                "Driver",
                "One",
                "100000001",
                LocalDate.of(1990, 1, 1),
                "1111",
                11.1111,
                11.111,
                "Street 1",
                "City",
                1,
                1000,
                "AAA1111",
                "Car",
                "Model1"
        );

        driver2 = new TaxiDriver(
                "driver2",
                "pass2",
                "driver2@test.com",
                "Driver",
                "Two",
                "100000002",
                LocalDate.of(1990, 1, 2),
                "2222",
                11.1111,
                11.1111,
                "Street 2",
                "City",
                2,
                10000,
                "BBB2222",
                "Car",
                "Model2"
        );

        driver3 = new TaxiDriver(
                "driver3",
                "pass3",
                "driver3@test.com",
                "Driver",
                "Three",
                "100000003",
                LocalDate.of(1990, 1, 3),
                "3333",
                38.10,
                23.90,
                "Street 3",
                "City",
                3,
                10000,
                "CCC3333",
                "Car",
                "Model3"
        );

        driver4 = new TaxiDriver(
                "driver4",
                "pass4",
                "driver4@test.com",
                "Driver",
                "Four",
                "100000004",
                LocalDate.of(1990, 1, 4),
                "4444",
                11.1201,
                11.1111,
                "Street 4",
                "City",
                4,
                10000,
                "CCC4444",
                "Car",
                "Model4"
        );

        request = new TaxiRideRequest(pickupPoint, destination, customer);
    }

    @Test
    public void constructor_initializesCoreFieldsAndAddsToCustomer() {
        assertEquals(customer.getRideRequests().get(0).getRequestID(), request.getRequestID()); // Check if customer has the same request
        assertEquals("PENDING", request.getStatus());
        assertSame(destination, request.getDestination());
        assertNotNull(request.getPickupPoint());
        assertNotNull(request.getRequestTime());
        assertEquals(LocalDate.now(), request.getDate());
    }


    @Test(expected = IllegalArgumentException.class)
    public void chooseTaxi_invalidNegativeIndex_throwsException() {
        ArrayList<TaxiDriver> drivers = new ArrayList<>();
        drivers.add(driver1);
        request.findAllDrivers(drivers);

        request.chooseTaxi(-1);
    }

    @Test
    public void findAllDriversCheck() {
        ArrayList<TaxiDriver> drivers = new ArrayList<>();




        drivers.add(driver1); // Near = 0.0
        driver2.setAvailability(false);
        drivers.add(driver2); // not available but near = 0.0
        drivers.add(driver3); // Far ~3000km
        drivers.add(driver4); // Near ~1km

        ArrayList<TaxiDriver> allDrivers = request.findAllDrivers(drivers);
        assertEquals(2, allDrivers.size());

    }

    @Test
    public void chooseTaxi() {
        ArrayList<TaxiDriver> allDrivers = new ArrayList<>();
        allDrivers.add(driver1);
        allDrivers.add(driver2);
        allDrivers.add(driver3);
        allDrivers.add(driver4);

        request.findAllDrivers(allDrivers);

        TaxiDriver chosen = request.chooseTaxi(0);

        assertSame(driver1, chosen);

    }

//    @Test
//    public void acceptRequest() {
//    }

    @Test
    public void denyRequest() {
        request.denyRequest();
        assertEquals("DENIED", request.getStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void chooseTaxi_whenNoAvailableDrivers_throwsException() {

        ArrayList<TaxiDriver> emptyList = new ArrayList<>();

        request.findAllDrivers(emptyList);

        request.chooseTaxi(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void acceptRequest_withNullDriver_throwsException() {
        request.acceptRequest(null);
    }

    @Test
    public void acceptRequest() {
        TaxiDriver chosen = new TaxiDriver(
                "driver1",
                "pass1",
                "driver1@test.com",
                "Driver",
                "One",
                "100000001",
                LocalDate.of(1990, 1, 1),
                "1111",
                11.1111,
                11.111,
                "Street 1",
                "City",
                1,
                1000,
                "AAA1111",
                "Car",
                "Model1"
        );

        TaxiBooking booking = request.acceptRequest(chosen);
        assertEquals("ACCEPTED", request.getStatus());
    }


}