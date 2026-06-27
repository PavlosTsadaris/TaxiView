package gr.softeng.team19.dao;

import gr.softeng.team19.domain.TaxiDriver;
import java.util.List;

public interface TaxiDriverDAO {
    void save(TaxiDriver driver);
    void delete(TaxiDriver driver);
    List<TaxiDriver> findAll();
    TaxiDriver find(String id);
}