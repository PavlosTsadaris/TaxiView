package gr.softeng.team19.memorydao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import gr.softeng.team19.dao.UserDAO;
import gr.softeng.team19.domain.Customer;
import gr.softeng.team19.domain.ApplicationUser;

/**
 * Memory-based implementation of the UserDAO.
 */
public class UserDAOMemory implements UserDAO {

    /** Static list representing the in-memory database of users. */
    protected static List<ApplicationUser> entities = new ArrayList<>();

    /**
     * Adds a new user or updates an existing one in memory.
     * @param user The ApplicationUser object to be added or updated.
     */
    @Override
    public void add(ApplicationUser user) {
        if (!entities.contains(user)) {
            entities.add(user);
        } else {
            entities.remove(user);
            entities.add(user);
        }
    }

    /**
     * Removes a user from the memory storage.
     * @param user The ApplicationUser object to be deleted.
     */
    @Override
    public void delete(ApplicationUser user) {
        entities.remove(user);
    }

    /**
     * Searches for a user by their unique username.
     * @param username The unique username string.
     * @return The matching ApplicationUser object, or null if not found.
     */
    @Override
    public ApplicationUser find(String username) {
        for (ApplicationUser user : entities) {
            if (user.getUserName().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Retrieves a copy of all users stored in memory.
     * @return A list containing all ApplicationUser objects.
     */
    @Override
    public List<ApplicationUser> findAll() {
        return new ArrayList<>(entities);
    }
}