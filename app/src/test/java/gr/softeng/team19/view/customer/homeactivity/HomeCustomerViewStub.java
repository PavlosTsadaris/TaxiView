package gr.softeng.team19.view.customer.homeactivity;

import java.util.List;
import gr.softeng.team19.domain.TaxiBooking;

/**
 * Manual stub implementation of HomeCustomerView used to track UI interactions
 * and pending trip evaluations on the customer dashboard.
 */
public class HomeCustomerViewStub implements HomeCustomerView {

    public String welcomeMessage;
    public String navigatedBookRideUsername;
    public String navigatedProfileUsername;
    public int historyClickCount = 0;
    public int ratingsClickCount = 0;
    public int requestsClickCount = 0;

    // State trackers for pending evaluation logic
    public int badgeCount = -1;
    public List<TaxiBooking> pendingList;
    public boolean hidePendingListCalled = false;
    public boolean showNoReviewsMessageCalled = false;
    public TaxiBooking bookingForReview;

    /**
     * Records the welcome text displayed to the customer.
     * @param username The formatted name or username.
     */
    @Override
    public void setWelcomeMessage(String username) {
        this.welcomeMessage = username;
    }

    /**
     * Captures the username used for ride booking navigation.
     */
    @Override
    public void navigateToBookRide(String username) {
        this.navigatedBookRideUsername = username;
    }

    /**
     * Captures the username used for profile screen navigation.
     */
    @Override
    public void navigateToProfile(String username) {
        this.navigatedProfileUsername = username;
    }

    /** Increments the counter for ride history navigation. */
    @Override
    public void navigateToHistory() {
        this.historyClickCount++;
    }

    /** Increments the counter for ratings screen navigation. */
    @Override
    public void navigateToRatings() {
        this.ratingsClickCount++;
    }

    /** Increments the counter for active requests navigation. */
    @Override
    public void navigateToRequests() {
        this.requestsClickCount++;
    }

    /**
     * Records the number displayed on the notification badge.
     * @param size The count of pending evaluations.
     */
    @Override
    public void showNotificationBadge(int size) {
        this.badgeCount = size;
    }

    /**
     * Captures the list of bookings awaiting evaluation.
     * @param pendingBookings List of trip objects.
     */
    @Override
    public void setupPendingList(List<TaxiBooking> pendingBookings) {
        this.pendingList = pendingBookings;
    }

    /**
     * Records the specific booking selected for review navigation.
     * @param booking The trip to be evaluated.
     */
    @Override
    public void navigateToDriverEvaluation(TaxiBooking booking) {
        this.bookingForReview = booking;
    }

    /** Marks that the pending evaluations section was hidden. */
    @Override
    public void hidePendingList() {
        this.hidePendingListCalled = true;
    }

    /** Marks that the "No reviews found" message was triggered. */
    @Override
    public void showNoReviewsMessage() {
        this.showNoReviewsMessageCalled = true;
    }
}
