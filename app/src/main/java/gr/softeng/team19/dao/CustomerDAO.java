package gr.softeng.team19.dao;

import gr.softeng.team19.domain.Customer;
import java.util.List;

public interface CustomerDAO {
    void save(Customer customer);
    void delete(Customer customer);
    List<Customer> findAll();
    Customer find(String id);
}