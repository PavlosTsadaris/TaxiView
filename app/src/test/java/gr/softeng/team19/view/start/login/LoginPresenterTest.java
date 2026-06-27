package gr.softeng.team19.view.start.login;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import gr.softeng.team19.domain.Customer;
import gr.softeng.team19.domain.TaxiDriver;
import gr.softeng.team19.memorydao.UserDAOMemory;

import java.time.LocalDate;

/**
 * Unit tests for LoginPresenter to verify authentication and navigation logic.
 */
public class LoginPresenterTest {
    private LoginPresenter presenter;
    private LoginViewStub viewStub;
    private UserDAOMemory userDAO;

    /**
     * Initializes the testing environment and clears the memory database.
     */
    @Before
    public void setUp() {
        viewStub = new LoginViewStub();
        presenter = new LoginPresenter(viewStub);
        userDAO = new UserDAOMemory();
        userDAO.findAll().clear();
    }

    /**
     * Verifies that the sign-up request triggers navigation to the registration screen.
     */
    @Test
    public void testOnSignUp() {
        presenter.onSignUp();
        Assert.assertTrue(viewStub.navigatedToSignUp);
    }

    /**
     * Verifies that the system prevents login when input fields are empty.
     */
    @Test
    public void testLoginEmptyFields() {
        viewStub.username = "";
        viewStub.password = "";
        presenter.onLogin();
        Assert.assertEquals("Please fill all fields.", viewStub.errorMessage);
    }

    /**
     * Verifies error handling when a user provides a non-existent username.
     */
    @Test
    public void testLoginUserNotFound() {
        viewStub.username = "unknown_user";
        viewStub.password = "any_pass";
        presenter.onLogin();
        Assert.assertEquals("User not found.", viewStub.errorMessage);
    }

    /**
     * Verifies error handling for incorrect password attempts.
     */
    @Test
    public void testLoginWrongPassword() {
        Customer existingUser = new Customer(
                "testUser", "correctPass", "email@test.com", "John", "Doe",
                "1234567890", LocalDate.now(), "1234567812345678", 0.0, 0.0,
                "Street", "City", 1, 12345
        );
        userDAO.add(existingUser);

        viewStub.username = "testUser";
        viewStub.password = "wrongPass";
        presenter.onLogin();

        Assert.assertEquals("Wrong Password.", viewStub.errorMessage);
    }

    /**
     * Verifies successful login and navigation for a Customer account.
     */
    @Test
    public void testLoginSuccessfulCustomer() {
        Customer customer = new Customer(
                "cust1", "pass1", "cust@test.com", "Jane", "Doe",
                "1234567890", LocalDate.now(), "1111222233334444", 0.0, 0.0,
                "Street", "City", 2, 54321
        );
        userDAO.add(customer);

        viewStub.username = "cust1";
        viewStub.password = "pass1";
        presenter.onLogin();

        Assert.assertTrue(viewStub.navigatedToCustomerMenu);
        Assert.assertNotNull(viewStub.successMessage);
    }

    /**
     * Verifies successful login and navigation for a TaxiDriver account.
     */
    @Test
    public void testLoginSuccessfulDriver() {
        TaxiDriver driver = new TaxiDriver(
                "driver1", "pass1", "drv@test.com", "George", "Papadopoulos",
                "6912345678", LocalDate.of(1985, 5, 20), "8888999900001111",
                37.9838, 23.7275, "Patission", "Athens", 100, 10434,
                "ZEX-9988", "Mercedes", "E-Class"
        );
        userDAO.add(driver);

        viewStub.username = "driver1";
        viewStub.password = "pass1";
        presenter.onLogin();

        Assert.assertTrue(viewStub.navigatedToDriverMenu);
        Assert.assertFalse(viewStub.navigatedToCustomerMenu);
    }
}