package gr.softeng.team19.view.history;

import java.util.List;
import gr.softeng.team19.domain.TaxiBooking;

/**
 * Manual stub implementation of HistoryView used for state-based unit testing.
 */
public class HistoryViewStub implements HistoryView {

    public List<TaxiBooking> displayedHistory;
    public boolean emptyStateShown = false;
    public double displayedTotalAmount = -1.0;
    public boolean backNavigated = false;
    public String displayedMessage;

    /**
     * Captures the history list provided by the presenter.
     * @param bookings List of previous taxi bookings.
     */
    @Override
    public void showHistoryList(List<TaxiBooking> bookings) {
        this.displayedHistory = bookings;
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
     * Records the calculated total expenditure or earnings.
     * @param total The numerical sum of payments.
     */
    @Override
    public void showTotalAmount(double total) {
        this.displayedTotalAmount = total;
    }

    /**
     * Records that the back navigation request was triggered.
     */
    @Override
    public void navigateBack() {
        this.backNavigated = true;
    }
}