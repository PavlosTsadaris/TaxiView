package gr.softeng.team19.view.ratings;

import java.util.List;
import gr.softeng.team19.domain.TaxiBooking;

/**
 * Manual stub implementation of RatingsView used for testing UI state and data filtering.
 */
public class RatingsViewStub implements RatingsView {

    public List<TaxiBooking> displayedBookings;
    public boolean emptyStateShown = false;
    public boolean backNavigated = false;
    public String displayedMessage;

    /**
     * Captures the list of bookings provided by the presenter.
     * @param bookings List of evaluated taxi bookings.
     */
    @Override
    public void showHistoryList(List<TaxiBooking> bookings) {
        this.displayedBookings = bookings;
    }

    /**
     * Marks the empty state UI as active.
     */
    @Override
    public void showEmptyState() {
        this.emptyStateShown = true;
    }

    /**
     * Captures notification messages for verification.
     * @param message The text content of the message.
     */
    @Override
    public void showMessage(String message) {
        this.displayedMessage = message;
    }

    /**
     * Records that the back navigation request was triggered.
     */
    @Override
    public void navigateBack() {
        this.backNavigated = true;
    }
}