package gr.softeng.team19.view.start.signup;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import gr.softeng.team19.R;
import gr.softeng.team19.domain.Customer;
import gr.softeng.team19.domain.RegistrationData;
import gr.softeng.team19.memorydao.UserDAOMemory;
import gr.softeng.team19.memorydao.CustomerDAOMemory;

/**
 * Unit Tests for SignUpPresenter to verify form validation and registration flows.
 */
public class SignUpPresenterTest {
    private SignUpPresenter presenter;
    private SignUpViewStub viewStub;

    /**
     * Resets the testing environment and clears static memory storage.
     */
    @Before
    public void setUp() {
        viewStub = new SignUpViewStub();
        presenter = new SignUpPresenter(viewStub);

        UserDAOMemory userDAO = new UserDAOMemory();
        CustomerDAOMemory customerDAO = new CustomerDAOMemory();

        userDAO.findAll().clear();
        customerDAO.findAll().clear();
        RegistrationData.clear();
    }

    /**
     * Helper to populate the view stub with valid default registration data.
     */
    private void fillValidData() {
        viewStub.username = "uniqueUser";
        viewStub.password = "pass123";
        viewStub.email = "test@example.com";
        viewStub.name = "John";
        viewStub.surname = "Doe";
        viewStub.phone = "1234567890";
        viewStub.birthDate = "01/01/1990";
        viewStub.street = "Main Street";
        viewStub.streetNumber = "10";
        viewStub.city = "Athens";
        viewStub.postalCode = "12345";
        viewStub.card = "1234567812345678";
        viewStub.role = "Customer";
    }

    @Test
    public void testEmptyUsername() {
        viewStub.username = "";
        presenter.onSignUp();
        Assert.assertEquals(R.string.error_username_required, viewStub.lastErrorResource);
    }

    @Test
    public void testEmptyPassword() {
        viewStub.username = "user"; viewStub.password = "";
        presenter.onSignUp();
        Assert.assertEquals(R.string.error_password_required, viewStub.lastErrorResource);
    }

    @Test
    public void testInvalidEmail() {
        fillValidData(); viewStub.email = "invalidemail";
        presenter.onSignUp();
        Assert.assertEquals(R.string.error_email_invalid, viewStub.lastErrorResource);
    }

    @Test
    public void testEmptyName() {
        fillValidData(); viewStub.name = "";
        presenter.onSignUp();
        Assert.assertEquals(R.string.error_firstname_required, viewStub.lastErrorResource);
    }

    @Test
    public void testEmptySurname() {
        fillValidData(); viewStub.surname = "";
        presenter.onSignUp();
        Assert.assertEquals(R.string.error_lastname_required, viewStub.lastErrorResource);
    }

    @Test
    public void testInvalidPhone() {
        fillValidData(); viewStub.phone = "123";
        presenter.onSignUp();
        Assert.assertEquals(R.string.error_phone_invalid, viewStub.lastErrorResource);
    }

    @Test
    public void testInvalidBirthDate() {
        fillValidData(); viewStub.birthDate = "35/13/2020";
        presenter.onSignUp();
        Assert.assertEquals(R.string.error_date_invalid, viewStub.lastErrorResource);
    }

    @Test
    public void testEmptyStreet() {
        fillValidData(); viewStub.street = "";
        presenter.onSignUp();
        Assert.assertEquals(R.string.error_street_required, viewStub.lastErrorResource);
    }

    @Test
    public void testEmptyStreetNumber() {
        fillValidData(); viewStub.streetNumber = "";
        presenter.onSignUp();
        Assert.assertEquals(R.string.error_street_number_required, viewStub.lastErrorResource);
    }

    @Test
    public void testEmptyPostalCode() {
        fillValidData(); viewStub.postalCode = "";
        presenter.onSignUp();
        Assert.assertEquals(R.string.error_postal_code_required, viewStub.lastErrorResource);
    }

    @Test
    public void testEmptyCity() {
        fillValidData(); viewStub.city = "";
        presenter.onSignUp();
        Assert.assertEquals(R.string.error_city_required, viewStub.lastErrorResource);
    }

    @Test
    public void testInvalidCard() {
        fillValidData(); viewStub.card = "notdigits";
        presenter.onSignUp();
        Assert.assertEquals(R.string.error_card_invalid, viewStub.lastErrorResource);
    }

    @Test
    public void testNullRole() {
        fillValidData(); viewStub.role = null;
        presenter.onSignUp();
        Assert.assertEquals(R.string.error_role_required, viewStub.lastErrorMessageResource);
    }

    @Test
    public void testUsernameTaken() {
        fillValidData();
        new UserDAOMemory().add(new Customer(viewStub.username, "p", "e", "n", "s", "p", null, "c", 0.0,0.0,"s","c",1,1));

        presenter.onSignUp();
        Assert.assertEquals(R.string.error_username_taken, viewStub.lastErrorResource);
    }

    /**
     * Verifies that a valid driver registration leads to the document upload step.
     */
    @Test
    public void testSuccessfulDriver() {
        fillValidData();
        viewStub.username = "driverUser";
        viewStub.role = "Driver";

        presenter.onSignUp();

        Assert.assertTrue(viewStub.navigatedToDriverDocs);
        Assert.assertEquals("driverUser", RegistrationData.username);
        // Έλεγχος αν το parsing σε Integer έγινε σωστά
        Assert.assertEquals(10, RegistrationData.streetNumber);
    }

    /**
     * Verifies that a valid customer registration leads to a success message and correct persistence.
     */
    @Test
    public void testSuccessfulCustomer() {
        fillValidData();
        viewStub.username = "customerUser";
        viewStub.role = "Customer";

        presenter.onSignUp();

        Assert.assertTrue("Navigation to success failed", viewStub.navigatedToSuccess);

        CustomerDAOMemory customerDAO = new CustomerDAOMemory();
        Customer saved = customerDAO.find("customerUser");
        Assert.assertNotNull("Customer was not saved in DAO", saved);
        Assert.assertEquals(12345, (int) saved.getAddress().getPostalCode());
    }
}