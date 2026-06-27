package gr.softeng.team19.view.start.firstimage;

/**
 * Presenter for the Splash Screen.
 * Handles the logic of moving from the logo to the Welcome screen.
 */
public class MainPresenter {
    private MainView view;

    /**
     * Connects the presenter to the splash view.
     * @param view The UI interface for the splash screen.
     */
    public MainPresenter(MainView view) {
        this.view = view;
    }

    /**
     * Called when the timer ends.
     * Switches the screen to WelcomeActivity.
     */
    public void onSplashFinished() {
        view.navigateToWelcome();
    }
}