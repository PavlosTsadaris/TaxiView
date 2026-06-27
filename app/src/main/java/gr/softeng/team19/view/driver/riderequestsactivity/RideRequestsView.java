package gr.softeng.team19.view.driver.riderequestsactivity;

import java.util.ArrayList;
import gr.softeng.team19.domain.TaxiRideRequest;

/**
 * Interface for the screen that lists available ride requests.
 * It defines how the list should be updated and how the driver moves
 * from the list to the navigation map.
 */
public interface RideRequestsView {

    /**
     * Refreshes the scrolling list with new requests and their distances.
     * @param requests  The list of customers looking for a ride.
     * @param distances How far away (in km) each customer is from the driver.
     */
    void updateRequestList(ArrayList<TaxiRideRequest> requests, ArrayList<Double> distances);

    /**
     * Shows a brief message to the driver (e.g., "No customers found").
     * @param message The text content of the message.
     */
    void showMessage(String message);

    /**
     * Closes the requests screen and returns to the previous menu.
     */
    void finishActivity();

    /**
     * Opens the map screen to guide the driver to the customer's pickup point.
     * @param requestID The unique ID of the ride the driver just accepted.
     */
    void navigateToNavigationMap(String requestID);
}