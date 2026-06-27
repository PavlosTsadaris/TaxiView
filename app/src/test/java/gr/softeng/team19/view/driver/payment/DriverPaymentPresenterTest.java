package gr.softeng.team19.view.driver.payment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.memorydao.MemoryInitializer;
import gr.softeng.team19.memorydao.PaymentDAOMemory;
import gr.softeng.team19.memorydao.TaxiBookingDAOMemory;

/**
 * Unit tests for DriverPaymentPresenter to verify fare validation and payment completion.
 */
public class DriverPaymentPresenterTest {

    private DriverPaymentViewStub viewStub;
    private DriverPaymentPresenter presenter;
    private String validBookingID;
    private final Double ESTIMATED_AMOUNT = 15.0;

    /**
     * Sets up mock data and initializes the presenter with a synchronous scheduler for testing.
     */
    @Before
    public void setUp() {
        MemoryInitializer.prepareData();
        viewStub = new DriverPaymentViewStub();

        List<TaxiBooking> bookings = new TaxiBookingDAOMemory().findAll();
        validBookingID = bookings.get(0).getBookingID();

        // Immediate execution scheduler to avoid timing issues in tests
        DriverPaymentPresenter.Scheduler syncScheduler = (task, delay) -> task.run();

        presenter = new DriverPaymentPresenter(viewStub, validBookingID, ESTIMATED_AMOUNT, syncScheduler);
    }

    /**
     * Verifies that a valid payment amount transitions the UI through processing to success.
     */
    @Test
    public void testConfirmPaymentSuccess() {
        presenter.onConfirmPayment("15.50");

        Assert.assertTrue(viewStub.waitingStateShown);
        Assert.assertTrue(viewStub.successStateShown);
        Assert.assertNotNull(viewStub.navigatedHomeUser);

        // Confirm the payment record was saved in the database
        Assert.assertFalse(new PaymentDAOMemory().findAll().isEmpty());
    }

    /**
     * Verifies that the system prevents submission if the amount field is empty.
     */
    @Test
    public void testConfirmPaymentEmpty() {
        presenter.onConfirmPayment("");
        Assert.assertEquals("Amount cannot be empty", viewStub.inputErrorMessage);
    }

    /**
     * Verifies error handling for non-numeric input in the payment field.
     */
    @Test
    public void testConfirmPaymentInvalidFormat() {
        presenter.onConfirmPayment("abc");
        Assert.assertEquals("Invalid format. Please enter a number (e.g. 12.50)", viewStub.inputErrorMessage);
    }

    /**
     * Verifies that zero or negative values are rejected.
     */
    @Test
    public void testConfirmPaymentZeroOrNegative() {
        presenter.onConfirmPayment("0");
        Assert.assertEquals("Amount must be greater than 0", viewStub.inputErrorMessage);

        presenter.onConfirmPayment("-5");
        Assert.assertEquals("Amount must be greater than 0", viewStub.inputErrorMessage);
    }

    /**
     * Verifies business logic that prevents overcharging the customer beyond a 33% threshold of the estimate.
     */
    @Test
    public void testConfirmPaymentTooHigh() {
        // Limit is Estimate + 1/3 of Estimate (15 + 5 = 20.0)
        presenter.onConfirmPayment("25.0");
        Assert.assertEquals("Amount is too high! Check again.", viewStub.inputErrorMessage);
    }

    /**
     * Verifies that the presenter handles initialization failures when the booking ID is not found.
     */
    @Test
    public void testInitWithInvalidBooking() {
        DriverPaymentViewStub newStub = new DriverPaymentViewStub();
        new DriverPaymentPresenter(newStub, "invalid_id", 10.0);
        Assert.assertNull(newStub.customerName);
    }
}