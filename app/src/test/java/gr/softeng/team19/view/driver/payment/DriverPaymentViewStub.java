package gr.softeng.team19.view.driver.payment;

/**
 * Manual stub implementation of DriverPaymentView used to verify payment UI transitions and errors.
 */
public class DriverPaymentViewStub implements DriverPaymentView {
    public String customerName;
    public double displayedAmount;
    public String inputErrorMessage;
    public boolean waitingStateShown = false;
    public boolean successStateShown = false;
    public String navigatedHomeUser;

    /**
     * Records the customer name to be displayed on the payment screen.
     */
    @Override
    public void setCustomerName(String name) { this.customerName = name; }

    /**
     * Records the initial estimated fare amount shown to the driver.
     */
    @Override
    public void setAmount(double amount) { this.displayedAmount = amount; }

    @Override
    public void showMessage(String message) { /* General messages not tracked */ }

    /**
     * Captures the username used for home screen navigation after success.
     */
    @Override
    public void navigateToHome(String username) { this.navigatedHomeUser = username; }

    /**
     * Captures specific validation error messages (e.g., "Invalid format").
     */
    @Override
    public void showInputError(String errorMsg) { this.inputErrorMessage = errorMsg; }

    /**
     * Records when the "Processing" or "Waiting" UI is displayed.
     */
    @Override
    public void showWaitingState() { this.waitingStateShown = true; }

    /**
     * Records when the processing state is hidden.
     */
    @Override
    public void hideWaitingState() { this.waitingStateShown = false; }

    /**
     * Records when the final success animation or state is triggered.
     */
    @Override
    public void showSuccessState() { this.successStateShown = true; }
}