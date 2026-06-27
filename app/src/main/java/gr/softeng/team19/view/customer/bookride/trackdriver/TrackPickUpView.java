package gr.softeng.team19.view.customer.bookride.trackdriver;

import org.osmdroid.util.GeoPoint;
import gr.softeng.team19.domain.TaxiBooking;

/**
 * Interface defining the UI operations for the ride tracking screen.
 */
public interface TrackPickUpView {

    /**
     * Initializes the map with markers for the driver, customer, and destination.
     * @param driverPoint Current coordinates of the taxi.
     * @param customerPoint Pickup location coordinates.
     * @param destinationPoint Final destination coordinates.
     */
    void setupDriverAndCustomerMarkers(GeoPoint driverPoint, GeoPoint customerPoint, GeoPoint destinationPoint);

    /**
     * Updates the driver details section in the UI.
     * @param name Driver's full name.
     * @param carModel Vehicle model description.
     * @param plate Vehicle license plate.
     * @param rating Driver's average star rating.
     */
    void setDriverInfo(String name, String carModel, String plate, double rating);

    /**
     * Updates the Estimated Time of Arrival display.
     * @param time Formatted time string (e.g., "5 min").
     */
    void setETA(String time);

    /**
     * Updates the current status message of the ride.
     * @param status Description of the current phase (e.g., "Driver arriving").
     */
    void setStatus(String status);

    /**
     * Moves the driver icon to a new position on the map.
     * @param lat New latitude.
     * @param lon New longitude.
     */
    void updateDriverLocationOnMap(double lat, double lon);

    /**
     * Displays a temporary toast or alert message.
     * @param message The text to be displayed.
     */
    void showMessage(String message);

    /**
     * Navigates the user back to the home dashboard.
     * @param username The current user identifier.
     */
    void navigateToHome(String username);

    /**
     * Displays an overlay prompting the user to review the driver.
     * @param booking The booking object associated with the review.
     */
    void showReviewPrompt(TaxiBooking booking);

    /**
     * Removes the review prompt overlay from the UI.
     */
    void deleteReviewPrompt();

    /**
     * Navigates to the evaluation screen.
     * @param booking The booking object to be evaluated.
     */
    void navigateToDriverEvaluation(TaxiBooking booking);

    /**
     * Disables or hides the call driver action.
     */
    void setCallButtonOff();

    /**
     * Disables or hides the cancel ride action.
     */
    void setCancelButtonOff();

    /**
     * Navigates to the payment screen upon ride completion.
     * @param amount The final calculated fare.
     * @param booking The completed booking details.
     */
    void navigateToPayment(double amount, TaxiBooking booking);

    /**
     * Displays the skip/fast-forward button for simulation.
     */
    void showSkip();

    /**
     * Hides the skip/fast-forward button.
     */
    void hideSkip();
}