package gr.softeng.team19.memorydao;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team19.domain.TaxiBooking;

/**
 * Memory-based implementation of the TaxiBookingDAO.
 */
public class TaxiBookingDAOMemory implements gr.softeng.team19.dao.TaxiBookingDAO {

    /** Static list to hold booking data in memory. */
    protected static List<TaxiBooking> bookings = new ArrayList<>();

    /**
     * Saves a booking to memory or updates it if it already exists.
     * @param booking The booking object to save or update.
     */
    @Override
    public void save(TaxiBooking booking) {
        if (!bookings.contains(booking)) {
            bookings.add(booking);
        } else {
            bookings.remove(booking);
            bookings.add(booking);
        }
    }

    /**
     * Deletes a specific booking from the memory list.
     * @param booking The booking object to remove.
     */
    @Override
    public void delete(TaxiBooking booking) {
        bookings.remove(booking);
    }

    /**
     * Retrieves all taxi bookings stored in memory.
     * @return A list containing all booking objects.
     */
    @Override
    public List<TaxiBooking> findAll() {
        return new ArrayList<>(bookings);
    }

    /**
     * Finds a booking by its unique identifier.
     * @param id The unique ID of the booking.
     * @return The found TaxiBooking object, or null if it does not exist.
     */
    @Override
    public TaxiBooking find(String id) {
        for (TaxiBooking b : bookings) {
            if (b.getBookingID().equals(id)) {
                return b;
            }
        }
        return null;
    }

}