package gr.softeng.team19.dao;

import java.util.List;

import gr.softeng.team19.domain.TaxiBooking;

public interface TaxiBookingDAO {
    void save(TaxiBooking booking);
    void delete(TaxiBooking booking);
    List<TaxiBooking> findAll();
    TaxiBooking find(String id);
}
