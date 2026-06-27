package gr.softeng.team19.view.driver.riderequestsactivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import gr.softeng.team19.domain.TaxiRideRequest;
import gr.softeng.team19.memorydao.MemoryInitializer;
import gr.softeng.team19.memorydao.TaxiRideRequestDAOMemory;

/**
 * Unit tests for RideRequestsAdapter to verify data size and list updates.
 */
public class RideRequestsAdapterTest {

    private RideRequestsAdapter adapter;
    private ArrayList<TaxiRideRequest> testRequests;
    private ArrayList<Double> testDistances;

    /**
     * Initializes test data and the adapter before each test.
     */
    @Before
    public void setUp() {
        MemoryInitializer.prepareData();
        testRequests = new ArrayList<>(new TaxiRideRequestDAOMemory().findAll());
        testDistances = new ArrayList<>();
        // Mocking distances for each request
        for (int i = 0; i < testRequests.size(); i++) testDistances.add(1.5);

        adapter = new RideRequestsAdapter(testRequests, request -> {}, testDistances);
    }

    /**
     * Verifies that the adapter correctly counts the number of available ride requests.
     */
    @Test
    public void testGetItemCount() {
        Assert.assertEquals(testRequests.size(), adapter.getItemCount());
    }

    /**
     * Verifies that updating the adapter's dataset correctly changes the item count.
     */
    @Test
    public void testSetRequests() {
        ArrayList<TaxiRideRequest> newList = new ArrayList<>();
        newList.add(testRequests.get(0));
        adapter.setRequests(newList);
        Assert.assertEquals(1, adapter.getItemCount());
    }
}