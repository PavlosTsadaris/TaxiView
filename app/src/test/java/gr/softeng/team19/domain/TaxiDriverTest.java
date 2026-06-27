package gr.softeng.team19.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.time.LocalDate;
import java.util.ArrayList;


public class TaxiDriverTest {

    private TaxiDriver driver;
    private Customer customer;
    private GPSLocation pickup;
    private Address destination;
    private TaxiRideRequest request;

    private TaxiBooking booking;



    @Before
    public void setUp() throws Exception{

        pickup = new GPSLocation(0.0, 0.0);
        destination = new Address("Chalkidos", "Chalkida", 10, 43456);
        driver = new TaxiDriver(
                "driverUser", "1234", "makhsaritos@email.com",
                "Makhs", "Aritos", "123123123",
                LocalDate.of(2005, 3, 4),
                "3425",
                37.9, 23.7,
                "Akadimias", "Athens", 10, 10564,
                "XYZ1224", "Toyota", "Corolla"
        );

        customer = new Customer("user1", "pass123",
                "user1@email.com", "DImitris", "Mamalakhs",
                "698273434", LocalDate.of(1990,5,5),"3546",
                37.9856,98.5087, "Street2",
                "Athens", 5, 13234 );
        request = new TaxiRideRequest(pickup, destination, customer);

         booking = new TaxiBooking(pickup,destination,
                customer, driver, request);
    }

    @Test
    public void availability_changesCorrectly() {
        assertTrue(driver.getAvailability());
        driver.setAvailability(false);
        assertFalse(driver.getAvailability());
    }

    @Test
    public void constructor_initializesCorrectly(){
        assertEquals("driverUser", driver.getUserName());
        assertEquals("1234", driver.getPassword());
        assertEquals("makhsaritos@email.com", driver.getEmail());
        assertEquals("Makhs", driver.getName());
        assertEquals("Aritos", driver.getSurname());
        assertEquals("123123123", driver.getPhoneNumber());
        assertEquals(LocalDate.of(2005, 3, 4), driver.getBirthDate());
        assertEquals("3425", driver.getCreditCardNumber());
        assertEquals("XYZ1224", driver.getVehicle().getLisencePlate());
        assertTrue(driver.getAvailability());
        assertNotNull(driver.getRequiredDocuments());
    }
    @Test
    public void addDocument_updatesCorrectly(){
        Document doc = new Document(Document.DocumentType.drivingLicense, driver);
        driver.setNewDocument(doc);
        assertTrue(driver.getRequiredDocuments().contains(doc));
    }

    @Test
    public void addBooking_happensInConstructor(){

        assertEquals(0.0, driver.getAverageRating(),0.001);


    }

    @Test
    public void updateBooking_withRating_updatesAverage(){

        DriverRating rating = new DriverRating(booking);

        ArrayList<Double> serviceRatings = new ArrayList<>();
        serviceRatings.add(4.0);
        serviceRatings.add(5.0);
        rating.fillDriverRating(serviceRatings, "W Driver");

        booking.setDriverRating(rating);

        driver.updateBooking(booking);
        assertEquals(4.5, driver.getAverageRating(), 0.001);
    }


    @Test
    public void updateBooking_withWrongID(){
        booking.setBookingID("WrongID");

        driver.updateBooking(booking);

    }
    @Test
    public void getFullDetails_containsAllInfo(){

        Document doc = new Document(Document.DocumentType.drivingLicense, driver);
        driver.setNewDocument(doc);

        DriverRating rating = new DriverRating(booking);
        ArrayList<Double> ratings = new ArrayList<>();
        ratings.add(4.0);
        ratings.add(5.0);
        rating.fillDriverRating(ratings, "Good");
        booking.setDriverRating(rating);
        driver.addBooking(booking);

        String details = driver.getFullDetails();
        assertTrue(details.contains("Vehicle"));
        assertTrue(details.contains("Required Documents"));
        assertTrue(details.contains("Average Rating"));
        assertTrue(details.contains("Availability"));

    }

    @Test
    public void updateDriverRatings_updatesCorrectly() {

        // Null rating
        driver.updateDriverRatings(null);
        assertEquals(0, driver.getDriverRatings().size());

        // Non-null rating
        DriverRating rating1 = new DriverRating(booking);
        driver.updateDriverRatings(rating1);
        assertEquals(1, driver.getDriverRatings().size());

        // Replace
        DriverRating rating2 = new DriverRating(booking);
        rating2.setRatingID(rating1.getRatingID());
        driver.updateDriverRatings(rating2);
        assertEquals(1, driver.getDriverRatings().size());
    }

    @Test
    public void addDriverRatings_updatesCorrectly() {
        DriverRating rating1 = new DriverRating(booking);
        driver.addDriverRatings(rating1);
        assertEquals(1, driver.getDriverRatings().size());


        driver.addDriverRatings(rating1);
        assertEquals(1, driver.getDriverRatings().size());
        DriverRating rating2 = new DriverRating(booking);
        driver.addDriverRatings(rating2);
        assertEquals(2, driver.getDriverRatings().size());
    }


}

