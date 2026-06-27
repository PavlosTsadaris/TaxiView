package gr.softeng.team19.view.customer.payment;

import gr.softeng.team19.domain.TaxiBooking;

/**
 * Manual stub implementation of PaymentView used for state-based unit testing.
 * Records interactions with payment prompts and navigation events.
 */
public class PaymentViewStub implements PaymentView {

    public String displayedBookingID;
    public double displayedAmount;
    public boolean cashPromptShown = false;
    public boolean cardPromptShown = false;
    public boolean successPromptShown = false;
    public boolean errorPromptShown = false;
    public boolean newCardPromptShown = false;
    public String processingMessage;
    public boolean homeNavigated = false;
    public TaxiBooking navigatedBooking;

    /**
     * Captures basic ride details displayed to the user before payment.
     */
    @Override
    public void displayBookingDetails(String id, String start, String end, double dist, double amt) {
        this.displayedBookingID = id;
        this.displayedAmount = amt;
    }

    /** Marks the cash payment confirmation UI as displayed. */
    @Override
    public void showCashPaymentPrompt() {
        this.cashPromptShown = true;
    }

    /** Marks the credit card selection/confirmation UI as displayed. */
    @Override
    public void showCardPaymentPrompt() {
        this.cardPromptShown = true;
    }

    /** Marks the transaction success message as displayed. */
    @Override
    public void showSuccessPaymentPrompt() {
        this.successPromptShown = true;
    }

    /** Marks the transaction failure or bank rejection message as displayed. */
    @Override
    public void showErrorPaymentPrompt() {
        this.errorPromptShown = true;
    }

    /**
     * Captures the status message shown during bank communication.
     * @param message Text such as "Processing..." or "Contacting Bank...".
     */
    @Override
    public void showProcessing(String message) {
        this.processingMessage = message;
    }

    /** Marks the UI for entering new card details as displayed. */
    @Override
    public void showNewCardPrompt() {
        this.newCardPromptShown = true;
    }

    /**
     * Records successful navigation back to the home screen.
     * @param booking The final booking object with payment confirmation.
     */
    @Override
    public void navigateToHome(TaxiBooking booking) {
        this.homeNavigated = true;
        this.navigatedBooking = booking;
    }
}