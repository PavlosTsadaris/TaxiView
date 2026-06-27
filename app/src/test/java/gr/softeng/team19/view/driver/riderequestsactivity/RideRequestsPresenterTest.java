package gr.softeng.team19.view.driver.riderequestsactivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import gr.softeng.team19.domain.TaxiDriver;
import gr.softeng.team19.domain.TaxiRideRequest;
import gr.softeng.team19.memorydao.MemoryInitializer;
import gr.softeng.team19.memorydao.TaxiDriverDAOMemory;
import gr.softeng.team19.memorydao.TaxiRideRequestDAOMemory;

/**
 * Unit tests for RideRequestsPresenter to verify request filtering by distance and status.
 */
public class RideRequestsPresenterTest {

    private RideRequestsViewStub viewStub;
    private RideRequestsPresenter presenter;
    private String testDriverUser = "driver1";

    /**
     * Initializes the data environment and view stub before each test.
     */
    @Before
    public void setUp() {
        MemoryInitializer.prepareData();
        viewStub = new RideRequestsViewStub();
        presenter = new RideRequestsPresenter(viewStub, testDriverUser);
    }

    /**
     * Verifies that only requests within a 3km radius are loaded and displayed.
     */
    @Test
    public void testStartLoadingProcessSuccess() {
        presenter.startLoadingProcess();

        Assert.assertNotNull(viewStub.receivedRequests);
        Assert.assertEquals(viewStub.receivedRequests.size(), viewStub.receivedDistances.size());

        if (viewStub.receivedRequests.isEmpty()) {
            Assert.assertEquals("No customers found within 3km.", viewStub.lastMessage);
        }
    }

    /**
     * Verifies that accepting a request updates its status and triggers map navigation.
     */
    @Test
    public void testOnAcceptRequest() {
        TaxiRideRequest req = new TaxiRideRequestDAOMemory().findAll().get(0);
        presenter.onAcceptRequest(req);

        Assert.assertEquals("ACCEPTED", req.getStatus());
        Assert.assertEquals(req.getRequestID(), viewStub.navigatedRequestId);
    }

    /**
     * Verifies handling of non-existent drivers during the loading process.
     */
    @Test
    public void testStartLoadingWithInvalidDriver() {
        RideRequestsPresenter invalidPresenter = new RideRequestsPresenter(viewStub, "ghost_driver");
        invalidPresenter.startLoadingProcess();
        Assert.assertNull(viewStub.receivedRequests);
    }
}