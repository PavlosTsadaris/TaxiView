package gr.softeng.team19.view.start.firstimage;

/**
 * Manual stub implementation of MainView for testing splash screen navigation.
 */
public class MainViewStub implements MainView {
    /** Counter to track calls to the welcome screen navigation. */
    public int welcomeClicks = 0;

    /**
     * Records a call to navigate to the welcome screen.
     */
    @Override
    public void navigateToWelcome() {
        welcomeClicks++;
    }
}