package gr.softeng.team19.view.customer.bookride.destination;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import gr.softeng.team19.memorydao.MockRideData;

/**
 * Unit tests for ChooseDestinationPresenter.
 * Verifies location selection logic and navigation flows.
 */
public class ChooseDestinationPresenterTest {

    private ChooseDestinationPresenter presenter;
    private ChooseDestinationViewStub viewStub;

    /** Sets up a fresh view stub and presenter before each test. */
    @Before
    public void setUp() {
        viewStub = new ChooseDestinationViewStub();
        presenter = new ChooseDestinationPresenter(viewStub);
    }

    /** Verifies the location dialog is filled with names from mock data. */
    @Test
    public void testShowLocationSelectionDialog_PopulatesCorrectNames() {
        presenter.showLocationSelectionDialog();

        Assert.assertNotNull("Location names should not be null", viewStub.displayedLocationNames);
        Assert.assertEquals("List size should match Mock Data", MockRideData.LOCATIONS.size(), viewStub.displayedLocationNames.length);
        Assert.assertEquals(MockRideData.LOCATIONS.get(0).name, viewStub.displayedLocationNames[0]);
    }

    /** Verifies that selecting the first available location updates the UI. */
    @Test
    public void testOnChooseDestination_FirstItem_UpdatesViewCorrectly() {
        int index = 0;
        MockRideData.DemoLocation expectedLoc = MockRideData.LOCATIONS.get(index);

        presenter.onChooseDestination(index);

        Assert.assertEquals("Map location should update", expectedLoc, viewStub.mapLocation);
        Assert.assertEquals("Address text should update", expectedLoc.name, viewStub.displayedAddressText);
        Assert.assertTrue("Confirm button should be enabled", viewStub.isConfirmButtonEnabled);
    }

    /** Verifies that selecting the last available location updates the UI. */
    @Test
    public void testOnChooseDestination_LastItem_UpdatesViewCorrectly() {
        int index = MockRideData.LOCATIONS.size() - 1;
        MockRideData.DemoLocation expectedLoc = MockRideData.LOCATIONS.get(index);

        presenter.onChooseDestination(index);

        Assert.assertEquals("Map location should update to last item", expectedLoc, viewStub.mapLocation);
        Assert.assertEquals("Address text should update to last item", expectedLoc.name, viewStub.displayedAddressText);
        Assert.assertTrue("Confirm button should be enabled", viewStub.isConfirmButtonEnabled);
    }

    /** Verifies that confirming a destination triggers navigation to driver selection. */
    @Test
    public void testOnConfirmDestination_NavigatesToDriverSelection() {
        String destination = "Acropolis Museum";

        presenter.onConfirmDestination(destination);

        Assert.assertEquals("Should navigate with correct destination name", destination, viewStub.navigatedDestination);
    }

    /** Verifies that canceling the selection returns the user to the home screen. */
    @Test
    public void testOnCancelBooking_NavigatesBackToHome() {
        presenter.onCancelBooking();

        Assert.assertTrue("Should navigate back to Home", viewStub.homeNavigated);
    }
}