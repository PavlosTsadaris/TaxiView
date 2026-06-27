package gr.softeng.team19.memorydao;

import java.util.ArrayList;
import java.util.List;
import gr.softeng.team19.dao.TaxiRideRequestDAO;
import gr.softeng.team19.domain.TaxiRideRequest;

/**
 * Memory-based implementation of the TaxiRideRequestDAO.
 */
public class TaxiRideRequestDAOMemory implements TaxiRideRequestDAO {

    /** Static list to store ride requests in memory. */
    protected static List<TaxiRideRequest> requests = new ArrayList<>();

    /**
     * Saves a ride request to memory or updates it if it exists.
     * @param request The ride request object to save or update.
     */
    @Override
    public void save(TaxiRideRequest request) {
        if (!requests.contains(request)) {
            requests.add(request);
        } else {
            requests.remove(request);
            requests.add(request);
        }
    }

    /**
     * Deletes a ride request from the memory list.
     * @param request The ride request object to remove.
     */
    @Override
    public void delete(TaxiRideRequest request) {
        requests.remove(request);
    }

    /**
     * Retrieves all ride requests stored in memory.
     * @return A new list containing all stored ride requests.
     */
    @Override
    public List<TaxiRideRequest> findAll() {
        return new ArrayList<>(requests);
    }

    /**
     * Finds a specific ride request by its unique ID.
     * @param requestId The unique identifier of the request.
     * @return The found TaxiRideRequest object, or null if no match exists.
     */
    @Override
    public TaxiRideRequest find(String requestId) {
        for (TaxiRideRequest r : requests) {
            if (r.getRequestID().equals(requestId)) {
                return r;
            }
        }
        return null;
    }

    /**
     * Finds a ride request associated with a specific customer username.
     * @param username The customer's unique username.
     * @return The matching TaxiRideRequest object, or null if not found.
     */
    @Override
    public TaxiRideRequest findByUsername(String username) {
        for (TaxiRideRequest r : requests) {
            if (r.getCustomer().getUserName().equals(username)) {
                return r;
            }
        }
        return null;
    }

    /**
     * Retrieves all ride requests that are currently in a pending state.
     * @return A list of all pending ride requests.
     */
    @Override
    public List<TaxiRideRequest> findAllPending() {
        List<TaxiRideRequest> pending = new ArrayList<>();
        for (TaxiRideRequest r : requests) {
            if ("PENDING".equals(r.getStatus())) {
                pending.add(r);
            }
        }
        return pending;
    }
}