package gr.softeng.team19.dao;

import java.util.List;
import gr.softeng.team19.domain.Route;

public interface RouteDAO {
    void save(Route route);
    void delete(Route route);
    List<Route> findAll();
    Route find(String id);
}