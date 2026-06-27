package gr.softeng.team19.view.customer.bookride.destination;

import gr.softeng.team19.memorydao.MockRideData;

/**
 * Presenter that manages picking a destination.
 * it connects the list of available locations to the UI and handles
 * what happens when a user clicks confirm or cancel.
 */
public class ChooseDestinationPresenter {
    private ChooseDestinationView view;

    /**
     * Connects the presenter to the destination selection screen.
     * @param view The UI interface for the destination screen.
     */
    public ChooseDestinationPresenter(ChooseDestinationView view) {
        this.view = view;
    }

    /**
     * Gathers all location names from the database and tells the view to show them in a list.
     */
    public void showLocationSelectionDialog() {
        // Create an array of names from our mock data
        String[] names = new String[MockRideData.LOCATIONS.size()];
        for (int i = 0; i < MockRideData.LOCATIONS.size(); i++) {
            names[i] = MockRideData.LOCATIONS.get(i).name;
        }
        view.showLocationDialog(names);
    }

    /**
     * Called when the user clicks the confirm button.
     * Moves the user to the next screen to find a driver.
     * @param destinationName The name of the place the user wants to go.
     */
    public void onConfirmDestination(String destinationName) {
        view.navigateToDriverSelection(destinationName);
    }

    /**
     * Stops the booking process and returns the user to the home dashboard.
     */
    public void onCancelBooking() {
        view.navigateBackToHome();
    }

    /**
     * Logic for when a user picks a specific place from the list.
     * It updates the map marker, changes the address text, and enables the confirm button.
     * @param which The position of the selected item in the list.
     */
    public void onChooseDestination(int which) {
        MockRideData.DemoLocation selectedLoc = MockRideData.LOCATIONS.get(which);

        // Update the UI with the new location data
        view.updateMapLocation(selectedLoc);
        view.updateAddressText(selectedLoc.name);
        view.setButtonEnabled(true);
    }
}