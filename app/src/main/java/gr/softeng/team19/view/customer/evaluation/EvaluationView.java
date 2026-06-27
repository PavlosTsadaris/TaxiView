package gr.softeng.team19.view.customer.evaluation;

import java.util.ArrayList;
import gr.softeng.team19.domain.TaxiBooking;

/**
 * Interface for the driver evaluation view components.
 */
public interface EvaluationView {

    /**
     * Retrieves the rating values entered by the user.
     * @return A list containing the numerical ratings.
     */
    ArrayList<Double> getRatings();

    /**
     * Retrieves the feedback comment provided by the user.
     * @return The comment string.
     */
    String getComment();

    /**
     * Displays the driver's full name.
     * @param name The name to display.
     */
    void setDriverName(String name);

    /**
     * Displays vehicle information.
     * @param info Details about the car.
     */
    void setCarInfo(String info);

    /**
     * Displays the customer's name.
     * @param name The passenger's name.
     */
    void setCustomerName(String name);

    /**
     * Displays summary details about the booking.
     * @param details The text containing booking info.
     */
    void setBookingDetails(String details);

    /**
     * Displays an error message to the user.
     * @param message The error description.
     */
    void showError(String message);

    /**
     * Navigates to the ride tracking screen after submission.
     * @param bookingID The unique identifier for the booking.
     */
    void navigateToRideTracking(String bookingID);


    void navigateToCancelRating(String bookingID);

    /**
     * Checks if the user has provided valid ratings.
     * @return True if ratings are valid, false otherwise.
     */
    boolean areRatingsValid();
}