package gr.softeng.team19.view.driver.chooselocation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDate;
import gr.softeng.team19.domain.TaxiDriver;
import gr.softeng.team19.memorydao.MockRideData;
import gr.softeng.team19.memorydao.TaxiDriverDAOMemory;

/**
 * Unit tests for DriverChooseLocationPresenter to verify location selection and DAO updates.
 */
public class DriverChooseLocationPresenterTest {

    private DriverChooseLocationViewStub viewStub;
    private DriverChooseLocationPresenter presenter;
    private TaxiDriverDAOMemory driverDAO;
    private final String TEST_USERNAME = "tester_driver";

    /**
     * Initializes the test environment and populates the DAO with a test driver.
     */
    @Before
    public void setUp() {
        viewStub = new DriverChooseLocationViewStub();
        presenter = new DriverChooseLocationPresenter(viewStub);
        driverDAO = new TaxiDriverDAOMemory();

        TaxiDriver testDriver = new TaxiDriver(
                TEST_USERNAME, "pass", "test@test.com", "John", "Doe",
                "6900000000", LocalDate.now(), "IBAN123",
                37.9755, 23.7348, "Ermou", "Athens", 1, 10000,
                "ABC-1234", "Toyota", "Corolla"
        );
        testDriver.setAvailability(false);
        driverDAO.save(testDriver);
    }

    /**
     * Verifies that the selection dialog is populated with data from the mock location provider.
     */
    @Test
    public void testShowLocationSelectionDialog() {
        presenter.showLocationSelectionDialog();
        Assert.assertNotNull("Location names should not be null", viewStub.lastLocationNames);
        Assert.assertEquals("Location count should match MockRideData size",
                MockRideData.LOCATIONS.size(), viewStub.lastLocationNames.length);
    }

    /**
     * Verifies that picking a location updates the UI and enables the confirmation button.
     */
    @Test
    public void testOnChooseLocationValid() {
        presenter.showLocationSelectionDialog();
        presenter.onChooseLocation(0);

        Assert.assertNotNull("View should display the address text", viewStub.lastLocationText);
        Assert.assertNotNull("Map should receive new coordinates", viewStub.lastMapLocation);
        Assert.assertTrue("Confirmation button must be enabled", viewStub.buttonEnabled);
    }

    /**
     * Verifies that the UI prevents proceeding if an invalid selection index is provided.
     */
    @Test
    public void testOnChooseLocationInvalidIndices() {
        presenter.onChooseLocation(-1);
        Assert.assertFalse("Button should not enable for negative index", viewStub.buttonEnabled);

        presenter.onChooseLocation(9999);
        Assert.assertFalse("Button should not enable for out-of-bounds index", viewStub.buttonEnabled);
    }

    /**
     * Verifies that the "Go Online" action successfully updates the driver's availability in the database.
     */
    @Test
    public void testOnGoOnlineSuccess() {
        MockRideData.DemoLocation loc = new MockRideData.DemoLocation("Test Spot", 38.0, 23.0);

        presenter.onGoOnline(TEST_USERNAME, loc);

        // Verify navigation triggered in View
        Assert.assertEquals("Test Spot", viewStub.onlineModeLocation);

        // Verify state change in DAO
        TaxiDriver updatedDriver = driverDAO.find(TEST_USERNAME);
        Assert.assertNotNull(updatedDriver);
        Assert.assertTrue("Driver status must be set to Available", updatedDriver.getAvailability());
        Assert.assertEquals(38.0, updatedDriver.getUserLocation().getLatitude(), 0.001);
    }

    /**
     * Verifies that going online is blocked or handled safely if no location is selected.
     */
    @Test
    public void testOnGoOnlineWithNullLocation() {
        presenter.onGoOnline(TEST_USERNAME, null);
        Assert.assertNull("Navigation should not occur without location data", viewStub.onlineModeLocation);
    }

    /**
     * Verifies handling of non-existent drivers during the status update process.
     */
    @Test
    public void testOnGoOnlineWithInvalidUser() {
        MockRideData.DemoLocation loc = new MockRideData.DemoLocation("Test Spot", 38.0, 23.0);
        presenter.onGoOnline("unknown_user_123", loc);

        Assert.assertEquals("Test Spot", viewStub.onlineModeLocation);
    }

    /**
     * Verifies that the cancel button triggers back navigation.
     */
    @Test
    public void testOnCancel() {
        presenter.onCancel();
        Assert.assertEquals("navigateBack should be called once", 1, viewStub.navigateBackCount);
    }
}