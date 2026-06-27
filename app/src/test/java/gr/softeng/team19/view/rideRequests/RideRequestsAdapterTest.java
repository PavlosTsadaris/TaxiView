package gr.softeng.team19.view.rideRequests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import gr.softeng.team19.domain.TaxiRideRequest;
import gr.softeng.team19.memorydao.MemoryInitializer;
import gr.softeng.team19.memorydao.TaxiRideRequestDAOMemory;

/**
 * Unit tests for RideRequestsAdapter to verify data binding logic for the UI list.
 */
public class RideRequestsAdapterTest {

    private RideRequestsAdapter adapter;
    private List<TaxiRideRequest> testList;

    /**
     * Initializes test data and the adapter before each test case.
     */
    @Before
    public void setUp() {
        MemoryInitializer.prepareData();
        testList = new TaxiRideRequestDAOMemory().findAll();
        adapter = new RideRequestsAdapter(testList);
    }

    /**
     * Verifies that the adapter correctly reports the size of the data list.
     */
    @Test
    public void testGetItemCount() {
        Assert.assertEquals(testList.size(), adapter.getItemCount());
    }

    /**
     * Verifies that the adapter handles an empty list without errors.
     */
    @Test
    public void testGetItemCountEmpty() {
        adapter = new RideRequestsAdapter(new ArrayList<>());
        Assert.assertEquals(0, adapter.getItemCount());
    }
}