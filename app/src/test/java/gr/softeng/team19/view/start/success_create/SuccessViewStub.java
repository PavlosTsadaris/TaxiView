package gr.softeng.team19.view.start.success_create;

/**
 * Manual stub implementation of SuccessView used for testing UI interactions.
 */
public class SuccessViewStub implements SuccessView {
    /** The message received from the presenter. */
    public String capturedMessage = null;
    /** Flag indicating if login navigation was triggered. */
    public boolean navigatedToLogin = false;

    /**
     * Captures the message to be displayed.
     * @param message The success text received.
     */
    @Override
    public void setSuccessMessage(String message) {
        this.capturedMessage = message;
    }

    /**
     * Marks the login navigation as triggered.
     */
    @Override
    public void navigateToLogin() {
        this.navigatedToLogin = true;
    }
}