package gr.softeng.team19.view.start.login;

/**
 * Interface that defines the UI actions for the Login screen.
 * It is implemented by the Activity to handle user input and navigation.
 */
public interface LoginView {

    /**
     * Gets the username entered by the user.
     * @return The username string.
     */
    String getUsername();

    /**
     * Gets the password entered by the user.
     * @return The password string.
     */
    String getPassword();

    /**
     * Displays a popup window with an error message.
     * @param title The title of the error.
     * @param message The details of the error.
     */
    void showErrorMessage(String title, String message);

    /**
     * Displays a brief success message on the screen.
     * @param message The text to show.
     */
    void showSuccessMessage(String message);

    /**
     * Opens the registration screen for new users.
     */
    void navigateToSignUp();

    /**
     * Takes the user to the Customer's home screen.
     */
    void navigateToCustomerMenu();

    /**
     * Takes the user to the Driver's home screen.
     */
    void navigateToDriverMenu();
}