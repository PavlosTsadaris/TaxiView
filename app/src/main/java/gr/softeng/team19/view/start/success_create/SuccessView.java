package gr.softeng.team19.view.start.success_create;

/**
 * Interface for the Success screen.
 * Defines the UI actions to show a confirmation message and navigate away.
 */
public interface SuccessView {

    /**
     * Updates the screen with the specific success message received.
     * @param message The text to display to the user.
     */
    void setSuccessMessage(String message);

    /**
     * Takes the user back to the main Login screen.
     */
    void navigateToLogin();
}