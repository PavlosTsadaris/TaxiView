package gr.softeng.team19.view.driver.homeactivity;

import android.os.Handler;
import android.os.Looper;

import gr.softeng.team19.domain.TaxiDriver;
import gr.softeng.team19.memorydao.TaxiDriverDAOMemory;

/**
 * Presenter for the Driver Home Screen.
 * It manages the driver's dashboard, including their online/offline status,
 * personal stats, and navigation to requests or history.
 */
public class HomeDriverPresenter {

    /**
     * Interface used to delay tasks (like showing a notification).
     * This makes the code easier to test because we can control time during testing.
     */
    public interface Scheduler {
        /**
         * Runs a specific task after a certain number of milliseconds.
         * @param task The action to perform.
         * @param delayMillis How long to wait.
         */
        void execute(Runnable task, long delayMillis);
    }

    private HomeDriverPresenter.Scheduler scheduler;
    private HomeDriverView view;
    private String currentUsername;
    private boolean isLocationActive = false;

    /**
     * Standard constructor that uses the Android system to handle timers.
     * @param view The UI interface for the driver home screen.
     */
    public HomeDriverPresenter(HomeDriverView view) {
        this.view = view;
        this.scheduler = (task, delay) -> new Handler(Looper.getMainLooper()).postDelayed(task, delay);
    }

    /**
     * Constructor used for testing.
     * @param view The UI interface stub.
     * @param testScheduler A custom timer used for unit tests.
     */
    public HomeDriverPresenter(HomeDriverView view, Scheduler testScheduler) {
        this.view = view;
        this.scheduler = testScheduler;
    }

    /**
     * Loads the driver's data (name and rating) when the home page opens.
     * @param username The unique ID of the logged-in driver.
     */
    public void onHomePageLoad(String username) {
        this.currentUsername = username;

        if (username == null) {
            view.setWelcomeMessage("Guest Driver");
            return;
        }

        TaxiDriverDAOMemory driverDAO = new TaxiDriverDAOMemory();
        TaxiDriver driver = driverDAO.find(username);

        if (driver != null) {
            // Combine name and surname for the welcome message
            view.setWelcomeMessage(driver.getName() + " " + driver.getSurname());
            // Format rating to show one decimal (e.g., 4.8)
            String ratingStr = String.format("%.1f", driver.getAverageRating());
            view.setRating(ratingStr);
        } else {
            view.setWelcomeMessage(username);
            view.setRating("0.0");
        }
    }

    /**
     * Switches the driver between Online and Offline.
     * If going Online, it opens the location picker. It also schedules
     * a "New Requests" notification to appear after 3 seconds.
     */
    public void onLocationToggleSelected() {
        if(!isLocationActive){
            view.navigateToChooseLocation();
        }

        // Toggle the status (if true becomes false, if false becomes true)
        isLocationActive = !isLocationActive;

        // Update the driver's availability in the database
        TaxiDriver driver = new TaxiDriverDAOMemory().find(currentUsername);
        if (driver != null) {
            driver.setAvailability(isLocationActive);
        }

        view.updateLocationStatus(isLocationActive);

        if (isLocationActive) {
            // Wait 3 seconds, then show a notification if the driver is still online
            scheduler.execute(() -> {
                if (isLocationActive) {
                    view.showRequestNotification("New requests found nearby!");
                }
            }, 3000);
        }
    }

    /**
     * Opens the Driver's profile screen.
     */
    public void onProfileSelected() {
        view.navigateToProfile(currentUsername);
    }

    /**
     * Opens the ride history screen.
     */
    public void onHistorySelected() {
        view.navigateToHistory();
    }

    /**
     * Opens the ride requests list, but only if the driver is currently Online.
     */
    public void onRequestsListSelected() {
        if (isLocationActive) {
            view.navigateToRequests();
        } else {
            view.showErrorMessage("You must turn on location to receive requests!");
        }
    }

    /**
     * Opens the ratings and reviews screen.
     */
    public void onRatingsSelected() {
        view.navigateToRatings();
    }
}