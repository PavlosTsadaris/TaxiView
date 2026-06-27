package gr.softeng.team19.memorydao;

import java.util.ArrayList;
import java.util.List;
import gr.softeng.team19.dao.DriverRatingDAO;
import gr.softeng.team19.domain.DriverRating;

/**
 * Temporary storage for driver ratings in the phone's memory.
 * It stores all the feedback and star ratings given by customers.
 */
public class DriverRatingDAOMemory implements DriverRatingDAO {

    // A shared list that holds all rating data
    protected static List<DriverRating> ratings = new ArrayList<>();

    /**
     * Saves a new rating or updates an existing one in the list.
     */
    @Override
    public void save(DriverRating rating) {
        if (!ratings.contains(rating)) {
            ratings.add(rating);
        } else {
            // Replace the old rating entry with the updated one
            ratings.remove(rating);
            ratings.add(rating);
        }
    }

    /**
     * Removes a specific rating from the list.
     */
    @Override
    public void delete(DriverRating rating) {
        ratings.remove(rating);
    }

    /**
     * Returns a list of all ratings stored in the system.
     */
    @Override
    public List<DriverRating> findAll() {
        return new ArrayList<>(ratings);
    }

    /**
     * Searches for a rating using its unique ID.
     * @return The Rating if found, otherwise returns null.
     */
    @Override
    public DriverRating find(String id) {
        for (DriverRating r : ratings) {
            if (r.getRatingID().equals(id)) {
                return r;
            }
        }
        return null;
    }
}