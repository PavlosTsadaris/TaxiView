package gr.softeng.team19.view.rideRequests;

import java.util.List;
import gr.softeng.team19.domain.TaxiRideRequest;

/**
 * Manual stub implementation of RideRequestsView used for state-based testing.
 */
public class RideRequestsViewStub implements RideRequestsView {
    public List<TaxiRideRequest> receivedRequests;
    public boolean emptyStateShown = false;
    public boolean navigatedBack = false;

    /**
     * Captures the list of requests sent by the presenter.
     * @param requests The list of taxi ride requests to display.
     */
    @Override
    public void showRequestsList(List<TaxiRideRequest> requests) {
        this.receivedRequests = requests;
    }

    /**
     * Marks the empty state UI as triggered.
     */
    @Override
    public void showEmptyState() {
        this.emptyStateShown = true;
    }

    /**
     * Displays a temporary notification message.
     * @param message The text content of the message.
     */
    @Override
    public void showMessage(String message) { /* Optional verification */ }

    /**
     * Marks the back navigation action as triggered.
     */
    @Override
    public void navigateBack() {
        this.navigatedBack = true;
    }
}