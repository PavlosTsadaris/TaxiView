package gr.softeng.team19.memorydao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import gr.softeng.team19.dao.CustomerDAO;
import gr.softeng.team19.domain.Customer;

/**
 * Temporary storage for Customers in the phone's memory.
 * This class works like a simple database that stays active while the app runs.
 */
public class CustomerDAOMemory implements CustomerDAO {

    // A shared list that holds all customer data
    protected static List<Customer> customers = new ArrayList<>();

    /**
     * Adds a new customer or updates an existing one in the list.
     */
    @Override
    public void save(Customer customer) {
        if (!customers.contains(customer)) {
            customers.add(customer);
        } else {
            // If they already exist, replace the old data with the new ones
            customers.remove(customer);
            customers.add(customer);
        }
    }

    /**
     * Removes a customer from the list.
     */
    @Override
    public void delete(Customer customer) {
        customers.remove(customer);
    }

    /**
     * Returns a list of all registered customers.
     */
    @Override
    public List<Customer> findAll() {
        return new ArrayList<>(customers);
    }

    /**
     * Searches for a customer by their unique username.
     * @return The Customer if found, otherwise returns null.
     */
    @Override
    public Customer find(String id) {
        for (Customer c : customers) {
            if (c.getUserName().equals(id)) {
                return c;
            }
        }
        return null;
    }
}