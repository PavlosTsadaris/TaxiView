package gr.softeng.team19.view.driver.rideexecution;

/**
 * Manual stub implementation of RideNavigationView used to verify map data and navigation events.
 */
public class RideNavigationViewStub implements RideNavigationView {

    public boolean setupMapCalled = false;
    public double lastLat;
    public double lastLon;
    public String lastLocationName;
    public boolean showToastCalled = false;
    public String navigatedBookingId;
    public boolean cancelCalled = false;

    /**
     * Captures the coordinates and address name for the map display.
     * @param lat The latitude coordinate.
     * @param lon The longitude coordinate.
     * @param locationName The text description of the location.
     */
    @Override
    public void setupMap(double lat, double lon, String locationName) {
        this.setupMapCalled = true;
        this.lastLat = lat;
        this.lastLon = lon;
        this.lastLocationName = locationName;
    }

    /**
     * Records when a notification toast is displayed.
     */
    @Override
    public void showToast(String message) {
        this.showToastCalled = true;
    }

    /**
     * Captures the booking ID when moving from the navigation phase to the active ride phase.
     * @param bookingId The ID of the confirmed ride booking.
     */
    @Override
    public void navigateToOngoingRide(String bookingId) {
        this.navigatedBookingId = bookingId;
    }

    /**
     * Records when the navigation session is canceled.
     */
    @Override
    public void cancel() {
        this.cancelCalled = true;
    }
}