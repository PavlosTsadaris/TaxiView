package gr.softeng.team19.view.customer.evaluation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import gr.softeng.team19.domain.DriverRating;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.memorydao.DriverRatingDAOMemory;
import gr.softeng.team19.memorydao.MemoryInitializer;
import gr.softeng.team19.memorydao.TaxiBookingDAOMemory;

/**
 * Unit tests for EvaluationPresenter to verify rating submission and validation.
 */
public class EvaluationPresenterTest {

    private EvaluationPresenter presenter;
    private EvaluationViewStub viewStub;
    private TaxiBookingDAOMemory bookingDAO;
    private DriverRatingDAOMemory ratingDAO;
    private TaxiBooking testBooking;

    /**
     * Initializes test data and mock objects before each test execution.
     */
    @Before
    public void setUp() {
        MemoryInitializer.prepareData();
        bookingDAO = new TaxiBookingDAOMemory();
        ratingDAO = new DriverRatingDAOMemory();
        viewStub = new EvaluationViewStub();

        List<TaxiBooking> allBookings = bookingDAO.findAll();
        Assert.assertFalse("DAO should not be empty", allBookings.isEmpty());
        testBooking = allBookings.get(0);

        // Immediate scheduler for synchronous task execution
        EvaluationPresenter.Scheduler immediateScheduler = (task, delay) -> task.run();

        presenter = new EvaluationPresenter(viewStub, String.valueOf(testBooking.getBookingID()), immediateScheduler);
    }

    /**
     * Verifies that the view is correctly populated with driver and booking info on startup.
     */
    @Test
    public void testInitialization_SetsCorrectViewData() {
        Assert.assertNotNull(viewStub.driverName);
        Assert.assertNotNull(viewStub.carInfo);
        Assert.assertTrue(viewStub.customerName.contains(testBooking.getCustomer().getName()));
        Assert.assertTrue(viewStub.bookingDetails.contains(String.valueOf(testBooking.getBookingID())));
    }

    /**
     * Verifies that attempting to submit without selecting star ratings triggers an error message.
     */
    @Test
    public void testOnSubmitRating_InvalidInput_ShowsError() {
        viewStub.setSimulatedValidation(false);

        presenter.onSubmitRating();

        Assert.assertNotNull("Error message should be shown", viewStub.errorMessage);
        Assert.assertFalse("Should NOT navigate away", viewStub.rideTrackingNavigated);
    }

    /**
     * Verifies that a valid submission updates the booking status and persists the rating data.
     */
    @Test
    public void testOnSubmitRating_ValidInput_SavesAndNavigates() {
        viewStub.setSimulatedValidation(true);
        viewStub.setSimulatedComment("Great driver!");
        ArrayList<Double> ratings = new ArrayList<>(Arrays.asList(4.0, 4.0, 4.0, 4.0));
        viewStub.setSimulatedRatings(ratings);

        presenter.onSubmitRating();

        // Check navigation
        Assert.assertTrue("Should navigate after submission", viewStub.rideTrackingNavigated);
        Assert.assertEquals(String.valueOf(testBooking.getBookingID()), viewStub.navigatedBookingID);

        // Check Domain state
        Assert.assertTrue("Booking should be marked as evaluated", testBooking.isEvaluated());

        // Check Persistence
        List<DriverRating> allRatings = ratingDAO.findAll();
        boolean found = false;
        for (DriverRating r : allRatings) {
            if (r.getCustomerComment().equals("Great driver!")) {
                found = true;
                break;
            }
        }
        Assert.assertTrue("New rating should be saved in DAO", found);
    }

    /**
     * Verifies that canceling the evaluation redirects the user back to the home screen.
     */
    @Test
    public void testOnCancelRating_NavigatesBack() {
        presenter.onCancelRating();

        Assert.assertTrue("Should navigate to booking screen", viewStub.bookingScreenNavigated);
    }

    /**
     * Verifies that the presenter handles invalid booking IDs gracefully without crashing.
     */
    @Test
    public void testInitialization_WithInvalidBookingID_DoesNotCrash() {
        EvaluationViewStub cleanViewStub = new EvaluationViewStub();
        EvaluationPresenter.Scheduler immediateScheduler = (task, delay) -> task.run();

        EvaluationPresenter errorPresenter = new EvaluationPresenter(cleanViewStub, "999999", immediateScheduler);
        errorPresenter.onSubmitRating();

        Assert.assertFalse("Should not navigate", cleanViewStub.rideTrackingNavigated);
        Assert.assertNull("Driver name should be null", cleanViewStub.driverName);
    }
}