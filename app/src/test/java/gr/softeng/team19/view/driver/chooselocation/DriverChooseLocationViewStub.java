package gr.softeng.team19.view.driver.chooselocation;

import gr.softeng.team19.memorydao.MockRideData;

/**
 * Manual stub implementation of DriverChooseLocationView used to track UI interactions
 * during location selection testing.
 */
public class DriverChooseLocationViewStub implements DriverChooseLocationView {

    public String[] lastLocationNames;
    public boolean buttonEnabled = false;
    public String lastLocationText;
    public MockRideData.DemoLocation lastMapLocation;
    public String onlineModeLocation;
    public int navigateBackCount = 0;
    public MockRideData.DemoLocation lastOnLocationSelected;

    /**
     * Captures the list of location names sent to the selection dialog.
     * @param locationNames Array of available location strings.
     */
    @Override
    public void showLocationDialog(String[] locationNames) {
        this.lastLocationNames = locationNames;
    }

    /**
     * Records whether the confirmation button was enabled or disabled.
     * @param isEnabled True if the button is active.
     */
    @Override
    public void setButtonEnabled(boolean isEnabled) {
        this.buttonEnabled = isEnabled;
    }

    /**
     * Captures the address text to be displayed in the UI.
     * @param address The formatted address string.
     */
    @Override
    public void updateLocationText(String address) {
        this.lastLocationText = address;
    }

    /**
     * Records the coordinates used to update the map marker.
     * @param location Data object containing latitude and longitude.
     */
    @Override
    public void updateMapLocation(MockRideData.DemoLocation location) {
        this.lastMapLocation = location;
    }

    /**
     * Captures the location name used when transitioning to the online dashboard.
     * @param locationName Name of the selected spot.
     */
    @Override
    public void navigateToOnlineMode(String locationName) {
        this.onlineModeLocation = locationName;
    }

    /**
     * Increments the counter whenever a back navigation request is made.
     */
    @Override
    public void navigateBack() {
        this.navigateBackCount++;
    }

    /**
     * Records the final selection of a location object.
     * @param newLocation The selected location data.
     */
    @Override
    public void onLocationSelected(MockRideData.DemoLocation newLocation) {
        this.lastOnLocationSelected = newLocation;
    }
}