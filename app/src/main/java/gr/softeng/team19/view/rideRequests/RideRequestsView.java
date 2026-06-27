package gr.softeng.team19.view.rideRequests;

import java.util.List;
import gr.softeng.team19.domain.TaxiRideRequest;

/**
 * Interface that defines the UI actions for the Ride Requests screen.
 * It is implemented by the Activity and called by the Presenter.
 */
public interface RideRequestsView {

    /**
     * Displays a list of ride requests on the screen.
     * @param requests The list of TaxiRideRequest objects to show.
     */
    void showRequestsList(List<TaxiRideRequest> requests);

    /**
     * Displays a message or layout indicating that no requests were found.
     */
    void showEmptyState();

    /**
     * Shows a short message to the user.
     * @param message The text content of the message.
     */
    void showMessage(String message);

    /**
     * Commands the UI to close the current screen and return to the previous one.
     */
    void navigateBack();
}