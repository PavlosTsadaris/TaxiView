package gr.softeng.team19.view.ratings;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.memorydao.MemoryInitializer;
import gr.softeng.team19.memorydao.TaxiBookingDAOMemory;

/**
 * Unit tests for RatingsPresenter to verify filtering of evaluated rides and navigation.
 */
public class RatingsPresenterTest {

    private RatingsPresenter presenter;
    private RatingsViewStub viewStub;
    private TaxiBookingDAOMemory bookingDAO;

    /**
     * Initializes mock data and view stub before each test.
     */
    @Before
    public void setUp() {
        MemoryInitializer.prepareData();
        bookingDAO = new TaxiBookingDAOMemory();
        viewStub = new RatingsViewStub();
    }

    /**
     * Verifies that a customer correctly sees a list containing only their evaluated rides.
     */
    @Test
    public void testLoadRatings_CustomerWithEvaluatedRides_ShowsList() {
        String username = "kostas";

        List<TaxiBooking> allBookings = bookingDAO.findAll();
        TaxiBooking targetBooking = null;
        for(TaxiBooking b : allBookings) {
            if(b.getCustomer().getUserName().equals(username)) {
                b.setEvaluated(true);
                targetBooking = b;
                break;
            }
        }
        Assert.assertNotNull("Setup failed: Could not find booking for kostas", targetBooking);

        presenter = new RatingsPresenter(viewStub, username);

        Assert.assertFalse("Should not show empty state", viewStub.emptyStateShown);
        Assert.assertNotNull("List should be populated", viewStub.displayedBookings);
        Assert.assertTrue("Target booking should be in the list", viewStub.displayedBookings.contains(targetBooking));
    }

    /**
     * Verifies that the empty state is displayed when a customer has no evaluated rides.
     */
    @Test
    public void testLoadRatings_CustomerWithNoEvaluatedRides_ShowsEmpty() {
        String username = "kostas";

        List<TaxiBooking> allBookings = bookingDAO.findAll();
        for(TaxiBooking b : allBookings) {
            if(b.getCustomer().getUserName().equals(username)) {
                b.setEvaluated(false);
            }
        }

        presenter = new RatingsPresenter(viewStub, username);

        Assert.assertTrue("Should show empty state", viewStub.emptyStateShown);
        Assert.assertNull("List should not be populated", viewStub.displayedBookings);
    }

    /**
     * Verifies that a driver can see rides they participated in that have been evaluated.
     */
    @Test
    public void testLoadRatings_DriverWithEvaluatedRides_ShowsList() {
        String username = "driver1";

        List<TaxiBooking> allBookings = bookingDAO.findAll();
        TaxiBooking targetBooking = null;
        for(TaxiBooking b : allBookings) {
            if(b.getTaxiDriver().getUserName().equals(username)) {
                b.setEvaluated(true);
                targetBooking = b;
                break;
            }
        }
        Assert.assertNotNull("Setup failed: Could not find booking for driver giannis", targetBooking);

        presenter = new RatingsPresenter(viewStub, username);

        Assert.assertNotNull("List should be populated", viewStub.displayedBookings);
        Assert.assertTrue("Target booking should be in the list", viewStub.displayedBookings.contains(targetBooking));
    }

    /**
     * Verifies handling of null username input by showing the empty state.
     */
    @Test
    public void testLoadRatings_NullUser_ShowsEmptyState() {
        presenter = new RatingsPresenter(viewStub, null);

        Assert.assertTrue("Should show empty state", viewStub.emptyStateShown);
    }

    /**
     * Verifies that the back request triggers navigation in the view.
     */
    @Test
    public void testGoBack_NavigatesBack() {
        presenter = new RatingsPresenter(viewStub, "kostas");
        presenter.goBack();

        Assert.assertTrue("Should navigate back", viewStub.backNavigated);
    }
}