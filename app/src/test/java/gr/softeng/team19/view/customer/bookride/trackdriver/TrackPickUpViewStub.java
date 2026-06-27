package gr.softeng.team19.view.customer.bookride.trackdriver;

import org.osmdroid.util.GeoPoint;
import gr.softeng.team19.domain.TaxiBooking;

/**
 * Manual stub implementation of TrackPickUpView used to capture UI updates
 * and navigation events during the ride tracking simulation.
 */
public class TrackPickUpViewStub implements TrackPickUpView {

    public String driverName;
    public String eta;
    public String status;
    public String lastMessage;
    public double lastDriverLat, lastDriverLon;

    // Flags to verify navigation and button state changes
    public boolean paymentNavigated = false;
    public double paymentAmount;
    public boolean homeNavigated = false;
    public boolean skipShown = false;
    public boolean skipHidden = false;
    public boolean reviewPromptShown = false;
    public boolean reviewPromptDeleted = false;
    public boolean evaluationNavigated = false;
    public boolean callButtonOff = false;
    public boolean cancelButtonOff = false;

    @Override
    public void setupDriverAndCustomerMarkers(GeoPoint driverPoint, GeoPoint customerPoint, GeoPoint destinationPoint) {
        // Verification is usually handled via status or location updates
    }

    /**
     * Captures the driver's profile information.
     */
    @Override
    public void setDriverInfo(String name, String carModel, String plate, double rating) {
        this.driverName = name;
    }

    /**
     * Records the Estimated Time of Arrival string.
     */
    @Override
    public void setETA(String time) {
        this.eta = time;
    }

    /**
     * Records the status text (e.g., "Driver is 200m away").
     */
    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Captures the driver's geographic coordinates as they move on the map.
     */
    @Override
    public void updateDriverLocationOnMap(double lat, double lon) {
        this.lastDriverLat = lat;
        this.lastDriverLon = lon;
    }

    /**
     * Captures general UI messages or error alerts.
     */
    @Override
    public void showMessage(String message) {
        this.lastMessage = message;
    }

    /**
     * Records when the view navigates back to the main customer home screen.
     */
    @Override
    public void navigateToHome(String username) {
        this.homeNavigated = true;
    }

    /** Marks that the pending review notification was displayed. */
    @Override
    public void showReviewPrompt(TaxiBooking booking) {
        this.reviewPromptShown = true;
    }

    /** Marks that the review notification was dismissed. */
    @Override
    public void deleteReviewPrompt() {
        this.reviewPromptDeleted = true;
    }

    /** Records navigation to the driver rating/evaluation screen. */
    @Override
    public void navigateToDriverEvaluation(TaxiBooking booking) {
        this.evaluationNavigated = true;
    }

    /** Records when the 'Call Driver' button is disabled. */
    @Override
    public void setCallButtonOff() {
        this.callButtonOff = true;
    }

    /** Records when the 'Cancel Ride' button is disabled. */
    @Override
    public void setCancelButtonOff() {
        this.cancelButtonOff = true;
    }

    /**
     * Records navigation to the final payment screen and captures the fare amount.
     */
    @Override
    public void navigateToPayment(double amount, TaxiBooking booking) {
        this.paymentNavigated = true;
        this.paymentAmount = amount;
    }

    /** Marks the simulation 'Skip' button as visible. */
    @Override
    public void showSkip() {
        this.skipShown = true;
    }

    /** Marks the simulation 'Skip' button as hidden. */
    @Override
    public void hideSkip() {
        this.skipHidden = true;
    }
}