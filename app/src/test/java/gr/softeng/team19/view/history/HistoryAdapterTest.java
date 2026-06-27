package gr.softeng.team19.view.history;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.memorydao.MemoryInitializer;
import gr.softeng.team19.memorydao.TaxiBookingDAOMemory;

/**
 * Unit tests for HistoryAdapter to ensure correct mapping of booking data to the UI list.
 */
public class HistoryAdapterTest {

    private HistoryAdapter adapter;
    private List<TaxiBooking> testBookings;

    /**
     * Prepares memory data and initializes the adapter before each test.
     */
    @Before
    public void setUp() {
        MemoryInitializer.prepareData();
        testBookings = new TaxiBookingDAOMemory().findAll();
        adapter = new HistoryAdapter(testBookings);
    }

    /**
     * Verifies that the adapter returns the correct number of items for the RecyclerView.
     */
    @Test
    public void testGetItemCount() {
        Assert.assertEquals(testBookings.size(), adapter.getItemCount());
    }

    /**
     * Verifies that the adapter handles an empty history list without errors.
     */
    @Test
    public void testEmptyList() {
        HistoryAdapter emptyAdapter = new HistoryAdapter(new ArrayList<>());
        Assert.assertEquals(0, emptyAdapter.getItemCount());
    }

    /**
     * Verifies the proper instantiation of the adapter.
     */
    @Test
    public void testConstructor() {
        Assert.assertNotNull(adapter);
    }
}