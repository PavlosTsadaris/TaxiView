package gr.softeng.team19.view.customer.bookride.trackdriver;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.osmdroid.util.GeoPoint;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.memorydao.MemoryInitializer;
import gr.softeng.team19.memorydao.TaxiBookingDAOMemory;

/**
 * Unit tests for TrackPickUpPresenter to verify real-time driver tracking and simulation.
 */
public class TrackPickUpPresenterTest {

    private TrackPickUpPresenter presenter;
    private TrackPickUpViewStub viewStub;
    private TaxiBookingDAOMemory bookingDAO;
    private TaxiBooking testBooking;

    /**
     * Helper scheduler that runs tasks immediately for non-simulation tests.
     */
    private TrackPickUpPresenter.Scheduler immediateScheduler = new TrackPickUpPresenter.Scheduler() {
        @Override
        public void execute(Runnable task, long delayMillis) {
            task.run();
        }

        @Override
        public void executeRecurring(Runnable task) {
            // Do nothing here to prevent the infinite simulation loop from breaking basic tests
        }

        @Override
        public void removeCallbacks(Runnable task) {}
    };

    /**
     * Initializes mock data and ensures the test booking has valid coordinates before each test.
     */
    @Before
    public void setUp() {
        MemoryInitializer.prepareData();
        bookingDAO = new TaxiBookingDAOMemory();
        viewStub = new TrackPickUpViewStub();

        List<TaxiBooking> allBookings = bookingDAO.findAll();
        Assert.assertFalse("Should have bookings from Initializer", allBookings.isEmpty());
        testBooking = allBookings.get(0);

        // Set up coordinates for driver, pickup, and destination
        testBooking.getTaxiDriver().setUserLocation(37.97, 23.73);
        testBooking.getPickupPoint().setPoint(new GeoPoint(37.98, 23.74));
        testBooking.getDestination().setPoint(new GeoPoint(37.99, 23.75));

        presenter = new TrackPickUpPresenter(viewStub, String.valueOf(testBooking.getBookingID()), immediateScheduler);
    }

    /**
     * Verifies that the view is updated with the driver's name and status when tracking starts.
     */
    @Test
    public void testStartTracking_UpdatesInitialInfo() {
        presenter.startTracking();
        Assert.assertNotNull("Driver name should be displayed", viewStub.driverName);
        Assert.assertTrue("Status should indicate driver is on the way", viewStub.status.contains("Driver"));
    }

    /**
     * Verifies that the skip button correctly triggers the immediate-arrival logic.
     */
    @Test
    public void testOnSkip_ShowsSkipOption() {
        presenter.startTracking();
        presenter.onSkip();
        Assert.assertTrue("Skip functionality should be triggered", viewStub.skipShown);
    }

    /**
     * Verifies that cancelling the ride updates the UI and redirects the user to the home screen.
     */
    @Test
    public void testOnCancelRide_NavigatesHome() {
        presenter.onCancelRide();
        Assert.assertEquals("Ride Cancelled", viewStub.lastMessage);
        Assert.assertTrue("Should navigate home", viewStub.homeNavigated);
    }

    /**
     * Verifies that the call driver action displays the driver's contact info.
     */
    @Test
    public void testOnCallDriver_ShowsCallingMessage() {
        presenter.onCallDriver();
        Assert.assertTrue("Message should contain driver name",
                viewStub.lastMessage.contains(testBooking.getTaxiDriver().getName()));
    }

    /**
     * Verifies that the notification icon correctly opens the pending evaluation prompt.
     */
    @Test
    public void testOnNotificationIcon_ShowsReviewPrompt() {
        presenter.onNotificationIcon();
        Assert.assertTrue("Review prompt should be shown", viewStub.reviewPromptShown);
    }

    /**
     * Verifies that selecting "Review Now" closes the prompt and moves to the evaluation screen.
     */
    @Test
    public void testOnReviewNow_NavigatesToEvaluation() {
        presenter.onReviewNow(testBooking);
        Assert.assertTrue("Review prompt should be deleted", viewStub.reviewPromptDeleted);
        Assert.assertTrue("Should navigate to evaluation screen", viewStub.evaluationNavigated);
    }

    /**
     * Verifies that the presenter handles non-existent booking IDs by showing an error message.
     */
    @Test
    public void testStartTracking_WithInvalidBooking_ShowsError() {
        TrackPickUpPresenter invalidPresenter = new TrackPickUpPresenter(viewStub, "invalid_id_999", immediateScheduler);
        invalidPresenter.startTracking();
        Assert.assertEquals("Error: Booking not found!", viewStub.lastMessage);
    }

    /**
     * Verifies the full ride simulation: movement, pickup arrival, and final ride completion.
     */
    @Test
    public void testFullSimulation_EndToEnd() {
        final Queue<Runnable> taskQueue = new LinkedList<>();

        TrackPickUpPresenter.Scheduler queueScheduler = new TrackPickUpPresenter.Scheduler() {
            @Override
            public void execute(Runnable task, long delayMillis) { taskQueue.add(task); }
            @Override
            public void executeRecurring(Runnable task) { taskQueue.add(task); }
            @Override
            public void removeCallbacks(Runnable task) {}
        };

        TrackPickUpPresenter simPresenter = new TrackPickUpPresenter(viewStub, String.valueOf(testBooking.getBookingID()), queueScheduler);

        simPresenter.onSkip(); // Fast-forward simulation
        simPresenter.startTracking();

        // Process the simulation queue
        int safetyLimit = 0;
        while (!taskQueue.isEmpty() && safetyLimit < 100) {
            simPresenter.onSkip();
            Runnable task = taskQueue.poll();
            if (task != null) task.run();
            safetyLimit++;
        }

        Assert.assertTrue("Should have navigated to Payment after ride ends", viewStub.paymentNavigated);
    }
}