package gr.softeng.team19.dao;

import java.util.List;

import gr.softeng.team19.domain.DriverRating;

public interface DriverRatingDAO {
    void save(DriverRating rating);
    void delete(DriverRating rating);
    List<DriverRating> findAll();
    DriverRating find(String id);
}
