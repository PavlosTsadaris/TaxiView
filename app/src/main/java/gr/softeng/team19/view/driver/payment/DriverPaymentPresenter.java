package gr.softeng.team19.view.driver.payment;

import android.os.Handler;
import android.os.Looper;

import gr.softeng.team19.dao.PaymentDAO;
import gr.softeng.team19.domain.Payment;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.memorydao.PaymentDAOMemory;
import gr.softeng.team19.memorydao.TaxiBookingDAOMemory;

/**
 * Presenter that manages the payment logic for the driver.
 * It handles checking the price entered by the driver, simulating a bank processing
 * delay, and saving the final payment to the database.
 */
public class DriverPaymentPresenter {

    /**
     * Interface to help manage timing (like waiting for a "Success" message).
     * This is useful for testing so we don't have to wait for real seconds to pass.
     */
    public interface Scheduler {
        void execute(Runnable task, long delayMillis);
    }

    private DriverPaymentPresenter.Scheduler scheduler;
    private DriverPaymentView view;
    private PaymentDAO paymentDAO;
    private TaxiBooking booking;
    private Double amount;

    /**
     * Sets up the presenter, finds the specific ride (booking), and shows the customer's name.
     * @param view The UI interface for the payment screen.
     * @param bookingID The unique ID of the ride.
     * @param amount The initial estimated price.
     */
    public DriverPaymentPresenter(DriverPaymentView view, String bookingID, Double amount) {
        this.view = view;
        this.paymentDAO = new PaymentDAOMemory();
        this.amount = amount;

        // Find the ride details in the database
        this.booking = new TaxiBookingDAOMemory().find(bookingID);

        if (booking != null) {
            view.setCustomerName(booking.getCustomer().getName() + " " + booking.getCustomer().getSurname());
            // Round the price to 2 decimal places (e.g., 10.50)
            this.amount = Math.ceil(amount * 100) / 100.0;
            view.setAmount(this.amount);
        }

        // Standard Android timer for the loading screen
        this.scheduler = (task, delay) -> new Handler(Looper.getMainLooper()).postDelayed(task, delay);
    }

    /**
     * Constructor used for unit testing.
     */
    public DriverPaymentPresenter(DriverPaymentView view, String bookingID, Double amount, Scheduler testScheduler) {
        this(view, bookingID, amount);
        this.scheduler = testScheduler;
    }

    /**
     * Handles the logic when the driver clicks "Confirm Payment."
     * It checks if the amount is valid and follows a specific flow:
     * 1. Show loading -> 2. Process payment -> 3. Show success -> 4. Go Home.
     * @param amountStr The price text entered by the driver.
     */
    public void onConfirmPayment(String amountStr) {
        // Validation: Don't allow empty fields
        if (amountStr == null || amountStr.trim().isEmpty()) {
            view.showInputError("Amount cannot be empty");
            return;
        }

        try {
            // Fix input if the user used a comma instead of a dot
            double finalAmount = Double.parseDouble(amountStr.replace(",", "."));

            // Validation: Price must be positive
            if (finalAmount <= 0) {
                view.showInputError("Amount must be greater than 0");
                return;
            }

            // Security Rule: The driver cannot charge more than 33% extra over the estimate
            if (finalAmount > (amount + amount / 3)) {
                view.showInputError("Amount is too high! Check again.");
                return;
            }

            // Step 1: Show the "Processing..." screen
            view.showWaitingState();

            // Step 2: Wait 2.5 seconds to simulate bank processing
            scheduler.execute(() -> {
                Payment payment = booking.payRoute(finalAmount, Payment.PaymentMethod.PayByCash);
                paymentDAO.save(payment);

                // Step 3: Show the success icon
                view.showSuccessState();

                // Step 4: Wait 1.5 seconds so the driver can see the success icon, then go home
                scheduler.execute(() -> {
                    view.navigateToHome(booking.getTaxiDriver().getUserName());
                }, 1500);

            }, 2500);

        } catch (NumberFormatException e) {
            view.showInputError("Invalid format. Please enter a number (e.g. 12.50)");
        }
    }
}