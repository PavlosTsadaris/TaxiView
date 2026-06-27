package gr.softeng.team19.view.driver.rideexecution;

/**
 * Interface for the screen that guides the driver to the customer.
 * It defines how the map should be prepared and how to move to the
 * next stage once the customer is picked up.
 */
public interface RideNavigationView {

    /**
     * Prepares the map by focusing on the customer's pickup point
     * and adding a marker to show the exact location.
     * @param lat The latitude of the pickup spot.
     * @param lon The longitude of the pickup spot.
     * @param locationName The name of the address or landmark.
     */
    void setupMap(double lat, double lon, String locationName);

    /**
     * Shows a quick pop-up message (Toast) to the driver.
     * @param message The text content of the message.
     */
    void showToast(String message);

    /**
     * Moves the driver to the next screen to track the ride progress
     * after the customer has entered the taxi.
     * @param bookingID The unique ID for the active trip.
     */
    void navigateToOngoingRide(String bookingID);

    /**
     * Stops the navigation process and returns the driver to the previous screen.
     */
    void cancel();
}