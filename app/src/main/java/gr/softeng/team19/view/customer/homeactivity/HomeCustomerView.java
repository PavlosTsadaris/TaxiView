package gr.softeng.team19.view.customer.homeactivity;

import java.util.List;

import gr.softeng.team19.domain.TaxiBooking;

/**
 * Interface defining the UI operations for the Customer Home screen.
 */
public interface HomeCustomerView {

    /**
     * Displays the welcome greeting on the dashboard.
     * @param username The name or identifier to be displayed.
     */
    void setWelcomeMessage(String username);

    /**
     * Navigates to the ride booking flow.
     * @param username The current user's identifier.
     */
    void navigateToBookRide(String username);

    /**
     * Navigates to the profile management screen.
     * @param username The current user's identifier.
     */
    void navigateToProfile(String username);

    /**
     * Navigates to the ride history screen.
     */
    void navigateToHistory();

    /**
     * Updates the UI to show the number of pending evaluations.
     * @param size The count of pending items.
     */
    void showNotificationBadge(int size);

    /**
     * Navigates to the user's ratings screen.
     */
    void navigateToRatings();

    /**
     * Navigates to the submitted requests screen.
     */
    void navigateToRequests();

    /**
     * Launches the evaluation screen for a specific booking.
     * @param booking The booking object to be evaluated.
     */
    void navigateToDriverEvaluation(TaxiBooking booking);

    /**
     * Hides the overlay list of pending reviews.
     */
    void hidePendingList();

    /**
     * Populates the UI list with bookings requiring review.
     * @param pendingBookings The list of bookings to display.
     */
    void setupPendingList(List<TaxiBooking> pendingBookings);

    /**
     * Displays a message indicating no reviews are available.
     */
    void showNoReviewsMessage();
}