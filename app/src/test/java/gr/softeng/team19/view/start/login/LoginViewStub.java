package gr.softeng.team19.view.start.login;

/**
 * Manual stub implementation of LoginView for state-based testing.
 */
public class LoginViewStub implements LoginView {
    public String username = "";
    public String password = "";

    public String errorTitle, errorMessage, successMessage;
    public boolean navigatedToSignUp = false;
    public boolean navigatedToCustomerMenu = false;
    public boolean navigatedToDriverMenu = false;

    /** @return The username stored in the stub. */
    @Override public String getUsername() { return username; }

    /** @return The password stored in the stub. */
    @Override public String getPassword() { return password; }

    /**
     * Captures error details for verification.
     * @param title The error heading.
     * @param message The detailed error description.
     */
    @Override
    public void showErrorMessage(String title, String message) {
        this.errorTitle = title;
        this.errorMessage = message;
    }

    /**
     * Captures a success message.
     * @param message The success text received.
     */
    @Override
    public void showSuccessMessage(String message) {
        this.successMessage = message;
    }

    /** Flags that sign-up navigation was triggered. */
    @Override public void navigateToSignUp() { navigatedToSignUp = true; }

    /** Flags that customer menu navigation was triggered. */
    @Override public void navigateToCustomerMenu() { navigatedToCustomerMenu = true; }

    /** Flags that driver menu navigation was triggered. */
    @Override public void navigateToDriverMenu() { navigatedToDriverMenu = true; }
}