package gr.softeng.team19.view.start.welcome;

/**
 * Presenter that manages the logic for the Welcome screen.
 * It listens for button clicks and tells the view which screen to open next.
 */
public class WelcomePresenter {
    private WelcomeView view;

    /**
     * Constructor that connects the presenter to the welcome screen.
     * @param view The UI interface for the welcome screen.
     */
    public WelcomePresenter(WelcomeView view) {
        this.view = view;
    }

    /**
     * Called when the user clicks the "Create Account" button.
     * Tells the view to show the registration screen.
     */
    public void onCreateAccount() {
        view.navigateToSignUp();
    }

    /**
     * Called when the user clicks the "Login" button.
     * Tells the view to show the login screen.
     */
    public void onLogin() {
        view.navigateToLogin();
    }
}