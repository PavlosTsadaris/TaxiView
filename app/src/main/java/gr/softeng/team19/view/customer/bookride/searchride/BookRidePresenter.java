package gr.softeng.team19.view.customer.bookride.searchride;

import android.os.Handler;
import android.os.Looper;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import java.util.ArrayList;
import java.util.Iterator;
import gr.softeng.team19.R;
import gr.softeng.team19.dao.TaxiBookingDAO;
import gr.softeng.team19.dao.TaxiRideRequestDAO;
import gr.softeng.team19.domain.Customer;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.domain.TaxiDriver;
import gr.softeng.team19.domain.TaxiRideRequest;
import gr.softeng.team19.memorydao.CustomerDAOMemory;
import gr.softeng.team19.memorydao.MockRideData;
import gr.softeng.team19.memorydao.TaxiBookingDAOMemory;
import gr.softeng.team19.memorydao.TaxiDriverDAOMemory;
import gr.softeng.team19.memorydao.TaxiRideRequestDAOMemory;

/**
 * Presenter that controls the ride booking flow.
 * It manages searching for your location, finding nearby drivers,
 * and simulating if a driver accepts or rejects your request.
 */
public class BookRidePresenter {

    /**
     * Helper interface to handle delays (like the 2-second search time).
     * This makes it easier to test the code without waiting for real time.
     */
    public interface Scheduler {
        void execute(Runnable task, long delayMillis);
    }

    private BookRideView view;
    private Scheduler scheduler;
    private GeoPoint destinationPoint;

    private boolean isSearching = false;
    private String lastAddress;
    private TaxiRideRequest request;
    private TaxiDriver beforeDriver;
    private ArrayList<TaxiDriver> rejected = new ArrayList<>();
    private MockRideData.DemoLocation[] locations;

    /**
     * Standard constructor for the Android app.
     * @param view The UI interface for booking.
     */
    public BookRidePresenter(BookRideView view) {
        this.view = view;
        this.scheduler = (task, delay) -> new Handler(Looper.getMainLooper()).postDelayed(task, delay);
    }

    /**
     * Constructor used specifically for Unit Testing.
     */
    public BookRidePresenter(BookRideView view, Scheduler testScheduler) {
        this.view = view;
        this.scheduler = testScheduler;
    }

    /**
     * Simulates the phone looking for your GPS location.
     * After 2 seconds, it "finds" Syntagma Square as the default pickup.
     * @param destination The user's destination.
     */
    public void startLocationSearch(String destination) {
        view.setStatusText(R.string.status_locating);
        view.setAddressText("...");
        view.setButtonEnabled(false);

        scheduler.execute(() -> {
            view.setStatusText(R.string.status_confirm);
            view.setAddressText("Syntagma Square, Athens");
            lastAddress = "Syntagma Square, Athens";
            for (int i = 0; i < MockRideData.LOCATIONS.size(); i++) {
                if (MockRideData.LOCATIONS.get(i).name.equals(destination)) {
                    destinationPoint = new GeoPoint(MockRideData.LOCATIONS.get(i).point.getLatitude(), MockRideData.LOCATIONS.get(i).point.getLongitude());
                    continue;
                }
            }
            view.setButtonText(R.string.btn_confirm_location);
            view.setButtonEnabled(true);
        }, 2000);
    }

    /**
     * Starts searching for taxi drivers near the user's pickup point.
     * @param username The customer's username.
     */
    public void onActionButtonClicked(String username) {
        if (!isSearching) {
            isSearching = true;

            view.setStatusText(R.string.status_searching);
            view.setButtonText(R.string.btn_searching);
            view.setButtonEnabled(false);

            Customer customer = new CustomerDAOMemory().find(username);
            request = new TaxiRideRequest(customer.getUserLocation(), customer.getAddress(), customer);

            if(destinationPoint != null) {
                request.getDestination().setPoint(destinationPoint);
            }

            new TaxiRideRequestDAOMemory().save(request);

            // Wait 3 seconds to "find" drivers
            scheduler.execute(() -> {
                view.setStatusText(R.string.status_drivers_found);
                view.setButtonText(R.string.btn_select_driver);
                view.setButtonEnabled(true);

                ArrayList<TaxiDriver> drivers = request.findAllDrivers((ArrayList<TaxiDriver>) new TaxiDriverDAOMemory().findAll());
                view.showDriverList(drivers);
            }, 3000);
        }
    }

    /**
     * Stops the current search or returns the user to the home screen.
     */
    public void onCancelBookRide() {
        if (isSearching) {
            isSearching = false;
            view.resetUI(lastAddress);
            view.hideWaitingOverlay();
            if(beforeDriver != null){
                view.showCanceledDriver(beforeDriver.getName());
                beforeDriver = null;
            }
        } else {
            view.navigateToHomeScreen();
        }
    }

    /**
     * Sends the user back to pick a different destination.
     */
    public void onChangeDestination() {
        view.navigateToDestinationScreen();
    }

    /**
     * Prepares a list of street names for the pickup selection dialog.
     * @param destination The current destination (to avoid picking the same spot).
     */
    public void showLocationSelectionDialog(String destination) {

        locations = new MockRideData.DemoLocation[MockRideData.LOCATIONS.size() - 1];
        String[] locationNames = new String[MockRideData.LOCATIONS.size() - 1];
        int f = 0;

        view.showRemoveSelectedDestination(destination);

        for (int i = 0; i < MockRideData.LOCATIONS.size(); i++) {
            if (MockRideData.LOCATIONS.get(i).name.equals(destination)) {
                destinationPoint = new GeoPoint(MockRideData.LOCATIONS.get(i).point.getLatitude(), MockRideData.LOCATIONS.get(i).point.getLongitude());
                continue;
            }
            locationNames[f] = MockRideData.LOCATIONS.get(i).name;
            locations[f++] = MockRideData.LOCATIONS.get(i);
        }

        view.showLocationSelectionDialog(locationNames);
    }

    /**
     * Updates the map and address when the user selects a new pickup spot.
     */
    public void onLocationSelected(int which, MapView map, Marker startMarker, String username) {
        MockRideData.DemoLocation selectedLoc = locations[which];

        map.getController().animateTo(selectedLoc.point);
        startMarker.setPosition(selectedLoc.point);
        map.invalidate();

        Customer customer = new CustomerDAOMemory().find(username);
        if (customer != null) {
            customer.setUserLocation(selectedLoc.point.getLatitude(), selectedLoc.point.getLongitude());
        }

        view.setAddressText(selectedLoc.name);
        lastAddress = selectedLoc.name;
        view.setButtonText(R.string.btn_confirm_location);
        view.setButtonEnabled(true);
    }

    /**
     * Handles what happens when a user taps a driver.
     * It simulates an 80% chance of the driver accepting the ride.
     * @param selectedDriver The driver the customer wants to book.
     */
    public void onChooseDriver(TaxiDriver selectedDriver) {
        view.setRecycleListEnabled(false);
        beforeDriver = selectedDriver;

        view.showWaitingOverlay(selectedDriver.getName());

        scheduler.execute(() -> {
            boolean isAccepted = Math.random() > 0.2; // 80% Acceptance Chance

            if (isAccepted) {
                // Flow for successful booking
                TaxiBooking finalBooking = request.acceptRequest(selectedDriver);
                new TaxiBookingDAOMemory().save(finalBooking);

                view.showSuccessState();
                view.showDriverAcceptedMessage(selectedDriver.getName());

                scheduler.execute(() -> {
                    view.hideWaitingOverlay();
                    view.showReviewPrompt(finalBooking);
                }, 1000);

            } else {
                // Flow for rejected request
                request.denyRequest();
                rejected.add(selectedDriver);

                ArrayList<TaxiDriver> currentDrivers = request.findAllDrivers((ArrayList<TaxiDriver>) new TaxiDriverDAOMemory().findAll());
                // Remove rejected drivers from the list
                currentDrivers.removeIf(d -> rejected.stream().anyMatch(r -> r.getUserName().equals(d.getUserName())));

                view.showCancelState();
                view.showDriverRejectedMessage(selectedDriver.getName());

                scheduler.execute(() -> view.hideCancelState(), 1800);
                view.showDriverList(currentDrivers);
                view.setRecycleListEnabled(true);
            }
        }, 2500);
    }

    /**
     * Takes the user to the rating screen.
     */
    public void onReviewNow(TaxiBooking booking) {
        view.deleteReviewPrompt();
        view.navigateToDriverEvaluation(booking);
    }

    /**
     * Skips the rating for now and goes straight to tracking the driver.
     */
    public void onReviewLater(TaxiBooking booking) {
        booking.setEvaluated(false);
        view.navigateToRideTracking(booking);
    }
}