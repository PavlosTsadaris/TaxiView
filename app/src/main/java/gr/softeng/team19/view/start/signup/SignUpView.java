package gr.softeng.team19.view.start.signup;

/**
 * Interface that defines the UI actions for the Registration (Sign Up) screen.
 * It is implemented by the Activity to handle data collection and navigation logic.
 */
public interface SignUpView {

    // ---  (Getters) ---
    // These methods allow the presenter to read what the user typed in the fields.

    String getUsername();
    String getPassword();
    String getEmail();
    String getName();
    String getSurname();
    String getPhone();
    String getBirthDate();
    String getStreet();
    String getStreetNumber();
    String getCity();
    String getPostalCode();
    String getCreditCard();

    /**
     * Gets the role selected by the user (e.g., "Driver" or "Customer").
     * @return The selected role as a string.
     */
    String getSelectedRole();

    // --- Feedback Methods ---

    /**
     * Shows a general error message to the user.
     * @param resourceId The ID of the string to show.
     */
    void showErrorMessage(int resourceId);

    /**
     * Highlights a specific field (like the email) and shows a specific error message on it.
     * @param fieldName The name of the field with the error.
     * @param resourceId The ID of the error message to display.
     */
    void showFieldError(String fieldName, int resourceId);

    // --- Navigation ---

    /**
     * Takes a Driver to the document upload screen.
     */
    void navigateToDriverDocs();

    /**
     * Takes a Customer to the final success screen.
     */
    void navigateToSuccess();
}