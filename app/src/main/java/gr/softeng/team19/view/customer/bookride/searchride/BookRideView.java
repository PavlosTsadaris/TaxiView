package gr.softeng.team19.view.customer.bookride.searchride;

import java.util.ArrayList;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.domain.TaxiDriver;

/**
 * Interface for the main booking screen.
 * It manages everything from picking a location to choosing a driver
 * and handling the waiting period while the driver responds.
 */
public interface BookRideView {

    /**
     * Resets the screen to its original state (showing address confirmation).
     * @param lastAddress The address text to show.
     */
    void resetUI(String lastAddress);

    /**
     * Shows a list of addresses for the user to choose as their pickup spot.
     */
    void showLocationSelectionDialog(String[] locationNames);

    /**
     * Changes the main status message (e.g., "Searching...").
     */
    void setStatusText(int resId);

    /**
     * Updates the address name shown on the screen.
     */
    void setAddressText(String address);

    /**
     * Changes the label on the main action button.
     */
    void setButtonText(int resId);

    /**
     * Enables or disables the primary confirm/search button.
     */
    void setButtonEnabled(boolean isEnabled);

    /**
     * Locks or unlocks the driver list so the user can't click during a process.
     */
    void setRecycleListEnabled(boolean isEnabled);

    /**
     * Displays all nearby taxi drivers in a list.
     */
    void showDriverList(ArrayList<TaxiDriver> drivers);

    /**
     * Hides the driver list and returns to the location view.
     */
    void closeDriverList();

    /**
     * Goes back to the destination selection screen.
     */
    void navigateToDestinationScreen();

    /**
     * Goes back to the main app dashboard.
     */
    void navigateToHomeScreen();

    /**
     * Moves to the screen where the user tracks the driver's arrival.
     */
    void navigateToRideTracking(TaxiBooking booking);

    /**
     * Opens the screen for the user to rate the driver.
     */
    void navigateToDriverEvaluation(TaxiBooking booking);

    /**
     * Notifies the user that a previously selected destination was changed.
     */
    void showRemoveSelectedDestination(String destination);

    /**
     * Shows a "Waiting" popup after selecting a driver.
     */
    void showWaitingOverlay(String driverName);

    /**
     * Hides the "Waiting" popup.
     */
    void hideWaitingOverlay();

    /**
     * Shows a message when a driver selection is cancelled.
     */
    void showCanceledDriver(String name);

    /**
     * Shows a message confirming a driver was picked.
     */
    void showSelectedDriver(String name);

    /**
     * Updates the status to show the driver said no.
     */
    void showDriverRejectedMessage(String name);

    /**
     * Updates the status to show the driver said yes.
     */
    void showDriverAcceptedMessage(String name);

    /**
     * Shows a success icon (checkmark) on the overlay.
     */
    void showSuccessState();

    /**
     * Shows an error/cancel icon on the overlay.
     */
    void showCancelState();

    /**
     * Resets the overlay so the user can try again.
     */
    void hideCancelState();

    /**
     * Displays a window asking the user if they want to rate the trip.
     */
    void showReviewPrompt(TaxiBooking booking);

    /**
     * Removes the rating prompt window.
     */
    void deleteReviewPrompt();
}