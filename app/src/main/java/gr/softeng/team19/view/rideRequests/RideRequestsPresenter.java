package gr.softeng.team19.view.rideRequests;

import java.util.ArrayList;
import java.util.List;
import gr.softeng.team19.dao.TaxiRideRequestDAO;
import gr.softeng.team19.domain.TaxiRideRequest;
import gr.softeng.team19.memorydao.TaxiRideRequestDAOMemory;

/**
 * Presenter class that controls the logic for showing ride requests.
 * It sits between the data (DAO) and the UI (View).
 */
public class RideRequestsPresenter {

    private RideRequestsView view;
    private TaxiRideRequestDAO requestDAO;
    private String currentUsername;

    /**
     * Constructor that sets up the presenter and starts loading data.
     * @param view The UI interface to communicate with.
     * @param username The username of the customer looking at their requests.
     */
    public RideRequestsPresenter(RideRequestsView view, String username) {
        this.view = view;
        this.currentUsername = username;
        this.requestDAO = new TaxiRideRequestDAOMemory();
        loadRequests();
    }

    /**
     * Gets all requests from the database and filters them by username.
     * It tells the view to show either the list of requests or an empty screen.
     */
    private void loadRequests() {
        List<TaxiRideRequest> allRequests = requestDAO.findAll();
        List<TaxiRideRequest> userRequests = new ArrayList<>();

        // If no user is logged in, show the empty screen
        if (currentUsername == null) {
            view.showEmptyState();
            return;
        }

        // Loop through all requests and keep only the ones belonging to this user
        for (TaxiRideRequest request : allRequests) {
            if (request.getCustomer() != null &&
                    request.getCustomer().getUserName().equals(currentUsername)) {
                userRequests.add(request);
            }
        }

        // Check if we found any requests and update the UI
        if (userRequests.isEmpty()) {
            view.showEmptyState();
        } else {
            view.showRequestsList(userRequests);
        }
    }

    /**
     * Tells the view to go back to the previous screen.
     */
    public void goBack() {
        view.navigateBack();
    }
}