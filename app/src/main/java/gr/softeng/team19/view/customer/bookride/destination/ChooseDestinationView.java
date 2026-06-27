package gr.softeng.team19.view.customer.bookride.destination;

import gr.softeng.team19.memorydao.MockRideData;

/**
 * Interface for the screen where customers choose their destination.
 * It defines how to show the location list and how to update the map
 * based on the user's selection.
 */
public interface ChooseDestinationView {

    /**
     * Shows a popup list of available addresses for the user to pick from.
     * @param locationNames A list of street names or landmarks.
     */
    void showLocationDialog(String[] locationNames);

    /**
     * Unlocks or locks the "Confirm" button.
     * @param isEnabled True if a destination has been selected.
     */
    void setButtonEnabled(boolean isEnabled);

    /**
     * Updates the address label on the screen with the selected location's name.
     * @param address The name of the destination.
     */
    void updateAddressText(String address);

    /**
     * Moves the map camera and the pin to a specific location.
     * @param location The coordinates and name of the chosen spot.
     */
    void updateMapLocation(MockRideData.DemoLocation location);

    /**
     * Moves the user to the next screen to begin searching for a driver.
     * @param destinationName The name of the selected destination.
     */
    void navigateToDriverSelection(String destinationName);

    /**
     * Closes the selection screen and returns the user to the home dashboard.
     */
    void navigateBackToHome();
}