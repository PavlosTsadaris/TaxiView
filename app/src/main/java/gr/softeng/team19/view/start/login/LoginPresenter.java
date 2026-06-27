package gr.softeng.team19.view.start.login;

import gr.softeng.team19.domain.TaxiDriver;
import gr.softeng.team19.domain.User;
import gr.softeng.team19.memorydao.UserDAOMemory;

/**
 * Presenter that manages the logic for logging into the app.
 * It verifies if the username and password are correct and checks
 * if the user is a driver or a customer.
 */
public class LoginPresenter {
    private LoginView view;

    /**
     * Constructor that links the presenter with the login screen interface.
     * @param view The UI interface for the login screen.
     */
    public LoginPresenter(LoginView view) {
        this.view = view;
    }

    /**
     * Handles the login logic when the user clicks the login button.
     * It checks for empty fields, finds the user in the database,
     * and validates the password.
     */
    public void onLogin() {
        String username = view.getUsername().trim();
        String password = view.getPassword().trim();

        // Step 1: Check if the user left any fields blank
        if (username.isEmpty() || password.isEmpty()) {
            view.showErrorMessage("Error", "Please fill all fields.");
            return;
        }

        // Step 2: Look for the user in the database
        User user = new UserDAOMemory().find(username);

        if (user == null) {
            view.showErrorMessage("Login Failed", "User not found.");
            return;
        }

        // Step 3: Verify the password and navigate to the correct screen
        if (!user.getPassword().equals(password)) {
            view.showErrorMessage("Login Failed", "Wrong Password.");
        } else {
            view.showSuccessMessage("Welcome back, " + user.getUserName() + "!");

            // Check the user type to decide which home screen to show
            if(user instanceof TaxiDriver){
                view.navigateToDriverMenu();
            } else {
                view.navigateToCustomerMenu();
            }
        }
    }

    /**
     * Tells the view to open the Sign-Up screen for new users.
     */
    public void onSignUp() {
        view.navigateToSignUp();
    }
}