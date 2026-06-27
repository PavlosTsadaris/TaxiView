package gr.softeng.team19.view.customer.evaluation;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;

import gr.softeng.team19.domain.DriverRating;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.memorydao.DriverRatingDAOMemory;
import gr.softeng.team19.memorydao.TaxiBookingDAOMemory;

/**
 * Presenter handling the logic for driver evaluation and rating submission.
 */
public class EvaluationPresenter {

    /**
     * Interface for scheduling delayed tasks to support unit testing.
     */
    public interface Scheduler {
        /**
         * @param task The runnable logic to execute.
         * @param delayMillis Delay in milliseconds.
         */
        void execute(Runnable task, long delayMillis);
    }

    private EvaluationView view;
    private TaxiBooking currentBooking;
    private TaxiBookingDAOMemory bookingDAO;
    private String bookingID;
    private DriverRatingDAOMemory ratingsDAO;
    private Scheduler scheduler;

    /**
     * Standard constructor for application use.
     * @param view The view implementation.
     * @param bookingID The identifier of the ride being evaluated.
     */
    public EvaluationPresenter(EvaluationView view, String bookingID) {
        this(view, bookingID, new Scheduler() {
            private Handler handler = new Handler(Looper.getMainLooper());
            @Override
            public void execute(Runnable task, long delayMillis) {
                handler.postDelayed(task, delayMillis);
            }
        });
    }

    /**
     * Constructor used for unit testing with dependency injection.
     * @param view The view implementation.
     * @param bookingID The identifier of the ride being evaluated.
     * @param scheduler Custom scheduler for timing control.
     */
    public EvaluationPresenter(EvaluationView view, String bookingID, Scheduler scheduler) {
        this.view = view;
        this.bookingID = bookingID;
        this.scheduler = scheduler;
        this.bookingDAO = new TaxiBookingDAOMemory();
        this.ratingsDAO = new DriverRatingDAOMemory();
        this.currentBooking = bookingDAO.find(bookingID);

        initializeView();
    }

    /**
     * Populates view fields with ride and driver data.
     */
    private void initializeView() {
        if (currentBooking != null) {
            view.setDriverName(currentBooking.getTaxiDriver().getName() + " " + currentBooking.getTaxiDriver().getSurname());
            view.setCustomerName("Passenger: " + currentBooking.getCustomer().getName());
            view.setCarInfo(currentBooking.getTaxiDriver().getVehicle().getModel());
            view.setBookingDetails("Date: " + currentBooking.getDate().toString() + " | ID: #" + currentBooking.getBookingID());
        }
    }

    /**
     * Validates input and saves the evaluation to the persistence layer.
     */
    public void onSubmitRating() {
        if (!view.areRatingsValid()) {
            view.showError("Please rate all categories before submitting!");
            return;
        }

        if (currentBooking != null) {
            DriverRating currentRating = currentBooking.createEvaluation(view.getRatings(), view.getComment(), currentBooking);

            currentBooking.setEvaluated(true);
            bookingDAO.save(currentBooking);
            ratingsDAO.save(currentRating);

            scheduler.execute(() -> {
                view.navigateToRideTracking(bookingID);
            }, 1500);
        }
    }

    /**
     * Aborts the evaluation process and returns to the previous screen.
     */
    public void onCancelRating() {
        view.navigateToCancelRating(bookingID);
    }
}