package gr.softeng.team19.view.customer.evaluation;

import java.util.ArrayList;

/**
 * Manual stub implementation of EvaluationView.
 * It stores UI data for verification and provides simulated user input for testing.
 */
public class EvaluationViewStub implements EvaluationView {

    // UI Capture Variables
    public String driverName;
    public String carInfo;
    public String customerName;
    public String bookingDetails;
    public String errorMessage;

    // Navigation Flags
    public boolean rideTrackingNavigated = false;
    public boolean bookingScreenNavigated = false;
    public String navigatedBookingID;

    // Simulation Inputs (Set these in your test)
    private ArrayList<Double> simulatedRatings;
    private String simulatedComment;
    private boolean simulatedIsValid = false;

    /**
     * Sets the star ratings to be returned when the Presenter calls getRatings().
     * @param ratings List of numerical ratings for different categories.
     */
    public void setSimulatedRatings(ArrayList<Double> ratings) {
        this.simulatedRatings = ratings;
    }

    /**
     * Sets the text comment to be returned when the Presenter calls getComment().
     * @param comment The feedback text.
     */
    public void setSimulatedComment(String comment) {
        this.simulatedComment = comment;
    }

    /**
     * Controls the result of the validation check in tests.
     * @param isValid True if the simulated input should pass validation.
     */
    public void setSimulatedValidation(boolean isValid) {
        this.simulatedIsValid = isValid;
    }

    // --- Interface Implementation ---

    @Override
    public ArrayList<Double> getRatings() {
        return simulatedRatings;
    }

    @Override
    public String getComment() {
        return simulatedComment;
    }

    @Override
    public void setDriverName(String name) {
        this.driverName = name;
    }

    @Override
    public void setCarInfo(String info) {
        this.carInfo = info;
    }

    @Override
    public void setCustomerName(String name) {
        this.customerName = name;
    }

    @Override
    public void setBookingDetails(String details) {
        this.bookingDetails = details;
    }

    @Override
    public void showError(String message) {
        this.errorMessage = message;
    }

    /**
     * Records successful navigation to the ride tracking or success screen.
     * @param bookingID The ID of the evaluated booking.
     */
    @Override
    public void navigateToRideTracking(String bookingID) {
        this.rideTrackingNavigated = true;
        this.navigatedBookingID = bookingID;
    }

    /** Records if the user chose to cancel and return to the main booking screen. */
    @Override
    public void navigateToCancelRating(String bookingID) {
        this.bookingScreenNavigated = true;
    }

    @Override
    public boolean areRatingsValid() {
        return simulatedIsValid;
    }
}