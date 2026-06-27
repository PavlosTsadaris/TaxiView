package gr.softeng.team19.view.customer.payment;

import gr.softeng.team19.domain.TaxiBooking;

/**
 * Interface defining the UI operations for the Payment screen.
 */
public interface PaymentView {

    /**
     * Updates the UI with ride information and final cost.
     * @param id The unique booking identifier.
     * @param start The formatted pickup time.
     * @param end The formatted drop-off time.
     * @param dist The total kilometers traveled.
     * @param amt The total amount due.
     */
    void displayBookingDetails(String id, String start, String end, double dist, double amt);

    /**
     * Displays a confirmation dialog for cash payments.
     */
    void showCashPaymentPrompt();

    /**
     * Displays options for card payment selection.
     */
    void showCardPaymentPrompt();

    /**
     * Displays a success message after successful transaction.
     */
    void showSuccessPaymentPrompt();

    /**
     * Displays an error message when a transaction fails.
     */
    void showErrorPaymentPrompt();

    /**
     * Shows a loading overlay with a status update.
     * @param message The text to display during processing.
     */
    void showProcessing(String message);

    /**
     * Displays an input form for a new credit card.
     */
    void showNewCardPrompt();

    /**
     * Navigates the user back to the home screen.
     * @param booking The completed booking data.
     */
    void navigateToHome(TaxiBooking booking);
}