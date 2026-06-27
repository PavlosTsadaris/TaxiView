package gr.softeng.team19.view.driver.chooselocation;

import gr.softeng.team19.memorydao.MockRideData;

/**
 * Interface for the location selection screen for drivers.
 * It defines how the screen should display map updates, location lists, and navigation.
 */
public interface DriverChooseLocationView {

    /**
     * Opens a popup list showing the names of available starting points.
     * @param locationNames A list of address names for the driver to pick from.
     */
    void showLocationDialog(String[] locationNames);

    /**
     * Enables or disables the "Go Online" button.
     * @param isEnabled True if the button should be clickable, false otherwise.
     */
    void setButtonEnabled(boolean isEnabled);

    /**
     * Changes the address text displayed on the screen.
     * @param address The human-readable name of the selected location.
     */
    void updateLocationText(String address);

    /**
     * Moves the map camera and places a marker on the chosen coordinates.
     * @param location The object containing the map point and name.
     */
    void updateMapLocation(MockRideData.DemoLocation location);

    /**
     * Confirms the driver is online and moves them to the main driver dashboard.
     * @param locationName The name of the starting point to show in a confirmation message.
     */
    void navigateToOnlineMode(String locationName);

    /**
     * Closes this screen and returns to the previous menu.
     */
    void navigateBack();

    /**
     * Updates the internal state of the activity with the new location data.
     * @param newLocation The data object for the selected location.
     */
    void onLocationSelected(MockRideData.DemoLocation newLocation);
}