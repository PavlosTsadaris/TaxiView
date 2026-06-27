package gr.softeng.team19.view.history;

import java.util.List;
import gr.softeng.team19.domain.TaxiBooking;

/**
 * Interface that defines the UI actions for the History screen.
 * It is implemented by the Activity to show ride data and navigation.
 */
public interface HistoryView {

    /**
     * Shows the list of previous rides on the screen.
     * @param bookings The list of past trip data to be displayed.
     */
    void showHistoryList(List<TaxiBooking> bookings);

    /**
     * Shows a message or a layout when no previous rides are found.
     */
    void showEmptyState();

    /**
     * Displays a short text message, like a Toast, to the user.
     * @param message The text content of the message.
     */
    void showMessage(String message);

    /**
     * Shows the total sum of money earned or spent on the screen.
     * @param total The numeric value of the total amount.
     */
    void showTotalAmount(double total);

    /**
     * Closes the history screen and returns to the previous page.
     */
    void navigateBack();
}