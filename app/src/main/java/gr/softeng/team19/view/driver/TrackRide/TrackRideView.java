package gr.softeng.team19.view.driver.TrackRide;

import org.osmdroid.util.GeoPoint;

/**
 * Interface for the live ride tracking screen.
 * It defines how the map, the taxi's movement, and the ride status
 * should be updated during an active trip.
 */
public interface TrackRideView {

    /**
     * Prepares the map by placing markers for the driver, customer, and destination.
     * @param driverPoint Current taxi location.
     * @param customerPoint Where the customer is waiting.
     * @param destinationPoint The final drop-off location.
     */
    void setupMap(GeoPoint driverPoint, GeoPoint customerPoint, GeoPoint destinationPoint);

    /**
     * Updates the status labels to show if the taxi is heading to pickup or drop-off.
     * @param pickup True if picking up, false if dropping off.
     * @param destination The name of the target address.
     */
    void setTxtRideInfo(boolean pickup, String destination);

    /**
     * Moves the car icon on the map to show the driver's real-time position.
     * @param lat New latitude.
     * @param lon New longitude.
     */
    void updateDriverLocationOnMap(double lat, double lon);

    /**
     * Displays a quick feedback message to the driver.
     * @param message The text to show.
     */
    void showMessage(String message);

    /**
     * Opens the payment screen once the ride reaches the final destination.
     * @param amount The total fare to be charged.
     * @param bookingID The ID of the finished trip.
     */
    void navigateToPayment(Double amount, String bookingID);

    /**
     * Shows or hides the button used to confirm arrival at the pickup point.
     * @param b True to show, false to hide.
     */
    void setButtonArrival(boolean b);

    /**
     * Shows or hides the button used to end the ride at the destination.
     * @param b True to show, false to hide.
     */
    void setButtonEndRide(boolean b);

    /**
     * Updates the text showing how many minutes are left until arrival.
     * @param b True to show the ETA label.
     * @param ETA The time string (e.g., "5" or "Arriving now...").
     */
    void setTextETA(boolean b, String ETA);

    /**
     * Shows the "Skip" button used for testing the simulation.
     */
    void showSkip();

    /**
     * Hides the "Skip" button once it is no longer needed.
     */
    void hideSkip();
}