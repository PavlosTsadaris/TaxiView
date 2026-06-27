package gr.softeng.team19.view.customer.bookride.searchride;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import gr.softeng.team19.R;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.domain.TaxiDriver;
import gr.softeng.team19.memorydao.MemoryInitializer;
import gr.softeng.team19.memorydao.MockRideData;
import gr.softeng.team19.memorydao.TaxiBookingDAOMemory;

/**
 * Unit tests for BookRidePresenter to verify ride searching, driver selection, and navigation.
 */
public class BookRidePresenterTest {

    private BookRidePresenter presenter;
    private BookRideViewStub viewStub;

    /**
     * Prepares memory data and initializes the presenter with a synchronous scheduler.
     */
    @Before
    public void setUp() {
        MemoryInitializer.prepareData();
        viewStub = new BookRideViewStub();

        // Immediate scheduler to bypass Android Handler delays during tests
        BookRidePresenter.Scheduler immediateScheduler = (task, delay) -> task.run();

        presenter = new BookRidePresenter(viewStub, immediateScheduler);
    }

    /**
     * Verifies that starting a location search sets the default address and enables the UI.
     */
    @Test
    public void testStartLocationSearch_UpdatesViewImmediately() {
        presenter.startLocationSearch ("Syntagma Square, Athens");

        Assert.assertEquals("Syntagma Square, Athens", viewStub.currentAddressText);
        Assert.assertTrue(viewStub.isButtonEnabled);
        Assert.assertEquals(R.string.btn_confirm_location, viewStub.currentButtonTextResId);
    }

    /**
     * Verifies that clicking the action button initiates a search and populates the driver list.
     */
    @Test
    public void testOnActionButtonClicked_StartsSearchAndShowsDrivers() {
        String username = "kostas";
        presenter.onActionButtonClicked(username);

        Assert.assertEquals(R.string.status_drivers_found, viewStub.currentStatusTextResId);
        Assert.assertNotNull(viewStub.displayedDrivers);
        Assert.assertFalse("Drivers list should not be empty", viewStub.displayedDrivers.isEmpty());
    }

    /**
     * Verifies that canceling while idle returns the user to the home screen.
     */
    @Test
    public void testOnCancelBookRide_WhileIdle_NavigatesHome() {
        presenter.onCancelBookRide();
        Assert.assertTrue("Should navigate to home screen", viewStub.homeScreenNavigated);
    }

    /**
     * Verifies that canceling during an active search resets the UI components.
     */
    @Test
    public void testOnCancelBookRide_WhileSearching_ResetsUI() {
        presenter.onActionButtonClicked("kostas");
        presenter.onCancelBookRide();

        Assert.assertTrue("Waiting overlay should be hidden", viewStub.waitingOverlayHidden);
        Assert.assertTrue(viewStub.isButtonEnabled);
    }

    /**
     * Verifies that selecting the destination change action triggers the correct navigation.
     */
    @Test
    public void testOnChangeDestination_NavigatesToDestinationScreen() {
        presenter.onChangeDestination();
        Assert.assertTrue(viewStub.destinationScreenNavigated);
    }

    /**
     * Verifies that the location selection dialog excludes the currently selected destination.
     */
    @Test
    public void testShowLocationSelectionDialog_PopulatesNames() {
        String currentDest = MockRideData.LOCATIONS.get(0).name;
        presenter.showLocationSelectionDialog(currentDest);

        Assert.assertNotNull(viewStub.locationDialogNames);
        Assert.assertEquals(MockRideData.LOCATIONS.size() - 1, viewStub.locationDialogNames.length);
        Assert.assertEquals(currentDest, viewStub.removedDestinationFromDialog);
    }

    /**
     * Verifies that choosing a driver triggers the waiting overlay and processes the request.
     */
    @Test
    public void testOnChooseDriver_ShowsWaitingOverlay() {
        presenter.onActionButtonClicked("kostas");
        ArrayList<TaxiDriver> drivers = viewStub.displayedDrivers;

        if (drivers != null && !drivers.isEmpty()) {
            TaxiDriver selected = drivers.get(0);
            presenter.onChooseDriver(selected);

            Assert.assertTrue("Waiting overlay should be shown", viewStub.waitingOverlayShown);
            Assert.assertEquals(selected.getName(), viewStub.selectedDriverName);

            // Verifies the process reached a terminal state (Success or Reject)
            boolean finished = viewStub.successStateShown || viewStub.cancelStateShown;
            Assert.assertTrue("Process should finish", finished);
        }
    }

    /**
     * Verifies that selecting "Review Now" navigates to the driver evaluation screen.
     */
    @Test
    public void testOnReviewNow_NavigatesToEvaluation() {
        TaxiBookingDAOMemory bookingDAO = new TaxiBookingDAOMemory();
        TaxiBooking booking = bookingDAO.findAll().get(0);

        presenter.onReviewNow(booking);

        Assert.assertTrue("Review prompt should be deleted", viewStub.reviewPromptDeleted);
        Assert.assertTrue("Should navigate to evaluation", viewStub.driverEvaluationNavigated);
    }

    /**
     * Verifies that selecting "Review Later" bypasses evaluation and moves to ride tracking.
     */
    @Test
    public void testOnReviewLater_NavigatesToTracking() {
        TaxiBookingDAOMemory bookingDAO = new TaxiBookingDAOMemory();
        TaxiBooking booking = bookingDAO.findAll().get(0);
        booking.setEvaluated(true);

        presenter.onReviewLater(booking);

        Assert.assertFalse("Booking should be marked as not evaluated", booking.isEvaluated());
        Assert.assertTrue("Should navigate to tracking", viewStub.rideTrackingNavigated);
    }
}