package gr.softeng.team19.view.driver.rideexecution;

import gr.softeng.team19.dao.TaxiRideRequestDAO;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.domain.TaxiRideRequest;
import gr.softeng.team19.memorydao.MockRideData;
import gr.softeng.team19.memorydao.TaxiBookingDAOMemory;
import gr.softeng.team19.memorydao.TaxiRideRequestDAOMemory;

/**
 * Presenter that manages the logic for guiding a driver to a customer.
 * It handles looking up the customer's location and officially starting
 * the ride once the driver reaches the pickup point.
 */
public class RideNavigationPresenter {
    private RideNavigationView view;
    private TaxiRideRequest request;
    private TaxiRideRequestDAO requestDAO;

    /**
     * Constructor that connects the presenter to the navigation screen.
     * @param view The UI interface for the navigation screen.
     */
    public RideNavigationPresenter(RideNavigationView view) {
        this.view = view;
    }

    /**
     * Starts the navigation process by finding the request details in the database.
     * It looks for the customer's coordinates and tells the view to show them on the map.
     * @param requestId The ID of the specific ride request.
     */
    public void startNavigation(String requestId) {
        requestDAO = new TaxiRideRequestDAOMemory();
        request = requestDAO.find(requestId);

        if (request == null) return;

        // Find the name of the location (e.g., "Syntagma Square") based on coordinates
        MockRideData.DemoLocation location = null;
        for (MockRideData.DemoLocation loc : MockRideData.LOCATIONS) {
            if (loc.point.getLatitude() == request.getPickupPoint().getLatitude()) {
                location = loc;
                break;
            }
        }

        String locName = (location != null) ? location.name : "Unknown Location";

        // Command the view to center the map on the customer
        view.setupMap(request.getPickupPoint().getLatitude(),
                request.getPickupPoint().getLongitude(),
                locName);
    }

    /**
     * Logic for when the driver confirms they have picked up the customer.
     * This action turns the "Request" into an official "Booking" and saves it.
     */
    public void onSelectCustomer() {
        if (request == null) return;

        // Official state change: The request is now an active booking
        TaxiBooking booking = request.acceptRequest(request.getChosenDriver());
        new TaxiBookingDAOMemory().save(booking);

        view.showToast("You chose customer: " + request.getCustomer().getName());

        // Move to the screen that tracks the ride until the destination
        view.navigateToOngoingRide(booking.getBookingID());
    }

    /**
     * Stops the navigation and closes the screen.
     */
    public void onCancel (){
        view.cancel();
    }
}