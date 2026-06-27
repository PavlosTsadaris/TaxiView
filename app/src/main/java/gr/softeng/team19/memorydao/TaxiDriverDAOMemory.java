package gr.softeng.team19.memorydao;

import java.util.ArrayList;
import java.util.List;
import gr.softeng.team19.dao.TaxiDriverDAO;
import gr.softeng.team19.domain.TaxiDriver;

/**
 * Memory-based implementation of the TaxiDriverDAO.
 */
public class TaxiDriverDAOMemory implements TaxiDriverDAO {

    /** Static list to store drivers in memory. */
    protected static List<TaxiDriver> drivers = new ArrayList<>();

    /**
     * Saves a driver to memory or updates if it already exists.
     * @param driver The taxi driver object to save or update.
     */
    @Override
    public void save(TaxiDriver driver) {
        if (!drivers.contains(driver)) {
            drivers.add(driver);
        } else {
            drivers.remove(driver);
            drivers.add(driver);
        }
    }

    /**
     * Deletes a taxi driver from the memory list.
     * @param driver The taxi driver object to remove.
     */
    @Override
    public void delete(TaxiDriver driver) {
        drivers.remove(driver);
    }

    /**
     * Retrieves all taxi drivers stored in memory.
     * @return A list of all stored taxi drivers.
     */
    @Override
    public List<TaxiDriver> findAll() {
        return new ArrayList<>(drivers);
    }

    /**
     * Finds a taxi driver by their unique username.
     * @param id The unique username identifier.
     * @return The found TaxiDriver object, or null if no match is found.
     */
    @Override
    public TaxiDriver find(String id) {
        for (TaxiDriver d : drivers) {
            if (d.getUserName().equals(id)) {
                return d;
            }
        }
        return null;
    }
}