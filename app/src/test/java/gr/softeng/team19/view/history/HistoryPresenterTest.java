package gr.softeng.team19.view.history;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import gr.softeng.team19.domain.Payment;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.memorydao.MemoryInitializer;
import gr.softeng.team19.memorydao.TaxiBookingDAOMemory;

/**
 * Unit tests for HistoryPresenter to verify data retrieval, payment calculations, and navigation.
 */
public class HistoryPresenterTest {

    private HistoryPresenter presenter;
    private HistoryViewStub viewStub;
    private TaxiBookingDAOMemory bookingDAO;

    /**
     * Initializes mock data and view stub before each test execution.
     */
    @Before
    public void setUp() {
        MemoryInitializer.prepareData();
        bookingDAO = new TaxiBookingDAOMemory();
        viewStub = new HistoryViewStub();
    }

    /**
     * Verifies that a customer correctly sees their personal ride history.
     */
    @Test
    public void testLoadHistory_CustomerWithRides_ShowsList() {
        String username = "kostas";

        presenter = new HistoryPresenter(viewStub, username);

        Assert.assertFalse("Empty state should NOT be shown", viewStub.emptyStateShown);
        Assert.assertNotNull("History list should be populated", viewStub.displayedHistory);
        Assert.assertFalse("History list should not be empty", viewStub.displayedHistory.isEmpty());

        for(TaxiBooking b : viewStub.displayedHistory) {
            Assert.assertEquals("Booking should belong to kostas", username, b.getCustomer().getUserName());
        }
    }

    /**
     * Verifies that a driver correctly sees the history of rides they provided.
     */
    @Test
    public void testLoadHistory_DriverWithRides_ShowsList() {
        String username = "driver1";

        presenter = new HistoryPresenter(viewStub, username);

        Assert.assertFalse("Empty state should NOT be shown", viewStub.emptyStateShown);
        Assert.assertNotNull("History list should be populated", viewStub.displayedHistory);

        for(TaxiBooking b : viewStub.displayedHistory) {
            Assert.assertEquals("Booking should belong to driver giannis", username, b.getTaxiDriver().getUserName());
        }
    }

    /**
     * Verifies that the total expenditure/earnings are summed correctly across all rides.
     */
    @Test
    public void testLoadHistory_CalculatesTotalAmountCorrectly() {
        String username = "kostas";

        double expectedSum = 0.0;
        for (TaxiBooking b : bookingDAO.findAll()) {
            if (b.getCustomer().getUserName().equals(username) && b.getPayment() != null) {
                expectedSum += b.getPayment().getAmount();
            }
        }

        presenter = new HistoryPresenter(viewStub, username);

        Assert.assertEquals("Total amount should match sum of payments", expectedSum, viewStub.displayedTotalAmount, 0.001);
    }

    /**
     * Verifies that the empty state is displayed when no history is found for a user.
     */
    @Test
    public void testLoadHistory_NoHistory_ShowsEmptyState() {
        String username = "ghostUser";

        presenter = new HistoryPresenter(viewStub, username);

        Assert.assertTrue("Should show empty state", viewStub.emptyStateShown);
        Assert.assertEquals("Total amount should be zero", 0.0, viewStub.displayedTotalAmount, 0.001);
        Assert.assertNull("History list should be null", viewStub.displayedHistory);
    }

    /**
     * Verifies handling of null username input (edge case).
     */
    @Test
    public void testLoadHistory_NullUser_ShowsEmptyState() {
        presenter = new HistoryPresenter(viewStub, null);

        Assert.assertTrue("Should show empty state for null user", viewStub.emptyStateShown);
    }

    /**
     * Verifies that the goBack action triggers the correct navigation in the view.
     */
    @Test
    public void testGoBack_NavigatesBack() {
        presenter = new HistoryPresenter(viewStub, "kostas");
        presenter.goBack();

        Assert.assertTrue("Should navigate back", viewStub.backNavigated);
    }
}