package gr.softeng.team19.view.driver.chooselocation;

import java.util.List;
import gr.softeng.team19.domain.TaxiDriver;
import gr.softeng.team19.memorydao.MockRideData;
import gr.softeng.team19.memorydao.TaxiDriverDAOMemory;

/**
 * Presenter that manages the logic for a driver starting their shift.
 * It handles picking a starting spot on the map and updating the driver's
 * status to "online" in the database.
 */
public class DriverChooseLocationPresenter {
    private DriverChooseLocationView view;
    private List<MockRideData.DemoLocation> driverLocations;

    /**
     * Constructor that loads a list of pre-defined starting points.
     * @param view The UI interface for the location selection screen.
     */
    public DriverChooseLocationPresenter(DriverChooseLocationView view) {
        this.view = view;
        this.driverLocations = MockRideData.getDriverStartingLocations();
    }

    /**
     * Gathers the names of all available locations and tells the view to show them in a list.
     */
    public void showLocationSelectionDialog() {
        String[] names = new String[driverLocations.size()];
        for (int i = 0; i < driverLocations.size(); i++) {
            names[i] = driverLocations.get(i).name;
        }
        view.showLocationDialog(names);
    }

    /**
     * Logic for when a driver picks a location from the list.
     * It updates the map marker, changes the address text, and enables the "Go Online" button.
     * @param which The position of the selected location in the list.
     */
    public void onChooseLocation(int which) {
        if (which < 0 || which >= MockRideData.LOCATIONS.size()) {
            view.setButtonEnabled(false);
            return;
        }

        MockRideData.DemoLocation selectedLoc = driverLocations.get(which);
        view.onLocationSelected(selectedLoc);
        view.updateMapLocation(selectedLoc);
        view.updateLocationText(selectedLoc.name);
        view.setButtonEnabled(true);
    }

    /**
     * Updates the driver's GPS coordinates and sets them to "Available" in the database.
     * This allows the system to start matching them with customers.
     * @param username The driver's unique ID.
     * @param selectedLoc The map coordinates where the driver is starting.
     */
    public void onGoOnline(String username, MockRideData.DemoLocation selectedLoc) {

        if (selectedLoc == null || username == null) {
            return;
        }

        TaxiDriverDAOMemory driverDAO = new TaxiDriverDAOMemory();
        TaxiDriver driver = driverDAO.find(username);

        if (driver != null) {
            // Save the new latitude and longitude
            driver.setUserLocation(selectedLoc.point.getLatitude(), selectedLoc.point.getLongitude());
            // Set availability to true so they appear on the map for customers
            driver.setAvailability(true);
            driverDAO.save(driver);
        }
        view.navigateToOnlineMode(selectedLoc.name);
    }

    /**
     * Cancels the process and returns the driver to the previous menu.
     */
    public void onCancel() {
        view.navigateBack();
    }
}