package gr.softeng.team19.view.history;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team19.dao.TaxiBookingDAO;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.memorydao.TaxiBookingDAOMemory;

/**
 * Presenter class that controls the logic for the Ride History screen.
 * It fetches all past trips, calculates the total money, and updates the UI.
 */
public class HistoryPresenter {

    private HistoryView view;
    private TaxiBookingDAO bookingDAO;
    private String currentUsername;

    /**
     * Constructor that sets up the presenter and starts loading the user's history.
     * @param view The UI interface used to update the screen.
     * @param username The username of the logged-in user (could be a Driver or a Customer).
     */
    public HistoryPresenter(HistoryView view, String username) {
        this.view = view;
        this.currentUsername = username;
        this.bookingDAO = new TaxiBookingDAOMemory();

        // Start loading the history as soon as the screen opens
        loadHistory();
    }

    /**
     * Finds all trips related to the user and calculates the total money earned or spent.
     * It tells the view to show the list of trips or an empty screen if nothing is found.
     */
    private void loadHistory() {
        List<TaxiBooking> allBookings = bookingDAO.findAll();
        List<TaxiBooking> userHistory = new ArrayList<>();
        double totalAmount = 0.0;

        // If no user is logged in, show the empty screen
        if (currentUsername == null) {
            view.showEmptyState();
            return;
        }

        // Loop through all bookings in the system
        for (TaxiBooking booking : allBookings) {

            // Check if the user was the customer for this trip
            boolean isMyCustomerRide = booking.getCustomer() != null &&
                    booking.getCustomer().getUserName().equals(currentUsername);

            // Check if the user was the driver for this trip
            boolean isMyDriverRide = booking.getTaxiDriver() != null &&
                    booking.getTaxiDriver().getUserName().equals(currentUsername);

            // If the user participated in the ride, add it to their history
            if (isMyCustomerRide || isMyDriverRide) {
                userHistory.add(booking);

                // If a payment was made, add the amount to the total sum
                if (booking.getPayment() != null) {
                    totalAmount += booking.getPayment().getAmount();
                }
            }
        }

        // Decide what the user sees based on the results
        if (userHistory.isEmpty()) {
            view.showEmptyState();
            view.showTotalAmount(0.0);
        } else {
            view.showHistoryList(userHistory);
            view.showTotalAmount(totalAmount);
        }
    }

    /**
     * Tells the view to close the history screen and return to the previous page.
     */
    public void goBack() {
        view.navigateBack();
    }
}