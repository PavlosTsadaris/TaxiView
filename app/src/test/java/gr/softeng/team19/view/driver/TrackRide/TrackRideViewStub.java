package gr.softeng.team19.view.driver.TrackRide;

import org.osmdroid.util.GeoPoint;

/**
 * Manual stub implementation of TrackRideView used to verify map updates and button states.
 */
public class TrackRideViewStub implements TrackRideView {
    public boolean mapSetupCalled = false;
    public String rideInfoLabel;
    public String lastMessage;
    public boolean arrivalButtonEnabled = false;
    public boolean endRideButtonEnabled = false;
    public Double navigatedPaymentAmount;
    public String currentETA;

    /**
     * Confirms the map was initialized with the driver, customer, and destination points.
     */
    @Override
    public void setupMap(GeoPoint d, GeoPoint c, GeoPoint dest) {
        this.mapSetupCalled = true;
    }

    /**
     * Captures the text displayed about the current ride destination.
     */
    @Override
    public void setTxtRideInfo(boolean pickup, String destination) {
        this.rideInfoLabel = destination;
    }

    @Override
    public void updateDriverLocationOnMap(double lat, double lon) {
        // Location updates are verified via the domain state in tests
    }

    /**
     * Captures status messages (e.g., "Arrived at Customer") for verification.
     */
    @Override
    public void showMessage(String message) {
        this.lastMessage = message;
    }

    /**
     * Records the final fare amount when the ride completes.
     */
    @Override
    public void navigateToPayment(Double amount, String bookingID) {
        this.navigatedPaymentAmount = amount;
    }

    /**
     * Updates the status of the "Arrived at Pickup" button.
     */
    @Override
    public void setButtonArrival(boolean b) {
        this.arrivalButtonEnabled = b;
    }

    /**
     * Updates the status of the "End Ride" button.
     */
    @Override
    public void setButtonEndRide(boolean b) {
        this.endRideButtonEnabled = b;
    }

    /**
     * Captures the estimated time of arrival (ETA) string.
     */
    @Override
    public void setTextETA(boolean b, String ETA) {
        this.currentETA = ETA;
    }

    @Override public void showSkip() {}
    @Override public void hideSkip() {}
}