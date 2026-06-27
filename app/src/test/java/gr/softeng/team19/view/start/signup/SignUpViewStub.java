package gr.softeng.team19.view.start.signup;

/**
 * Manual stub implementation of SignUpView for state-based unit testing.
 */
public class SignUpViewStub implements SignUpView {
    // Input fields simulated for the Presenter
    public String username = "", password = "", email = "", name = "", surname = "";
    public String phone = "", birthDate = "", street = "", streetNumber = "";
    public String city = "", postalCode = "", card = "", role = null;

    // Output variables for test verification
    public String lastErrorField = "";
    public int lastErrorResource = 0;
    public int lastErrorMessageResource = 0;
    public boolean navigatedToDriverDocs = false;
    public boolean navigatedToSuccess = false;

    @Override public String getUsername() { return username; }
    @Override public String getPassword() { return password; }
    @Override public String getEmail() { return email; }
    @Override public String getName() { return name; }
    @Override public String getSurname() { return surname; }
    @Override public String getPhone() { return phone; }
    @Override public String getBirthDate() { return birthDate; }
    @Override public String getStreet() { return street; }
    @Override public String getStreetNumber() { return streetNumber; }
    @Override public String getCity() { return city; }
    @Override public String getPostalCode() { return postalCode; }
    @Override public String getCreditCard() { return card; }
    @Override public String getSelectedRole() { return role; }

    /**
     * Records a validation error on a specific input field.
     * @param fieldName The name of the field with the error.
     * @param resourceId The Android string resource ID for the error message.
     */
    @Override
    public void showFieldError(String fieldName, int resourceId) {
        this.lastErrorField = fieldName;
        this.lastErrorResource = resourceId;
    }

    /**
     * Records a general error message.
     * @param resourceId The Android string resource ID for the message.
     */
    @Override
    public void showErrorMessage(int resourceId) {
        this.lastErrorMessageResource = resourceId;
    }

    /**
     * Flags that the driver document navigation was triggered.
     */
    @Override public void navigateToDriverDocs() { navigatedToDriverDocs = true; }

    /**
     * Flags that the success screen navigation was triggered.
     */
    @Override public void navigateToSuccess() { navigatedToSuccess = true; }
}