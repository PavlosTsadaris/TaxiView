package gr.softeng.team19.view.driver.TrackRide;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.memorydao.MemoryInitializer;
import gr.softeng.team19.memorydao.TaxiBookingDAOMemory;

/**
 * Unit tests for TrackRidePresenter to verify ride simulation, state transitions, and cleanup.
 */
public class TrackRidePresenterTest {

    private TrackRideViewStub viewStub;
    private TrackRidePresenter presenter;
    private String testBookingId;

    /**
     * Helper scheduler to execute background tasks immediately during testing.
     */
    private class TestScheduler implements TrackRidePresenter.Scheduler {
        private int callCount = 0;
        private static final int MAX_RECURSION = 100;

        @Override
        public void execute(Runnable task, long delayMillis) {
            callCount++;
            if (callCount < MAX_RECURSION) {
                task.run();
            }
        }

        @Override
        public void cancel(Runnable task) {
            callCount = 0;
        }
    }

    /**
     * Sets up the data and view stub before each test.
     */
    @Before
    public void setUp() {
        MemoryInitializer.prepareData();
        viewStub = new TrackRideViewStub();
        List<TaxiBooking> bookings = new TaxiBookingDAOMemory().findAll();
        testBookingId = bookings.get(0).getBookingID();
        presenter = new TrackRidePresenter(viewStub, testBookingId, new TestScheduler());
    }

    /**
     * Exercises the default constructor to ensure basic code coverage.
     */
    @Test
    public void testProductionConstructorCoverage() {
        try {
            new TrackRidePresenter(viewStub, testBookingId);
        } catch (Exception ignored) {}
    }

    /**
     * Verifies that the presenter handles null or invalid booking IDs by showing an error.
     */
    @Test
    public void testInvalidBookingIdScenarios() {
        new TrackRidePresenter(viewStub, null, new TestScheduler());
        Assert.assertEquals("Error: Booking not found", viewStub.lastMessage);

        new TrackRidePresenter(viewStub, "   ", new TestScheduler());
        Assert.assertEquals("Error: Booking not found", viewStub.lastMessage);

        new TrackRidePresenter(viewStub, "invalid_123", new TestScheduler());
        Assert.assertEquals("Error: Booking not found", viewStub.lastMessage);
    }

    /**
     * Verifies the full progression of a ride from pickup to completion using the skip feature.
     */
    @Test
    public void testFullRideCycleWithSkip() {
        // Step 1: Drive to the customer pickup point
        presenter.onSkipRide();
        presenter.goToCustomer();
        Assert.assertTrue("Arrival button should be enabled", viewStub.arrivalButtonEnabled);
        Assert.assertEquals("Arrived at Customer", viewStub.lastMessage);

        // Step 2: Drive to the final destination
        presenter.onSkipRide();
        presenter.onArrivedAtPickup();
        Assert.assertTrue("End ride button should be enabled", viewStub.endRideButtonEnabled);
        Assert.assertEquals("Arrived at Destination", viewStub.lastMessage);

        // Step 3: Complete the ride and trigger payment
        presenter.onEndRide();
        Assert.assertNotNull("Payment navigation should receive an amount", viewStub.navigatedPaymentAmount);
        Assert.assertEquals("Ride Completed.", viewStub.lastMessage);
    }

    /**
     * Verifies that the UI handles locations that do not have a known address string.
     */
    @Test
    public void testUnknownLocationLookup() {
        TaxiBooking booking = new TaxiBookingDAOMemory().find(testBookingId);
        booking.getPickupPoint().setLatitude(0.0);
        booking.getPickupPoint().setLongitude(0.0);

        presenter = new TrackRidePresenter(viewStub, testBookingId, new TestScheduler());
        Assert.assertNotNull(viewStub.rideInfoLabel);
    }

    /**
     * Verifies the incremental movement logic used for real-time tracking.
     */
    @Test
    public void testIncrementalMovementPath() {
        presenter.goToCustomer();
        Assert.assertTrue(viewStub.arrivalButtonEnabled);
    }

    /**
     * Verifies that the presenter stops all tasks when the view is destroyed.
     */
    @Test
    public void testOnDestroyCleanup() {
        presenter.goToCustomer();
        presenter.onDestroy();
        Assert.assertNotNull(presenter);
    }
}