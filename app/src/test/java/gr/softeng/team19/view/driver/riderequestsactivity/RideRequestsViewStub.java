package gr.softeng.team19.view.driver.riderequestsactivity;

import java.util.ArrayList;
import gr.softeng.team19.domain.TaxiRideRequest;

/**
 * Manual stub implementation of RideRequestsView used to verify UI data updates and navigation.
 */
public class RideRequestsViewStub implements RideRequestsView {
    public ArrayList<TaxiRideRequest> receivedRequests;
    public ArrayList<Double> receivedDistances;
    public String lastMessage;
    public String navigatedRequestId;
    public boolean activityFinished = false;

    /**
     * Captures the list of ride requests and their calculated distances from the driver.
     * @param requests The list of available taxi ride requests.
     * @param distances The distance in kilometers for each request.
     */
    @Override
    public void updateRequestList(ArrayList<TaxiRideRequest> requests, ArrayList<Double> distances) {
        this.receivedRequests = requests;
        this.receivedDistances = distances;
    }

    /**
     * Captures status or error messages for test verification.
     * @param message The text content of the message.
     */
    @Override
    public void showMessage(String message) {
        this.lastMessage = message;
    }

    /**
     * Records when the activity is commanded to close.
     */
    @Override
    public void finishActivity() {
        this.activityFinished = true;
    }

    /**
     * Records the ID of the request that the driver accepted to navigate towards.
     * @param requestID The unique identifier for the taxi request.
     */
    @Override
    public void navigateToNavigationMap(String requestID) {
        this.navigatedRequestId = requestID;
    }
}