package gr.softeng.team19.view.driver.riderequestsactivity;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team19.domain.TaxiDriver;
import gr.softeng.team19.domain.TaxiRideRequest;
import gr.softeng.team19.memorydao.MockRideData;
import gr.softeng.team19.memorydao.TaxiDriverDAOMemory;
import gr.softeng.team19.memorydao.TaxiRideRequestDAOMemory;

/**
 * Presenter that finds and filters ride requests for a driver.
 * It calculates the distance between the driver and the customers,
 * only showing requests that are within a 3km distance.
 */
public class RideRequestsPresenter {

    private RideRequestsView view;
    private TaxiRideRequestDAOMemory requestDAO;
    private String driverUsername;

    /**
     * Connects the presenter to the view and the driver's account.
     * @param view The UI interface for the requests list.
     * @param driverUsername The ID of the driver currently logged in.
     */
    public RideRequestsPresenter(RideRequestsView view, String driverUsername) {
        this.view = view;
        this.driverUsername = driverUsername;
        this.requestDAO = new TaxiRideRequestDAOMemory();
    }

    /**
     * Starts searching for nearby customers.
     * It checks all "PENDING" requests and keeps only the ones close to the driver.
     */
    public void startLoadingProcess() {
        // Step 1: Find the driver to get their current location
        TaxiDriver driver = new TaxiDriverDAOMemory().find(driverUsername);
        if (driver == null) return;

        List<TaxiRideRequest> requests = new TaxiRideRequestDAOMemory().findAll();
        ArrayList<TaxiRideRequest> nearbyRequests = new ArrayList<>();
        ArrayList<Double> distances = new ArrayList<>();

        // Step 2: Loop through every request in the database
        for (TaxiRideRequest request : requests) {
            // Only look at requests that haven't been taken yet
            if (!request.getStatus().equals("PENDING")) continue;

            // Step 3: Calculate how far the driver is from the customer
            double distance = driver.getUserLocation().distanceTo(request.getCustomer().getUserLocation());

            // Rule: Only show customers within a 3.0 km radius
            if (distance <= 3.0) {
                request.setChosenDriver(driver);
                nearbyRequests.add(request);
                distances.add(distance);
            }
        }

        // Step 4: Update the screen with the results
        view.updateRequestList(nearbyRequests, distances);

        if (nearbyRequests.isEmpty()) {
            view.showMessage("No customers found within 3km.");
        }
    }

    /**
     * Logic for when the driver picks a customer from the list.
     * It marks the request as "ACCEPTED" and moves to the navigation map.
     * @param request The specific ride request the driver selected.
     */
    public void onAcceptRequest(TaxiRideRequest request) {
        if (request == null) return;

        // Change the status so other drivers can't take this ride
        request.setStatus("ACCEPTED");
        requestDAO.save(request);

        // Send the driver to the map to start the pickup
        view.navigateToNavigationMap(request.getRequestID());
    }
}