package gr.softeng.team19.view.start.welcome;

/**
 * Interface for the Welcome screen.
 * It defines the navigation options available to the user when they first open the app.
 */
public interface WelcomeView {

    /**
     * Opens the screen where existing users can log in.
     */
    void navigateToLogin();

    /**
     * Opens the screen where new users can create an account.
     */
    void navigateToSignUp();
}