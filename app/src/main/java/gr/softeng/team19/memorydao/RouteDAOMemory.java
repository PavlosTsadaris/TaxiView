package gr.softeng.team19.memorydao;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team19.dao.RouteDAO;
import gr.softeng.team19.domain.Route;

/**
 * Memory-based implementation of the RouteDAO.
 */
public class RouteDAOMemory implements RouteDAO {

    /** Static list to store routes in memory. */
    protected static List<Route> routes = new ArrayList<>();

    /**
     * Saves a route to memory or updates it if it exists.
     * @param route The route object to save.
     */
    @Override
    public void save(Route route) {
        if (!routes.contains(route)) {
            routes.add(route);
        } else {
            routes.remove(route);
            routes.add(route);
        }
    }

    /**
     * Deletes a route from the memory list.
     * @param route The route object to remove.
     */
    @Override
    public void delete(Route route) {
        routes.remove(route);
    }

    /**
     * Retrieves all routes stored in memory.
     * @return A list of all stored routes.
     */
    @Override
    public List<Route> findAll() {
        return new ArrayList<>(routes);
    }

    /**
     * Finds a route by its unique ID.
     * @param id The unique identifier of the route.
     * @return The found Route object or null.
     */
    @Override
    public Route find(String id) {
        for (Route r : routes) {
            if (r.getRouteID().equals(id)) {
                return r;
            }
        }
        return null;
    }
}