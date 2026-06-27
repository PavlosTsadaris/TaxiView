package gr.softeng.team19.view.viewmyprofile;

/**
 * Interface that defines the UI actions for the Profile screen.
 * It is implemented by the Activity to show user details or handle account actions.
 */
public interface MyProfileView {

    /**
     * Updates the screen with the user's personal information.
     * @param name The full name of the user.
     * @param username The unique login name.
     * @param email The contact email address.
     * @param phone The contact phone number.
     * @param address The physical home or work address.
     * @param card The protected (masked) credit card information.
     */
    void showUserDetails(String name, String username, String email, String phone, String address, String card);

    /**
     * Shows an error message if something goes wrong while loading the profile.
     * @param message The error text to display.
     */
    void showError(String message);

    /**
     * Closes the profile screen and returns to the previous page.
     */
    void navigateBack();

    /**
     * Logs the user out and returns them to the login or splash screen.
     */
    void logout();
}