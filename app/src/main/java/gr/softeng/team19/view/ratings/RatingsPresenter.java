package gr.softeng.team19.view.ratings;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team19.dao.DriverRatingDAO;
import gr.softeng.team19.domain.DriverRating;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.memorydao.DriverRatingDAOMemory;
import gr.softeng.team19.memorydao.TaxiBookingDAOMemory;

/**
 * Presenter class that handles the logic for showing ratings.
 * It filters the bookings to find only the ones that have been rated by or for the user.
 */
public class RatingsPresenter {
    private RatingsView view;
    private List<DriverRating> ratings;
    private String currentUsername;
    private DriverRatingDAO ratingsDAO;

    /**
     * Constructor that sets up the presenter and loads the rating data.
     * @param view The UI interface to talk to.
     * @param username The username of the person (driver or customer) whose ratings we want to see.
     */
    public RatingsPresenter(RatingsView view, String username) {
        this.view = view;
        this.currentUsername = username;
        this.ratingsDAO = new DriverRatingDAOMemory();
        loadRatings();
    }

    /**
     * Gets all bookings from the database and keeps only the ones belonging to the user
     * that have a rating. It then tells the view what to display.
     */
    private void loadRatings() {
        List<TaxiBooking> allBookings = new TaxiBookingDAOMemory().findAll();
        List<TaxiBooking> ratedBookings = new ArrayList<>();

        // If no user is specified, show the empty screen
        if (currentUsername == null) {
            view.showEmptyState();
            return;
        }

        // Loop through all bookings to find matches for the current user
        for (TaxiBooking booking : allBookings) {
            // Check if the user was either the customer or the driver for this ride
            boolean isMyRide = (booking.getCustomer() != null && booking.getCustomer().getUserName().equals(currentUsername)) ||
                    (booking.getTaxiDriver() != null && booking.getTaxiDriver().getUserName().equals(currentUsername));

            // Only add if it's the user's ride and it has been evaluated
            if (isMyRide && booking.isEvaluated()) {
                ratedBookings.add(booking);
            }
        }

        // Tell the view to show the list or the empty state message
        if (ratedBookings.isEmpty()) {
            view.showEmptyState();
        } else {
            view.showHistoryList(ratedBookings);
        }
    }

    /**
     * Tells the view to go back to the previous screen.
     */
    public void goBack() {
        view.navigateBack();
    }
}