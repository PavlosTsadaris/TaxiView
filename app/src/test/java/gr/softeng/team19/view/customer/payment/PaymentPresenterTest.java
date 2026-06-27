package gr.softeng.team19.view.customer.payment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import gr.softeng.team19.domain.GPSLocation;
import gr.softeng.team19.domain.Payment;
import gr.softeng.team19.domain.Route;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.memorydao.MemoryInitializer;
import gr.softeng.team19.memorydao.TaxiBookingDAOMemory;

/**
 * Unit tests for PaymentPresenter covering cash, card, and error handling scenarios.
 */
public class PaymentPresenterTest {

    private PaymentPresenter presenter;
    private PaymentViewStub viewStub;
    private TaxiBookingDAOMemory bookingDAO;
    private TaxiBooking testBooking;

    private PaymentPresenter.Scheduler immediateScheduler = (task, delay) -> task.run();

    /**
     * Initializes test data and mock objects before each test.
     */
    @Before
    public void setUp() {
        MemoryInitializer.prepareData();
        bookingDAO = new TaxiBookingDAOMemory();
        viewStub = new PaymentViewStub();

        List<TaxiBooking> allBookings = bookingDAO.findAll();
        Assert.assertFalse(allBookings.isEmpty());
        testBooking = allBookings.get(0);
        testBooking.setRoute(new Route(new GPSLocation(testBooking.getPickupPoint().getLatitude(), testBooking.getPickupPoint().getLongitude()),testBooking));
        testBooking.getRoute().setStartTime(java.time.LocalTime.now());
        testBooking.getRoute().setEndTime(java.time.LocalTime.now().plusMinutes(15));
        testBooking.getRoute().setTotalDistance(10.5);
    }

    /**
     * Verifies that the view correctly displays the booking ID and fare amount upon initialization.
     */
    @Test
    public void testInitialization_DisplaysDetails() {
        presenter = new PaymentPresenter(viewStub, String.valueOf(testBooking.getBookingID()), 15.50, immediateScheduler, () -> 0.5);

        Assert.assertEquals(String.valueOf(testBooking.getBookingID()), viewStub.displayedBookingID);
        Assert.assertEquals(15.50, viewStub.displayedAmount, 0.001);
    }

    /**
     * Verifies the complete workflow for cash payments from confirmation to home navigation.
     */
    @Test
    public void testOnCashConfirmed_Flow() {
        presenter = new PaymentPresenter(viewStub, String.valueOf(testBooking.getBookingID()), 10.0, immediateScheduler, () -> 0.5);

        presenter.onConfirmClicked(Payment.PaymentMethod.PayByCash);
        Assert.assertTrue(viewStub.cashPromptShown);

        presenter.onCashConfirmed();

        Assert.assertNotNull(viewStub.processingMessage);
        Assert.assertTrue("Success prompt should be shown", viewStub.successPromptShown);
        Assert.assertTrue("Should navigate home", viewStub.homeNavigated);
    }

    /**
     * Verifies successful card payment using a mock provider that simulates an approved bank response.
     */
    @Test
    public void testOnSavedCardConfirmed_Success() {
        PaymentPresenter.RandomProvider successRandom = () -> 0.9;
        presenter = new PaymentPresenter(viewStub, String.valueOf(testBooking.getBookingID()), 12.0, immediateScheduler, successRandom);

        presenter.onSavedCardConfirmed();

        Assert.assertNotNull(viewStub.processingMessage);
        Assert.assertTrue("Success prompt should be shown", viewStub.successPromptShown);
        Assert.assertFalse("Error prompt should NOT be shown", viewStub.errorPromptShown);
        Assert.assertTrue(viewStub.homeNavigated);
    }

    /**
     * Verifies that the system handles bank rejections correctly without navigating away.
     */
    @Test
    public void testOnSavedCardConfirmed_BankError() {
        PaymentPresenter.RandomProvider failRandom = () -> 0.1;
        presenter = new PaymentPresenter(viewStub, String.valueOf(testBooking.getBookingID()), 12.0, immediateScheduler, failRandom);

        presenter.onSavedCardConfirmed();

        Assert.assertTrue("Error prompt should be shown", viewStub.errorPromptShown);
        Assert.assertFalse("Success prompt should NOT be shown", viewStub.successPromptShown);
        Assert.assertFalse("Should NOT navigate home on error", viewStub.homeNavigated);
    }

    /**
     * Verifies that if a new card payment fails, the user's saved card number remains unchanged.
     */
    @Test
    public void testOnNewCardConfirmed_BankError_RevertsCardNumber() {
        String originalCard = testBooking.getCustomer().getCreditCardNumber();
        String newCard = "5555-6666-7777-8888";
        PaymentPresenter.RandomProvider failRandom = () -> 0.1;

        presenter = new PaymentPresenter(viewStub, String.valueOf(testBooking.getBookingID()), 20.0, immediateScheduler, failRandom);
        presenter.onNewCardConfirmed(newCard);

        Assert.assertTrue(viewStub.errorPromptShown);
        Assert.assertEquals("Card number should revert on error", originalCard, testBooking.getCustomer().getCreditCardNumber());
    }

    /**
     * Verifies that successfully paying with a new card updates the customer's saved payment information.
     */
    @Test
    public void testOnNewCardConfirmed_Success_UpdatesCardNumber() {
        String newCard = "1234-5678-9012-3456";
        PaymentPresenter.RandomProvider successRandom = () -> 0.9;

        presenter = new PaymentPresenter(viewStub, String.valueOf(testBooking.getBookingID()), 20.0, immediateScheduler, successRandom);
        presenter.onNewCardConfirmed(newCard);

        Assert.assertTrue("Success prompt should be shown", viewStub.successPromptShown);
        Assert.assertEquals("Card number should be updated on success", newCard, testBooking.getCustomer().getCreditCardNumber());
        Assert.assertTrue(viewStub.homeNavigated);
    }

    /**
     * Verifies that selecting the card payment method triggers the correct UI prompts.
     */
    @Test
    public void testOnConfirmClicked_Card_ShowsCardPrompt() {
        presenter = new PaymentPresenter(viewStub, String.valueOf(testBooking.getBookingID()), 10.0, immediateScheduler, () -> 0.5);
        presenter.onConfirmClicked(Payment.PaymentMethod.PayByCard);

        Assert.assertTrue("Card prompt should be shown", viewStub.cardPromptShown);
        Assert.assertFalse("Cash prompt should NOT be shown", viewStub.cashPromptShown);
    }

    /**
     * Verifies that the presenter handles null booking scenarios safely without crashing.
     */
    @Test
    public void testSafetyChecks_WithInvalidBooking_DoesNothing() {
        PaymentPresenter unsafePresenter = new PaymentPresenter(viewStub, "invalid_id", 0.0, immediateScheduler, () -> 0.5);

        unsafePresenter.onConfirmClicked(Payment.PaymentMethod.PayByCash);
        unsafePresenter.onCashConfirmed();
        unsafePresenter.onSavedCardConfirmed();
        unsafePresenter.onNewCardConfirmed("1234");

        Assert.assertFalse("Cash prompt should NOT be shown", viewStub.cashPromptShown);
        Assert.assertFalse("Should NOT navigate home", viewStub.homeNavigated);
    }
}