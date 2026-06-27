package gr.softeng.team19.view.driver.homeactivity;

/**
 * Manual stub implementation of HomeDriverView used to track UI events and state changes.
 */
public class HomeDriverViewStub implements HomeDriverView {

    public String lastWelcomeMessage;
    public String lastRating;
    public boolean isLocationActive = false;
    public String navigatedProfileUsername;
    public boolean navigatedToHistory = false;
    public String lastNotificationMessage;
    public boolean navigatedToRequests = false;
    public String lastErrorMessage;
    public boolean navigatedToChooseLocation = false;
    public boolean navigatedToRatings = false;

    /**
     * Captures the welcome text displayed to the driver.
     * @param name The formatted name of the driver.
     */
    @Override
    public void setWelcomeMessage(String name) {
        this.lastWelcomeMessage = name;
    }

    /**
     * Records the numerical rating string displayed on the dashboard.
     */
    @Override
    public void setRating(String rating) {
        this.lastRating = rating;
    }

    /**
     * Updates the recorded status of the driver's availability toggle.
     */
    @Override
    public void updateLocationStatus(boolean isActive) {
        this.isLocationActive = isActive;
    }

    /**
     * Captures the username used when opening the profile screen.
     */
    @Override
    public void navigateToProfile(String username) {
        this.navigatedProfileUsername = username;
    }

    /**
     * Marks that the history navigation was triggered.
     */
    @Override
    public void navigateToHistory() {
        this.navigatedToHistory = true;
    }

    /**
     * Captures the content of pop-up notifications for new requests.
     */
    @Override
    public void showRequestNotification(String message) {
        this.lastNotificationMessage = message;
    }

    /**
     * Marks that the ride requests list navigation was triggered.
     */
    @Override
    public void navigateToRequests() {
        this.navigatedToRequests = true;
    }

    /**
     * Captures error messages displayed as toasts or snackbars.
     */
    @Override
    public void showErrorMessage(String message) {
        this.lastErrorMessage = message;
    }

    /**
     * Marks that the location selection screen navigation was triggered.
     */
    @Override
    public void navigateToChooseLocation() {
        this.navigatedToChooseLocation = true;
    }

    /**
     * Marks that the ratings history navigation was triggered.
     */
    @Override
    public void navigateToRatings() {
        this.navigatedToRatings = true;
    }
}