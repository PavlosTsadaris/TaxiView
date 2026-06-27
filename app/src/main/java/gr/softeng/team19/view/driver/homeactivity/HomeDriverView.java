package gr.softeng.team19.view.driver.homeactivity;

/**
 * Interface defining the UI actions for the Driver's Home Screen.
 * It manages personal stats, navigation to driver tools, and status updates.
 */
public interface HomeDriverView {

    /**
     * Updates the header with a personal greeting for the driver.
     * @param name The display name of the driver.
     */
    void setWelcomeMessage(String name);

    /**
     * Shows the driver's current star rating on the dashboard.
     * @param rating The average rating formatted as a string (e.g., "4.8").
     */
    void setRating(String rating);

    /**
     * Changes the UI to reflect if the driver is Online or Offline.
     * @param isActive True if the driver is currently available for rides.
     */
    void updateLocationStatus(boolean isActive);

    /**
     * Opens the personal profile screen.
     * @param username The driver's unique ID.
     */
    void navigateToProfile(String username);

    /**
     * Opens the history screen showing past completed rides.
     */
    void navigateToHistory();

    /**
     * Displays a popup notification when a new ride request is found nearby.
     * @param message The alert text to show the driver.
     */
    void showRequestNotification(String message);

    /**
     * Opens the screen containing the list of active ride requests.
     */
    void navigateToRequests();

    /**
     * Shows an error message if an action (like viewing requests while offline) fails.
     * @param message The error text to display.
     */
    void showErrorMessage(String message);

    /**
     * Opens the map screen where the driver picks their starting point.
     */
    void navigateToChooseLocation();

    /**
     * Opens the ratings screen to view customer feedback and reviews.
     */
    void navigateToRatings();
}