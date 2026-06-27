package gr.softeng.team19.view.ratings;

import java.util.List;
import gr.softeng.team19.domain.TaxiBooking;

/**
 * UI actions for the Ratings screen.
 */
public interface RatingsView {

    /**
     * Displays the list of rated bookings.
     * @param bookings List of rated trips.
     */
    void showHistoryList(List<TaxiBooking> bookings);

    /**
     * Shows a "no ratings found" screen.
     */
    void showEmptyState();

    /**
     * Shows a brief toast message.
     * @param message Text to show.
     */
    void showMessage(String message);

    /**
     * Returns to the previous screen.
     */
    void navigateBack();
}