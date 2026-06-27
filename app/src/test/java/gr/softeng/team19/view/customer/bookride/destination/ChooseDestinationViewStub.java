package gr.softeng.team19.view.customer.bookride.destination;

import gr.softeng.team19.memorydao.MockRideData;

/**
 * A stub implementation of ChooseDestinationView for testing purposes.
 * It captures UI updates and navigation signals to verify presenter behavior.
 */
public class ChooseDestinationViewStub implements ChooseDestinationView {

    public String[] displayedLocationNames;
    public boolean isConfirmButtonEnabled = false;
    public String displayedAddressText;
    public MockRideData.DemoLocation mapLocation;
    public String navigatedDestination;
    public boolean homeNavigated = false;

    /**
     * Stores the list of location names to be shown in the selection dialog.
     * @param locationNames Array of address strings.
     */
    @Override
    public void showLocationDialog(String[] locationNames) {
        this.displayedLocationNames = locationNames;
    }

    /**
     * Records whether the confirm button was enabled or disabled.
     * @param isEnabled True if the button is active.
     */
    @Override
    public void setButtonEnabled(boolean isEnabled) {
        this.isConfirmButtonEnabled = isEnabled;
    }

    /**
     * Captures the address text displayed on the screen.
     * @param address The formatted address string.
     */
    @Override
    public void updateAddressText(String address) {
        this.displayedAddressText = address;
    }

    /**
     * Records the geographic coordinates used to update the map view.
     * @param location Data object containing latitude and longitude.
     */
    @Override
    public void updateMapLocation(MockRideData.DemoLocation location) {
        this.mapLocation = location;
    }

    /**
     * Confirms navigation to the driver selection screen.
     * @param destinationName The name of the chosen destination.
     */
    @Override
    public void navigateToDriverSelection(String destinationName) {
        this.navigatedDestination = destinationName;
    }

    /**
     * Records when the user cancels and returns to the home screen.
     */
    @Override
    public void navigateBackToHome() {
        this.homeNavigated = true;
    }
}