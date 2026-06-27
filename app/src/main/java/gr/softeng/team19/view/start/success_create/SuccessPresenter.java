package gr.softeng.team19.view.start.success_create;

/**
 * Presenter class that manages the "Success" screen logic.
 * It ensures the right message is shown and handles the move back to the login page.
 */
public class SuccessPresenter {
    private SuccessView view;

    /**
     * Constructor that connects the presenter with the success screen UI.
     * @param view The UI interface for the success screen.
     */
    public SuccessPresenter(SuccessView view) {
        this.view = view;
    }

    /**
     * Called when the screen opens to display the confirmation message.
     * @param message The text to show (e.g., "Driver account created successfully").
     */
    public void onViewCreated(String message) {
        // Only update the text if a message was actually sent to this screen
        if (message != null && !message.isEmpty()) {
            view.setSuccessMessage(message);
        }
    }

    /**
     * Handles the logic when the user clicks the "Go to Login" button.
     */
    public void onLoginPressed() {
        view.navigateToLogin();
    }
}