package gr.softeng.team19.dao;

import java.util.List;
import gr.softeng.team19.domain.Payment;

public interface PaymentDAO {
    void save(Payment payment);
    void delete(Payment payment);
    List<Payment> findAll();
    Payment find(String id);
}