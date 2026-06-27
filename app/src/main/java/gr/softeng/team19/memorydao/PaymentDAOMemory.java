package gr.softeng.team19.memorydao;

import java.util.ArrayList;
import java.util.List;

import gr.softeng.team19.dao.PaymentDAO;
import gr.softeng.team19.domain.Payment;

/**
 * Memory-based implementation of the PaymentDAO.
 */
public class PaymentDAOMemory implements PaymentDAO {

    /** Static list to hold payment data in memory. */
    protected static List<Payment> payments = new ArrayList<>();

    /**
     * Saves a payment to memory or updates it if it already exists.
     * @param payment The payment object to save or update.
     */
    @Override
    public void save(Payment payment) {
        if (!payments.contains(payment)) {
            payments.add(payment);
        } else {
            payments.remove(payment);
            payments.add(payment);
        }
    }

    /**
     * Deletes a payment from the memory list.
     * @param payment The payment object to remove.
     */
    @Override
    public void delete(Payment payment) {
        payments.remove(payment);
    }

    /**
     * Retrieves all payments stored in memory.
     * @return A list containing all payment objects.
     */
    @Override
    public List<Payment> findAll() {
        return new ArrayList<>(payments);
    }

    /**
     * Finds a specific payment by its unique ID.
     * @param id The unique identifier of the payment.
     * @return The found Payment object, or null if no match exists.
     */
    @Override
    public Payment find(String id) {
        for (Payment p : payments) {
            if (p.getPaymentID().equals(id)) {
                return p;
            }
        }
        return null;
    }
}