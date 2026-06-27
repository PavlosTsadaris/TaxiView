package gr.softeng.team19.view.customer.homeactivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import gr.softeng.team19.domain.Customer;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.memorydao.CustomerDAOMemory;
import gr.softeng.team19.memorydao.MemoryInitializer;
import gr.softeng.team19.memorydao.TaxiBookingDAOMemory;

/**
 * Unit tests for HomeCustomerPresenter to verify dashboard data, navigation, and evaluation logic.
 */
public class HomeCustomerPresenterTest {

    private HomeCustomerPresenter presenter;
    private HomeCustomerViewStub viewStub;
    private TaxiBookingDAOMemory bookingDAO;
    private CustomerDAOMemory customerDAO;

    /**
     * Prepares memory data and initializes the presenter with a view stub before each test.
     */
    @Before
    public void setUp() {
        MemoryInitializer.prepareData();
        bookingDAO = new TaxiBookingDAOMemory();
        customerDAO = new CustomerDAOMemory();
        viewStub = new HomeCustomerViewStub();
        presenter = new HomeCustomerPresenter(viewStub);
    }

    /**
     * Verifies that the home page displays the full name of a registered customer upon loading.
     */
    @Test
    public void testOnHomePageLoad_WithExistingUser_ShowsName() {
        String username = "kostas";
        presenter.onHomePageLoad(username);
        Assert.assertEquals("Kostas Nikolaou", viewStub.welcomeMessage);
    }

    /**
     * Verifies that the presenter falls back to the username if the full profile data is missing.
     */
    @Test
    public void testOnHomePageLoad_WithUnknownUser_ShowsUsername() {
        String username = "ghostUser123";
        presenter.onHomePageLoad(username);
        Assert.assertEquals("ghostUser123", viewStub.welcomeMessage);
    }

    /**
     * Verifies that the book ride action triggers navigation for the correct user.
     */
    @Test
    public void testOnBookRideSelected_NavigatesCorrectly() {
        String username = "maria";
        presenter.onHomePageLoad(username);
        presenter.onBookRideSelected();
        Assert.assertEquals(username, viewStub.navigatedBookRideUsername);
    }

    /**
     * Verifies that selecting the profile action opens the correct user's profile.
     */
    @Test
    public void testOnProfileSelected_NavigatesCorrectly() {
        String username = "maria";
        presenter.onHomePageLoad(username);
        presenter.onProfileSelected();
        Assert.assertEquals(username, viewStub.navigatedProfileUsername);
    }

    /**
     * Verifies that clicking standard menu items (history, ratings, requests) triggers navigation.
     */
    @Test
    public void testMenuNavigations_IncrementCounters() {
        presenter.onHistorySelected();
        Assert.assertEquals(1, viewStub.historyClickCount);

        presenter.onRatingsSelected();
        Assert.assertEquals(1, viewStub.ratingsClickCount);

        presenter.onRequestsSelected();
        Assert.assertEquals(1, viewStub.requestsClickCount);
    }

    /**
     * Verifies that users are notified of pending trip evaluations on their dashboard.
     */
    @Test
    public void testCheckForPendingEvaluations_WithPending_ShowsList() {
        String username = "eleni";
        presenter.onHomePageLoad(username);

        List<TaxiBooking> allBookings = bookingDAO.findAll();
        TaxiBooking recycledBooking = allBookings.get(0);
        recycledBooking.getCustomer().setUserName(username);
        recycledBooking.setEvaluated(false);
        bookingDAO.save(recycledBooking);

        presenter.checkForPendingEvaluations();

        Assert.assertTrue("Badge count should be positive", viewStub.badgeCount >= 1);
        Assert.assertNotNull("Pending list should be populated", viewStub.pendingList);
    }

    /**
     * Verifies that the pending list is hidden if all trips have been previously evaluated.
     */
    @Test
    public void testCheckForPendingEvaluations_NoPending_HidesList() {
        List<Customer> allCustomers = customerDAO.findAll();
        Customer existingUser = allCustomers.get(0);
        String username = existingUser.getUserName();

        List<TaxiBooking> allBookings = bookingDAO.findAll();
        for (TaxiBooking booking : allBookings) {
            if (booking.getCustomer().getUserName().equals(username)) {
                booking.setEvaluated(true);
                bookingDAO.save(booking);
            }
        }

        presenter.onHomePageLoad(username);
        presenter.checkForPendingEvaluations();

        Assert.assertTrue(viewStub.hidePendingListCalled);
        Assert.assertTrue(viewStub.showNoReviewsMessageCalled);
    }

    /**
     * Verifies that selecting a specific pending trip navigates to the review submission screen.
     */
    @Test
    public void testOnPendingEvaluationSelected_NavigatesToEvaluation() {
        List<TaxiBooking> allBookings = bookingDAO.findAll();
        TaxiBooking booking = allBookings.get(0);

        presenter.onBookingSelectedForReview(booking);

        Assert.assertEquals(booking, viewStub.bookingForReview);
    }

    /**
     * Verifies that interacting with the notification badge refreshes the evaluation list.
     */
    @Test
    public void testOnNotificationBadgeClicked_TriggersCheck() {
        String username = "kostas";
        presenter.onHomePageLoad(username);
        viewStub.badgeCount = -1;

        presenter.onNotificationBadgeClicked();

        Assert.assertTrue("Badge count should be updated", viewStub.badgeCount >= 0);
    }
}