package gr.softeng.team19.view.start.welcome;

/**
 * Manual stub implementation of WelcomeView used for testing navigation states.
 */
public class WelcomeViewStub implements WelcomeView {
    /** Counter for sign-up navigation calls. */
    public int signUpClicks = 0;
    /** Counter for login navigation calls. */
    public int loginClicks = 0;

    /**
     * Increments the login navigation counter.
     */
    @Override
    public void navigateToLogin() {
        loginClicks++;
    }

    /**
     * Increments the sign-up navigation counter.
     */
    @Override
    public void navigateToSignUp() {
        signUpClicks++;
    }
}