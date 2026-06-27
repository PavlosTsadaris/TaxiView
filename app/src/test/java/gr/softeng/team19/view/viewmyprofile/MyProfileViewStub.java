package gr.softeng.team19.view.viewmyprofile;

/**
 * Stub implementation of MyProfileView for unit testing purposes.
 */
public class MyProfileViewStub implements MyProfileView {
    public String fullName, username, email, phone, address, card;
    public String errorMessage;
    public boolean backNavigated = false;
    public boolean logoutCalled = false;

    /**
     * Captures user details for verification.
     * @param name Full name of the user.
     * @param username Unique identifier.
     * @param email Contact email.
     * @param phone Contact number.
     * @param address Physical address.
     * @param card Masked credit card info.
     */
    @Override
    public void showUserDetails(String name, String username, String email, String phone, String address, String card) {
        this.fullName = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.card = card;
    }

    /**
     * Captures error messages.
     * @param message The error string.
     */
    @Override
    public void showError(String message) {
        this.errorMessage = message;
    }

    /**
     * Records that the back navigation was triggered.
     */
    @Override
    public void navigateBack() {
        this.backNavigated = true;
    }

    /**
     * Records that the logout action was triggered.
     */
    @Override
    public void logout() {
        this.logoutCalled = true;
    }
}