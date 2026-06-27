package gr.softeng.team19.view.customer.payment;

import android.os.Handler;
import android.os.Looper;
import java.time.format.DateTimeFormatter;
import gr.softeng.team19.domain.Payment;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.memorydao.PaymentDAOMemory;
import gr.softeng.team19.memorydao.TaxiBookingDAOMemory;

/**
 * Presenter handling payment logic, bank simulation, and navigation.
 */
public class PaymentPresenter {

    /** Interface to abstract thread scheduling for testing. */
    public interface Scheduler {
        /**
         * @param task The logic to run.
         * @param delayMillis Delay in milliseconds.
         */
        void execute(Runnable task, long delayMillis);
    }

    /** Interface to abstract random number generation for testing. */
    public interface RandomProvider {
        /** @return A random double between 0.0 and 1.0. */
        double nextDouble();
    }

    private PaymentView view;
    private TaxiBooking currentBooking;
    private double amount;
    private Scheduler scheduler;
    private RandomProvider random;

    /**
     * Standard constructor used by the application.
     * @param view The view implementation.
     * @param bookingID Unique ID of the ride.
     * @param amount Final cost to pay.
     */
    public PaymentPresenter(PaymentView view, String bookingID, double amount) {
        this(view, bookingID, amount,
                (task, delay) -> new Handler(Looper.getMainLooper()).postDelayed(task, delay),
                Math::random
        );
    }

    /**
     * Flexible constructor for unit testing.
     * @param view The view implementation.
     * @param bookingID Unique ID of the ride.
     * @param amount Final cost to pay.
     * @param scheduler Custom scheduler for delays.
     * @param random Custom random generator.
     */
    public PaymentPresenter(PaymentView view, String bookingID, double amount, Scheduler scheduler, RandomProvider random) {
        this.view = view;
        this.amount = amount;
        this.scheduler = scheduler;
        this.random = random;
        this.currentBooking = new TaxiBookingDAOMemory().find(bookingID);

        if (currentBooking != null && view != null) {
            if (currentBooking.getRoute() != null && currentBooking.getRoute().getStartTime() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                String startTimeStr = currentBooking.getRoute().getStartTime().format(formatter);
                String endTimeStr = currentBooking.getRoute().getEndTime().format(formatter);

                view.displayBookingDetails(
                        currentBooking.getBookingID(),
                        startTimeStr,
                        endTimeStr,
                        currentBooking.getRoute().getTotalDistance(),
                        amount
                );
            }
        }
    }

    /**
     * Initiates the payment process based on selected method.
     * @param method The chosen payment method (Cash or Card).
     */
    public void onConfirmClicked(Payment.PaymentMethod method) {
        if (currentBooking == null) return;

        Payment p = currentBooking.payRoute(amount, method);
        PaymentDAOMemory paymentDAO = new PaymentDAOMemory();
        paymentDAO.save(p);

        if ("PaymentCompleted".equals(p.getStatus())) {
            view.showCashPaymentPrompt();
        } else {
            p.setStatus("PaymentCompleted");
            view.showCardPaymentPrompt();
        }
    }

    /**
     * Simulates waiting for driver confirmation for cash payments.
     */
    public void onCashConfirmed() {
        if (currentBooking == null) return;
        view.showProcessing("Waiting for driver's confirmation...");

        scheduler.execute(() -> {
            view.showSuccessPaymentPrompt();

            scheduler.execute(() -> {
                view.navigateToHome(currentBooking);
            }, 2500);

        }, 4500);
    }

    /**
     * Handles payment processing using a previously saved credit card.
     */
    public void onSavedCardConfirmed() {
        if (currentBooking == null) return;
        view.showProcessing("Connecting with bank...");

        scheduler.execute(() -> {
            if (random.nextDouble() < 0.3) {
                view.showErrorPaymentPrompt();
            } else {
                currentBooking.setStatus("BookingCompleted");
                view.showSuccessPaymentPrompt();

                scheduler.execute(() -> {
                    view.navigateToHome(currentBooking);
                }, 2000);
            }
        }, 4500);
    }

    /**
     * Handles payment processing with a new credit card number.
     * @param number The new credit card string.
     */
    public void onNewCardConfirmed(String number) {
        if (currentBooking == null) return;
        String oldCard = currentBooking.getCustomer().getCreditCardNumber();
        currentBooking.getCustomer().setCreditCardNumber(number);

        view.showProcessing("Connecting with bank...");

        scheduler.execute(() -> {
            if (random.nextDouble() < 0.3) {
                currentBooking.getCustomer().setCreditCardNumber(oldCard);
                view.showErrorPaymentPrompt();
            } else {
                currentBooking.setStatus("BookingCompleted");
                view.showSuccessPaymentPrompt();

                scheduler.execute(() -> {
                    view.navigateToHome(currentBooking);
                }, 1800);
            }
        }, 4500);
    }
}