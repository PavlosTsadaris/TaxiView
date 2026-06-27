package gr.softeng.team19.dao;

import gr.softeng.team19.domain.TaxiRideRequest;
import java.util.List;

public interface TaxiRideRequestDAO {
    void save(TaxiRideRequest request);
    void delete(TaxiRideRequest request);
    List<TaxiRideRequest> findAll();
    TaxiRideRequest find(String requestId);

    TaxiRideRequest findByUsername(String username);

    List<TaxiRideRequest> findAllPending();
}