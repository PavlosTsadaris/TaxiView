package gr.softeng.team19.view.customer.bookride.searchride;

import java.util.ArrayList;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.domain.TaxiDriver;

/**
 * A stub implementation of BookRideView for testing.
 * It records UI changes and navigation events to verify the Presenter's logic.
 */
public class BookRideViewStub implements BookRideView {

    // UI State Variables
    public String currentAddressText;
    public int currentStatusTextResId;
    public int currentButtonTextResId;
    public boolean isButtonEnabled;
    public boolean waitingOverlayShown = false;
    public boolean waitingOverlayHidden = false;
    public boolean successStateShown = false;
    public boolean cancelStateShown = false;
    public boolean cancelStateHidden = false;
    public boolean recycleListEnabled = true;

    // Navigation Flags
    public boolean destinationScreenNavigated = false;
    public boolean homeScreenNavigated = false;
    public boolean rideTrackingNavigated = false;
    public boolean driverEvaluationNavigated = false;

    // Data Capture for verification
    public TaxiBooking bookingForReview;
    public boolean reviewPromptDeleted = false;
    public ArrayList<TaxiDriver> displayedDrivers;
    public String[] locationDialogNames;
    public String removedDestinationFromDialog;
    public String selectedDriverName;
    public String canceledDriverName;
    public String rejectedDriverName;
    public String acceptedDriverName;

    /** Resets UI to initial state. */
    @Override
    public void resetUI(String lastAddress) {
        this.currentAddressText = lastAddress;
        this.isButtonEnabled = true;
    }

    /** Stores location names for the selection dialog. */
    @Override
    public void showLocationSelectionDialog(String[] locationNames) {
        this.locationDialogNames = locationNames;
    }

    @Override
    public void setStatusText(int resId) {
        this.currentStatusTextResId = resId;
    }

    @Override
    public void setAddressText(String address) {
        this.currentAddressText = address;
    }

    @Override
    public void setButtonText(int resId) {
        this.currentButtonTextResId = resId;
    }

    @Override
    public void setButtonEnabled(boolean isEnabled) {
        this.isButtonEnabled = isEnabled;
    }

    @Override
    public void setRecycleListEnabled(boolean isEnabled) {
        this.recycleListEnabled = isEnabled;
    }

    /** Captures the list of drivers found by the search. */
    @Override
    public void showDriverList(ArrayList<TaxiDriver> drivers) {
        this.displayedDrivers = drivers;
    }

    @Override
    public void closeDriverList() {
        // UI cleanup logic
    }

    /** Confirms navigation to destination selection. */
    @Override
    public void navigateToDestinationScreen() {
        this.destinationScreenNavigated = true;
    }

    /** Confirms navigation back to home. */
    @Override
    public void navigateToHomeScreen() {
        this.homeScreenNavigated = true;
    }

    /** Confirms navigation to the live tracking screen. */
    @Override
    public void navigateToRideTracking(TaxiBooking booking) {
        this.rideTrackingNavigated = true;
    }

    /** Confirms navigation to the rating screen. */
    @Override
    public void navigateToDriverEvaluation(TaxiBooking booking) {
        this.driverEvaluationNavigated = true;
    }

    /** Captures which location was removed from the list. */
    @Override
    public void showRemoveSelectedDestination(String destination) {
        this.removedDestinationFromDialog = destination;
    }

    /** Records that the "Searching for Driver" overlay appeared. */
    @Override
    public void showWaitingOverlay(String driverName) {
        this.waitingOverlayShown = true;
        this.selectedDriverName = driverName;
    }

    @Override
    public void hideWaitingOverlay() {
        this.waitingOverlayHidden = true;
    }

    @Override
    public void showCanceledDriver(String name) {
        this.canceledDriverName = name;
    }

    @Override
    public void showSelectedDriver(String name) {
        this.selectedDriverName = name;
    }

    @Override
    public void showDriverRejectedMessage(String name) {
        this.rejectedDriverName = name;
    }

    @Override
    public void showDriverAcceptedMessage(String name) {
        this.acceptedDriverName = name;
    }

    /** Confirms the ride was successfully booked. */
    @Override
    public void showSuccessState() {
        this.successStateShown = true;
    }

    /** Confirms the booking was canceled. */
    @Override
    public void showCancelState() {
        this.cancelStateShown = true;
    }

    @Override
    public void hideCancelState() {
        this.cancelStateHidden = true;
    }

    /** Shows a prompt to review a past trip. */
    @Override
    public void showReviewPrompt(TaxiBooking booking) {
        this.bookingForReview = booking;
    }

    /** Confirms the review prompt was dismissed. */
    @Override
    public void deleteReviewPrompt() {
        this.reviewPromptDeleted = true;
    }
}