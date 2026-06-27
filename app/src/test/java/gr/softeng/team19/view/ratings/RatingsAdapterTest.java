package gr.softeng.team19.view.ratings;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.memorydao.MemoryInitializer;
import gr.softeng.team19.memorydao.TaxiBookingDAOMemory;

/**
 * Unit tests for RatingsAdapter to ensure proper item counting for the UI list.
 */
public class RatingsAdapterTest {

    private RatingsAdapter adapter;
    private List<TaxiBooking> ratedBookings;

    /**
     * Initializes mock data and filters evaluated bookings before each test.
     */
    @Before
    public void setUp() {
        MemoryInitializer.prepareData();
        ratedBookings = new ArrayList<>();
        for (TaxiBooking b : new TaxiBookingDAOMemory().findAll()) {
            if (b.isEvaluated()) {
                ratedBookings.add(b);
            }
        }
        adapter = new RatingsAdapter(ratedBookings);
    }

    /**
     * Verifies that the adapter correctly reports the number of evaluated bookings.
     */
    @Test
    public void testGetItemCount() {
        Assert.assertEquals(ratedBookings.size(), adapter.getItemCount());
    }

    /**
     * Verifies that the adapter handles an empty dataset without crashing.
     */
    @Test
    public void testEmptyList() {
        RatingsAdapter emptyAdapter = new RatingsAdapter(new ArrayList<>());
        Assert.assertEquals(0, emptyAdapter.getItemCount());
    }

    /**
     * Verifies that the adapter instance is correctly instantiated.
     */
    @Test
    public void testConstructor() {
        Assert.assertNotNull(adapter);
    }
}