package gr.softeng.team19.view.driver.rideexecution;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import gr.softeng.team19.domain.TaxiRideRequest;
import gr.softeng.team19.memorydao.MemoryInitializer;
import gr.softeng.team19.memorydao.TaxiBookingDAOMemory;
import gr.softeng.team19.memorydao.TaxiDriverDAOMemory;
import gr.softeng.team19.memorydao.TaxiRideRequestDAOMemory;

/**
 * Unit tests for RideNavigationPresenter to verify map setup and booking creation.
 */
public class RideNavigationPresenterTest {

    private RideNavigationViewStub viewStub;
    private RideNavigationPresenter presenter;
    private String testRequestId;

    /**
     * Initializes the data environment and ensures a valid request is available for testing.
     */
    @Before
    public void setUp() {
        MemoryInitializer.prepareData();
        viewStub = new RideNavigationViewStub();
        presenter = new RideNavigationPresenter(viewStub);

        TaxiRideRequestDAOMemory requestDAO = new TaxiRideRequestDAOMemory();
        TaxiDriverDAOMemory driverDAO = new TaxiDriverDAOMemory();

        TaxiRideRequest validRequest = null;
        for (TaxiRideRequest req : requestDAO.findAll()) {
            if (req.getChosenDriver() != null) {
                validRequest = req;
                break;
            }
        }

        if (validRequest == null) {
            validRequest = requestDAO.findAll().get(0);
            validRequest.setChosenDriver(driverDAO.findAll().get(0));
            requestDAO.save(validRequest);
        }

        testRequestId = validRequest.getRequestID();
    }

    /**
     * Verifies that starting navigation correctly initializes the map with coordinate data.
     */
    @Test
    public void testStartNavigation() {
        presenter.startNavigation(testRequestId);

        Assert.assertNotNull(viewStub.lastLocationName);
        Assert.assertTrue(viewStub.setupMapCalled);
        Assert.assertTrue(viewStub.lastLat > 37.0 && viewStub.lastLat < 39.0);
    }

    /**
     * Verifies that picking up a customer creates a formal booking and navigates to the ride screen.
     */
    @Test
    public void testOnSelectCustomer() {
        presenter.startNavigation(testRequestId);
        presenter.onSelectCustomer();

        Assert.assertNotNull(viewStub.navigatedBookingId);
        Assert.assertTrue(viewStub.showToastCalled);

        // Verify the booking is persisted in the database
        Assert.assertNotNull(new TaxiBookingDAOMemory().find(viewStub.navigatedBookingId));
    }

    /**
     * Verifies that the cancel action triggers the expected UI behavior.
     */
    @Test
    public void testOnCancel() {
        presenter.onCancel();
        Assert.assertTrue(viewStub.cancelCalled);
    }

    /**
     * Verifies that the presenter handles non-existent request IDs gracefully.
     */
    @Test
    public void testStartNavigationWithInvalidId() {
        presenter.startNavigation("invalid_id");
        Assert.assertFalse(viewStub.setupMapCalled);
    }
}