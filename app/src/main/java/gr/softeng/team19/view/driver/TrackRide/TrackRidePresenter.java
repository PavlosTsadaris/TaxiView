package gr.softeng.team19.view.driver.TrackRide;

import android.os.Handler;
import android.os.Looper;
import org.osmdroid.util.GeoPoint;
import gr.softeng.team19.domain.GPSLocation;
import gr.softeng.team19.domain.Route;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.memorydao.MockRideData;
import gr.softeng.team19.memorydao.TaxiBookingDAOMemory;

/**
 * Presenter that manages the real-time tracking of a taxi trip.
 * It simulates the car's movement on the map, calculates the Estimated Time
 * of Arrival (ETA), and handles the logic for finishing the ride.
 */
public class TrackRidePresenter {

    /**
     * Interface used to handle timers and repeated tasks (like moving the car).
     * This allows us to test the movement logic without waiting for real-time to pass.
     */
    public interface Scheduler {
        void execute(Runnable task, long delayMillis);
        void cancel(Runnable task);
    }

    private TrackRideView view;
    private TaxiBooking booking;
    private Route route;
    private Scheduler scheduler;
    private Runnable simulationRunnable;

    private boolean isHeadingToDestination = false;
    private double mins;
    private boolean change = true;
    private boolean skip = false;

    /**
     * Standard constructor that uses the Android system to move the taxi icon.
     */
    public TrackRidePresenter(TrackRideView view, String bookingID) {
        this(view, bookingID, new Scheduler() {
            private final Handler handler = new Handler(Looper.getMainLooper());
            @Override
            public void execute(Runnable task, long delay) { handler.postDelayed(task, delay); }
            @Override
            public void cancel(Runnable task) { handler.removeCallbacks(task); }
        });
    }

    /**
     * Constructor used for testing or custom scheduling.
     * Sets up the map with the driver, customer, and destination markers.
     */
    public TrackRidePresenter(TrackRideView view, String bookingID, Scheduler testScheduler) {
        this.view = view;
        this.scheduler = testScheduler;
        this.booking = new TaxiBookingDAOMemory().find(bookingID != null ? bookingID.trim() : null);

        if (booking != null) {
            // Place all initial markers on the map
            view.setupMap(
                    booking.getTaxiDriver().getUserLocation().getPoint(),
                    new GeoPoint(booking.getPickupPoint().getLatitude(), booking.getPickupPoint().getLongitude()),
                    booking.getDestination().getPoint()
            );

            MockRideData.DemoLocation location = findLocation(booking.getPickupPoint().getLatitude());
            view.setTxtRideInfo(true, location != null ? location.name : "Unknown");
            route = booking.startRoute(); // Start calculating the route data
            view.showSkip();
        } else {
            view.showMessage("Error: Booking not found");
        }
    }

    /**
     * Logic for when the driver arrives at the customer's pickup spot.
     * It stops the first movement and starts the movement toward the destination.
     */
    public void onArrivedAtPickup() {
        scheduler.cancel(simulationRunnable);
        view.setButtonArrival(false);
        isHeadingToDestination = true;

        MockRideData.DemoLocation location = findLocation(booking.getDestination().getPoint().getLatitude());
        view.setTxtRideInfo(false, location != null ? location.name : "Unknown");
        view.showSkip();

        // Start moving toward the final drop-off
        startMovementSimulation(
                booking.getDestination().getPoint().getLatitude(),
                booking.getDestination().getPoint().getLongitude(),
                "Arrived at Destination"
        );
    }

    /**
     * Finalizes the ride, calculates the cost based on time/distance,
     * and sends the driver to the payment screen.
     */
    public void onEndRide() {
        scheduler.cancel(simulationRunnable);
        booking.endRoute(route, booking.getDestination().getPoint().getLatitude(), booking.getDestination().getPoint().getLongitude());

        // Calculate the total fare
        double amount = route.calculateCost(mins, route.getTotalDistance());
        route.setTotalTime(mins);

        view.showMessage("Ride Completed.");
        view.navigateToPayment(amount, booking.getBookingID());
    }

    /**
     * Simulates the taxi driving toward a target point.
     * It updates the map every second to show the car "driving."
     */
    private void startMovementSimulation(double targetLat, double targetLon, String completionMessage) {
        simulationRunnable = new Runnable() {
            @Override
            public void run() {
                double driverLat = booking.getTaxiDriver().getUserLocation().getLatitude();
                double driverLon = booking.getTaxiDriver().getUserLocation().getLongitude();

                if (skip) {
                    // Instantly teleport to destination (for demo purposes)
                    driverLat = targetLat;
                    driverLon = targetLon;
                    skip = false;
                    view.hideSkip();
                } else {
                    // Move the car a small "step" closer to the target
                    driverLat = moveStep(driverLat, targetLat, 0.005);
                    driverLon = moveStep(driverLon, targetLon, 0.005);
                }

                // Update the driver's location in the system and on the map
                booking.getTaxiDriver().setUserLocation(driverLat, driverLon);
                view.updateDriverLocationOnMap(driverLat, driverLon);

                double distanceKm = booking.getTaxiDriver().getUserLocation().distanceTo(new GPSLocation(targetLat, targetLon));
                updateETA(distanceKm);

                if (distanceKm > 0) {
                    // Keep moving every 1 second
                    scheduler.execute(this, 1000);
                } else {
                    handleArrival(completionMessage);
                }
            }
        };
        scheduler.execute(simulationRunnable, 0);
    }

    /**
     * Helper to move a coordinate one small increment toward a goal.
     */
    private double moveStep(double current, double target, double step) {
        if (Math.abs(current - target) < step) return target;
        return current < target ? current + step : current - step;
    }

    /**
     * Updates the time remaining based on how many kilometers are left.
     */
    private void updateETA(double distance) {
        int minutesAway = (int) Math.max(1, Math.round(distance * 2.1));
        if (isHeadingToDestination && change) {
            mins = minutesAway;
            change = false;
        }
        view.setTextETA(true, distance < 0.3 ? "Arriving now..." : String.valueOf(minutesAway));
    }

    /**
     * Handles the UI changes once the car reaches its target.
     */
    private void handleArrival(String msg) {
        if (!isHeadingToDestination) {
            view.setButtonArrival(true); // Show "Arrived" for pickup
        } else {
            view.setButtonArrival(false);
            view.setTextETA(false, "");
            view.setButtonEndRide(true); // Show "End Ride" for drop-off
        }
        view.showMessage(msg);
    }

    private MockRideData.DemoLocation findLocation(double lat) {
        for (MockRideData.DemoLocation loc : MockRideData.LOCATIONS) {
            if (loc.point.getLatitude() == lat) return loc;
        }
        return null;
    }

    /**
     * Starts the first leg of the journey: going to the customer.
     */
    public void goToCustomer() {
        view.showSkip();
        startMovementSimulation(booking.getPickupPoint().getLatitude(), booking.getPickupPoint().getLongitude(), "Arrived at Customer");
    }

    /**
     * Sets the skip flag to true, causing the car to teleport to the destination.
     * Used for testing and demonstration.
     */
    public void onSkipRide() {
        this.skip = true;
    }

    /**
     * Stops the simulation and cleans up the scheduler when the activity is destroyed.
     */
    public void onDestroy() {
        scheduler.cancel(simulationRunnable);
    }
}