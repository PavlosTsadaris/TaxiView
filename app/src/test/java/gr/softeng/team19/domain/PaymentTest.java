package gr.softeng.team19.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;



public class PaymentTest {
    private Payment payment;
    private TaxiBooking mockBooking;

    @Before
    public void setUp() throws Exception {

        GPSLocation pickup = new GPSLocation(22.2222, 22.2222);
        Address destination = new Address("Panepistimiou", "Athens", 12, 10564);

        Customer customer = new Customer("user", "pass", "email@test.com", "Name", "Surname", "6900000000",
                java.time.LocalDate.of(2000, 1, 1), "123456", 22.2, 22.2, "Street", "City", 1, 10000);

        TaxiDriver driver = new TaxiDriver("driver", "pass", "dr@test.com", "DName", "DSurname", "6911111111",
                java.time.LocalDate.of(1990, 1, 1), "987654", 22.2, 22.2, "DStreet", "DCity", 2, 20000, "AAA1111", "Car", "Model");

        TaxiRideRequest mockRequest = new TaxiRideRequest(pickup, destination, customer);

         mockBooking = new TaxiBooking(pickup, destination, customer, driver, mockRequest);

        payment = new Payment(100.0, Payment.PaymentMethod.PayByCard, mockBooking);

    }

    @Test
    public void constructor_initializesCorrectly() {
        assertNotNull(payment.getPaymentID());
        assertEquals(100.0, payment.getAmount(), 0.001);
        assertNotNull(payment.getDateTime());
        assertEquals(Payment.PaymentMethod.PayByCard, payment.getPaymentType());
        assertEquals("Pending", payment.getStatus());
    }

    @Test
    public void testToString() {
        String output = payment.toString();

        assertTrue(output.contains("Payment ID"));
        assertTrue(output.contains("Amount"));
        assertTrue(output.contains("DateTime"));
        assertTrue(output.contains("Payment Type"));
        assertTrue(output.contains("Status"));
    }

    @Test
    public void paymentID_startsWithP() {
        String id = payment.getPaymentID();
        assertTrue(id.startsWith("P"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_throwsExceptionOnNegativeAmount() {
        new Payment(-25.0, Payment.PaymentMethod.PayByCash, mockBooking);
    }

    @Test
    public void getPaymentType() {
        Payment.PaymentMethod type = payment.getPaymentType();

        assertTrue(type == Payment.PaymentMethod.PayByCard
                    || type == Payment.PaymentMethod.PayByCash);
    }

    @Test
    public void setAmount_updatesCorrectly() {
        payment.setAmount(80.5);
        assertEquals(80.5, payment.getAmount(), 0.001);
    }

}