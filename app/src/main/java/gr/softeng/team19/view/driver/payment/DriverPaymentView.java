package gr.softeng.team19.view.driver.payment;

/**
 * Interface for the Driver's payment screen.
 * It defines the methods needed to show customer info, handle errors,
 * and display the different states of a transaction (processing vs. success).
 */
public interface DriverPaymentView {

    /**
     * Displays the name of the customer who is being charged.
     * @param name The customer's full name.
     */
    void setCustomerName(String name);

    /**
     * Fills the amount field with the calculated fare.
     * @param amount The numerical value of the fare.
     */
    void setAmount(double amount);

    /**
     * Shows a quick message or notification to the driver.
     * @param message The text to display.
     */
    void showMessage(String message);

    /**
     * Takes the driver back to the main dashboard after the payment is finished.
     * @param username The driver's username for the home screen.
     */
    void navigateToHome(String username);

    /**
     * Highlights the amount field and shows an error (e.g., "Price too high").
     * @param errorMsg The error text to show.
     */
    void showInputError(String errorMsg);

    /**
     * Displays a loading overlay to show that the payment is being processed.
     */
    void showWaitingState();

    /**
     * Hides the loading overlay if the process stops or fails.
     */
    void hideWaitingState();

    /**
     * Updates the UI to show a "Success" icon and message once the payment is saved.
     */
    void showSuccessState();
}