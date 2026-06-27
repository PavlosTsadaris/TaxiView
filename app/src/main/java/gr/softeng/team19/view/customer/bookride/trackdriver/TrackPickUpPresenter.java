package gr.softeng.team19.view.customer.bookride.trackdriver;

import android.os.Handler;
import android.os.Looper;

import gr.softeng.team19.dao.RouteDAO;
import gr.softeng.team19.dao.TaxiBookingDAO;
import gr.softeng.team19.domain.GPSLocation;
import gr.softeng.team19.domain.Route;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.memorydao.RouteDAOMemory;
import gr.softeng.team19.memorydao.TaxiBookingDAOMemory;

/**
 * Presenter for tracking driver pickup and ride progress simulation.
 */
public class TrackPickUpPresenter {

    /**
     * Interface to abstract time-based operations for testability.
     */
    public interface Scheduler {
        /**
         * @param task The task to run.
         * @param delayMillis Delay in milliseconds.
         */
        void execute(Runnable task, long delayMillis);

        /**
         * @param task The task to start immediately.
         */
        void executeRecurring(Runnable task);

        /**
         * @param task The task to remove from the queue.
         */
        void removeCallbacks(Runnable task);
    }

    private TrackPickUpView view;
    private TaxiBookingDAO bookingDAO;
    private TaxiBooking currentBooking;
    private RouteDAO routeDao;
    private Route route;
    private Scheduler scheduler;
    private Runnable simulationRunnable;
    private boolean pickup = false;
    private double mins;
    private boolean change = true;
    private boolean skip = false;

    /**
     * Production constructor using real Android Handlers.
     * @param view The view implementation.
     * @param bookingID The identifier for the current booking.
     */
    public TrackPickUpPresenter(TrackPickUpView view, String bookingID) {
        this(view, bookingID, new Scheduler() {
            private Handler handler;

            private Handler getHandler() {
                if (handler == null) {
                    handler = new Handler(Looper.getMainLooper());
                }
                return handler;
            }

            @Override
            public void execute(Runnable task, long delayMillis) {
                getHandler().postDelayed(task, delayMillis);
            }

            @Override
            public void executeRecurring(Runnable task) {
                getHandler().post(task);
            }

            @Override
            public void removeCallbacks(Runnable task) {
                getHandler().removeCallbacks(task);
            }
        });
    }

    /**
     * Test constructor for dependency injection.
     * @param view The view implementation.
     * @param bookingID The identifier for the current booking.
     * @param scheduler The custom scheduler for timing control.
     */
    public TrackPickUpPresenter(TrackPickUpView view, String bookingID, Scheduler scheduler) {
        this.view = view;
        this.scheduler = scheduler;
        this.bookingDAO = new TaxiBookingDAOMemory();
        this.currentBooking = bookingDAO.find(bookingID);
    }

    /**
     * Initializes the tracking view and starts the simulation logic.
     */
    public void startTracking() {
        if (currentBooking == null) {
            view.showMessage("Error: Booking not found!");
            return;
        }

        currentBooking.startRoute();
        route = currentBooking.getRoute();
        routeDao = new RouteDAOMemory();
        routeDao.save(route);

        view.setupDriverAndCustomerMarkers(
                currentBooking.getTaxiDriver().getUserLocation().getPoint(),
                currentBooking.getCustomer().getUserLocation().getPoint(),
                currentBooking.getDestination().getPoint()
        );

        view.setDriverInfo(
                currentBooking.getTaxiDriver().getName() + " " + currentBooking.getTaxiDriver().getSurname(),
                currentBooking.getTaxiDriver().getVehicle().getManufacturer(),
                currentBooking.getTaxiDriver().getVehicle().getLisencePlate(),
                (double) Math.round(currentBooking.getTaxiDriver().getAverageRating() * 10) / 10.0
        );

        view.setStatus("Driver is on the way");

        int minutesAway = 5;
        if (currentBooking.getPickupPoint() != null && currentBooking.getTaxiDriver().getUserLocation() != null) {
            double distanceKm = currentBooking.getTaxiDriver().getUserLocation().distanceTo(currentBooking.getPickupPoint());
            minutesAway = (int) Math.max(1, Math.round(distanceKm * 2.1));
        }

        view.setETA("Arriving in " + minutesAway + " min");
        view.showSkip();
        startMovementSimulation(currentBooking.getPickupPoint().getLatitude(), currentBooking.getPickupPoint().getLongitude(), "Driver has arrived!");
    }

    /**
     * Manages the iterative movement of the driver on the map.
     * @param destinationLat target latitude.
     * @param destinationLon target longitude.
     * @param finalText status message upon arrival.
     */
    private void startMovementSimulation(double destinationLat, double destinationLon, String finalText) {
        simulationRunnable = new Runnable() {
            double driverLat = currentBooking.getTaxiDriver().getUserLocation().getLatitude();
            double driverLon = currentBooking.getTaxiDriver().getUserLocation().getLongitude();

            @Override
            public void run() {
                if (skip) {
                    driverLat = destinationLat;
                    driverLon = destinationLon;
                    view.hideSkip();
                    skip = false;
                }

                double stepLat = 0.0005;
                double stepLon = 0.0003;

                if (Math.abs(driverLat - destinationLat) < stepLat) {
                    driverLat = destinationLat;
                } else if (driverLat < destinationLat) {
                    driverLat += stepLat;
                } else {
                    driverLat -= stepLat;
                }

                if (Math.abs(driverLon - destinationLon) < stepLon) {
                    driverLon = destinationLon;
                } else if (driverLon < destinationLon) {
                    driverLon += stepLon;
                } else {
                    driverLon -= stepLon;
                }

                view.updateDriverLocationOnMap(driverLat, driverLon);
                currentBooking.getTaxiDriver().setUserLocation(driverLat, driverLon);

                GPSLocation destination = new GPSLocation(destinationLat, destinationLon);
                double distanceKm = currentBooking.getTaxiDriver().getUserLocation().distanceTo(destination);
                int remainingTime = (int) Math.max(1, Math.round(distanceKm * 2.1));

                if (pickup && change) {
                    mins = remainingTime;
                    change = false;
                }

                view.setETA("Arriving in " + remainingTime + " min");

                if (distanceKm < 0.36) {
                    view.setETA("Arriving now...");
                }
                if (distanceKm == 0) {
                    view.setETA("Arrived!");
                    view.setStatus(finalText);
                }

                if (distanceKm > 0.001) {
                    scheduler.execute(this, 1500);
                } else {
                    if (!pickup) {
                        pickup = true;
                        driverArrived();
                    } else {
                        rideEnded();
                    }
                }
            }
        };

        scheduler.executeRecurring(simulationRunnable);
    }

    /**
     * Calculates costs and navigates to payment after ride completion.
     */
    private void rideEnded() {
        currentBooking.endRoute(route, currentBooking.getDestination().getPoint().getLatitude(), currentBooking.getDestination().getPoint().getLongitude());
        double amount = route.calculateCost(mins, route.getTotalDistance());
        route.setTotalTime(mins);

        view.setStatus("You have arrived at your destination!");

        scheduler.execute(() -> {
            view.navigateToPayment(amount, currentBooking);
        }, 1800);
    }

    /**
     * Handles state transition once the driver reaches the customer.
     */
    private void driverArrived() {
        scheduler.execute(() -> {
            view.setETA("Wait for driver to start the ride...");
        }, 1000);

        scheduler.execute(() -> {
            view.setCallButtonOff();
            view.setCancelButtonOff();
            view.setStatus("The ride started...");
            view.showSkip();
            startMovementSimulation(currentBooking.getDestination().getPoint().getLatitude(), currentBooking.getDestination().getPoint().getLongitude(), "Arrived at destination!");
        }, 2500);
    }

    /**
     * Initiates a mock call to the driver.
     */
    public void onCallDriver() {
        if (currentBooking != null) {
            view.showMessage("Calling " + currentBooking.getTaxiDriver().getName() + "...");
        } else {
            view.showMessage("Calling Driver...");
        }
    }

    /**
     * Stops simulation and returns user to the home screen.
     */
    public void onCancelRide() {
        scheduler.removeCallbacks(simulationRunnable);
        view.showMessage("Ride Cancelled");
        view.navigateToHome(currentBooking.getCustomer().getUserName());
    }

    /**
     * Triggers the review prompt UI.
     */
    public void onNotificationIcon(){
        view.showReviewPrompt(currentBooking);
    }

    /**
     * Cleans up resources when the activity is destroyed.
     */
    public void onDestroy() {
        scheduler.removeCallbacks(simulationRunnable);
    }

    /**
     * Navigates to the evaluation screen.
     * @param booking The booking to review.
     */
    public void onReviewNow(TaxiBooking booking) {
        view.deleteReviewPrompt();
        view.navigateToDriverEvaluation(booking);
    }

    /**
     * Defers the evaluation to a later time.
     * @param booking The booking to update.
     */
    public void onReviewLater(TaxiBooking booking) {
        booking.setEvaluated(false);
    }

    /**
     * Instantly finishes current movement segment.
     */
    public void onSkip() {
        skip = true;
    }
}