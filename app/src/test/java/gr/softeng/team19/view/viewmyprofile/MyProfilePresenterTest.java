package gr.softeng.team19.view.viewmyprofile;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDate;
import gr.softeng.team19.domain.Customer;
import gr.softeng.team19.domain.TaxiDriver;
import gr.softeng.team19.memorydao.TaxiDriverDAOMemory;
import gr.softeng.team19.memorydao.UserDAOMemory;

/**
 * Unit Tests for MyProfilePresenter focusing on profile display and logout logic.
 */
public class MyProfilePresenterTest {
    private MyProfilePresenter presenter;
    private MyProfileViewStub viewStub;
    private UserDAOMemory userDAO;
    private TaxiDriverDAOMemory taxiDriverDAO;

    /**
     * Initializes test environment and clears static memory DAOs.
     */
    @Before
    public void setUp() {
        viewStub = new MyProfileViewStub();
        userDAO = new UserDAOMemory();
        taxiDriverDAO = new TaxiDriverDAOMemory();

        userDAO.findAll().clear();
        taxiDriverDAO.findAll().clear();

        presenter = new MyProfilePresenter(viewStub, userDAO);
    }

    /**
     * Verifies error handling when a null username is provided.
     */
    @Test
    public void testOnLoadProfileNull() {
        presenter.onLoadProfile(null);
        Assert.assertEquals("User not found", viewStub.errorMessage);
    }

    /**
     * Verifies error handling when an empty username is provided.
     */
    @Test
    public void testOnLoadProfileEmpty() {
        presenter.onLoadProfile("");
        Assert.assertEquals("User not found", viewStub.errorMessage);
    }

    /**
     * Verifies behavior when the username does not exist in the DAO.
     */
    @Test
    public void testOnLoadProfileUserNotFound() {
        presenter.onLoadProfile("missing_user");
        Assert.assertEquals("Error: User data missing in Database.", viewStub.errorMessage);
    }

    /**
     * Verifies successful data loading and credit card masking for a customer.
     */
    @Test
    public void testOnLoadProfileSuccess() {
        Customer customer = new Customer("user1", "p", "e", "John", "Doe", "123", LocalDate.now(), "1234567890123456", 0.0, 0.0, "Street", "City", 1, 1);
        userDAO.add(customer);

        presenter.onLoadProfile("user1");

        Assert.assertEquals("John Doe", viewStub.fullName);
        Assert.assertEquals("**** **** **** 3456", viewStub.card);
    }

    /**
     * Verifies masking fallback when the card number is too short.
     */
    @Test
    public void testOnLoadProfileShortCard() {
        Customer customer = new Customer("user2", "p", "e", "n", "s", "1", LocalDate.now(), "123", 0.0, 0.0, "s", "c", 1, 1);
        userDAO.add(customer);

        presenter.onLoadProfile("user2");
        Assert.assertEquals("****", viewStub.card);
    }

    /**
     * Verifies that the back navigation request triggers the view.
     */
    @Test
    public void testOnBackRequested() {
        presenter.onBackRequested();
        Assert.assertTrue(viewStub.backNavigated);
    }

    /**
     * Verifies standard logout flow for regular customer users.
     */
    @Test
    public void testOnLogoutRequestedRegularUser() {
        Customer customer = new Customer("user1", "p", "e", "n", "s", "1", LocalDate.now(), "c", 0.0,0.0,"s","c",1,1);
        userDAO.add(customer);
        presenter.onLoadProfile("user1");

        presenter.onLogoutRequested();
        Assert.assertTrue(viewStub.logoutCalled);
    }

    /**
     * Verifies that a driver's availability is set to false upon logout.
     */
    @Test
    public void testOnLogoutRequestedTaxiDriver() {
        TaxiDriver driver = new TaxiDriver("driver1", "p", "e", "n", "s", "1", LocalDate.now(), "c", 0.0, 0.0, "s", "c", 1, 1, "plate", "make", "model");
        driver.setAvailability(true);
        userDAO.add(driver);
        taxiDriverDAO.save(driver);

        presenter.onLoadProfile("driver1");
        presenter.onLogoutRequested();

        Assert.assertTrue(viewStub.logoutCalled);
        Assert.assertFalse(taxiDriverDAO.find("driver1").getAvailability());
    }
}