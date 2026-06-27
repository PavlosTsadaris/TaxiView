package gr.softeng.team19.view.rideRequests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import gr.softeng.team19.domain.TaxiRideRequest;
import gr.softeng.team19.memorydao.MemoryInitializer;
import gr.softeng.team19.memorydao.TaxiRideRequestDAOMemory;

/**
 * Unit tests for RideRequestsPresenter to verify request filtering and navigation.
 */
public class RideRequestsPresenterTest {

    private RideRequestsViewStub viewStub;
    private RideRequestsPresenter presenter;
    private final String TEST_USER = "d";

    /**
     * Prepares data and view stub before each test execution.
     */
    @Before
    public void setUp() {
        MemoryInitializer.prepareData();
        viewStub = new RideRequestsViewStub();
    }

    /**
     * Verifies that the presenter correctly retrieves and displays requests for a specific user.
     */
    @Test
    public void testLoadRequestsSuccess() {
        presenter = new RideRequestsPresenter(viewStub, TEST_USER);

        Assert.assertNotNull(viewStub.receivedRequests);
        for (TaxiRideRequest req : viewStub.receivedRequests) {
            Assert.assertEquals(TEST_USER, req.getCustomer().getUserName());
        }
    }

    /**
     * Verifies that the empty state is displayed when a user has no active requests.
     */
    @Test
    public void testLoadRequestsEmptyState() {
        presenter = new RideRequestsPresenter(viewStub, "non_existent_user");
        Assert.assertTrue(viewStub.emptyStateShown);
        Assert.assertNull(viewStub.receivedRequests);
    }

    /**
     * Verifies handling of null username input.
     */
    @Test
    public void testLoadRequestsNullUser() {
        presenter = new RideRequestsPresenter(viewStub, null);
        Assert.assertTrue(viewStub.emptyStateShown);
    }

    /**
     * Verifies that the goBack request triggers navigation in the view.
     */
    @Test
    public void testGoBack() {
        presenter = new RideRequestsPresenter(viewStub, TEST_USER);
        presenter.goBack();
        Assert.assertTrue(viewStub.navigatedBack);
    }
}