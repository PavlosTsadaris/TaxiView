package gr.softeng.team19.view.customer.homeactivity;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team19.domain.Customer;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.memorydao.CustomerDAOMemory;
import gr.softeng.team19.memorydao.TaxiBookingDAOMemory;

/**
 * Presenter for the Home Customer Activity.
 */
public class HomeCustomerPresenter {
    private HomeCustomerView view;
    private String currentUsername;
    private TaxiBookingDAOMemory bookingDAO;

    /**
     * @param view The view interface implementation.
     */
    public HomeCustomerPresenter(HomeCustomerView view) {
        this.view = view;
        this.bookingDAO = new TaxiBookingDAOMemory();
    }

    /**
     * Loads customer data and updates the welcome message.
     * @param username The identifier of the logged-in customer.
     */
    public void onHomePageLoad(String username) {
        this.currentUsername = username;

        if (username == null) {
            view.setWelcomeMessage("Guest");
            return;
        }

        CustomerDAOMemory customerDAO = new CustomerDAOMemory();
        Customer customer = customerDAO.find(username);

        if (customer != null) {
            view.setWelcomeMessage(customer.getName() + " " + customer.getSurname());
        } else {
            view.setWelcomeMessage(username);
        }
    }

    /**
     * Triggers navigation to the ride booking screen.
     */
    public void onBookRideSelected() {
        view.navigateToBookRide(currentUsername);
    }

    /**
     * Triggers navigation to the profile screen.
     */
    public void onProfileSelected() {
        view.navigateToProfile(currentUsername);
    }

    /**
     * Triggers navigation to the ride history screen.
     */
    public void onHistorySelected() {
        view.navigateToHistory();
    }

    /**
     * Triggers navigation to the ratings screen.
     */
    public void onRatingsSelected() {
        view.navigateToRatings();
    }

    /**
     * Triggers navigation to the requests screen.
     */
    public void onRequestsSelected() {
        view.navigateToRequests();
    }

    /**
     * Filters bookings to find completed rides that need evaluation.
     */
    public void checkForPendingEvaluations() {
        List<TaxiBooking> allBookings = bookingDAO.findAll();
        List<TaxiBooking> pendingList = new ArrayList<>();

        for (TaxiBooking booking : allBookings) {
            if (booking.getCustomer().getUserName().equals(currentUsername) && !booking.isEvaluated()) {
                pendingList.add(booking);
            }
        }

        view.showNotificationBadge(pendingList.size());
        if (!pendingList.isEmpty()) {
            view.setupPendingList(pendingList);
        } else {
            view.showNoReviewsMessage();
            view.hidePendingList();
        }
    }

    /**
     * Refresh logic when the notification badge is clicked.
     */
    public void onNotificationBadgeClicked() {
        checkForPendingEvaluations();
    }

    /**
     * Navigates to the evaluation screen for the chosen booking.
     * @param selectedBooking The booking object to review.
     */
    public void onBookingSelectedForReview(TaxiBooking selectedBooking) {
        view.navigateToDriverEvaluation(selectedBooking);
    }
}