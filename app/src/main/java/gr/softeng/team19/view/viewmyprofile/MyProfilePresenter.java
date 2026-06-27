package gr.softeng.team19.view.viewmyprofile;

import gr.softeng.team19.domain.TaxiDriver;
import gr.softeng.team19.memorydao.TaxiDriverDAOMemory;
import gr.softeng.team19.memorydao.UserDAOMemory;
import gr.softeng.team19.domain.ApplicationUser;

/**
 * Presenter class that manages the user's profile information.
 * It handles loading user data, hiding sensitive info (like credit cards), and logging out.
 */
public class MyProfilePresenter {
    private MyProfileView view;
    private UserDAOMemory userDAO;
    private ApplicationUser user;

    /**
     * Constructor that links the presenter with the view and the user database.
     * @param view The UI interface for the profile screen.
     * @param userDAO The database access object to find user info.
     */
    public MyProfilePresenter(MyProfileView view, UserDAOMemory userDAO) {
        this.view = view;
        this.userDAO = userDAO;
    }

    /**
     * Fetches the user's information using their username and sends it to the UI.
     * @param username The unique name of the user to look up.
     */
    public void onLoadProfile(String username) {
        // If the username is missing, show an error message
        if (username == null || username.isEmpty()) {
            view.showError("User not found");
            return;
        }

        user = userDAO.find(username);

        if (user != null) {
            // Combine name and surname into one string
            String fullname = user.getName() + " " + user.getSurname();
            // Hide the credit card digits for safety
            String maskedCard = maskCardNumber(user.getCreditCardNumber());

            // Send all details to the view to be displayed
            view.showUserDetails(
                    fullname,
                    user.getUserName(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    user.getAddress().toString(),
                    maskedCard
            );
        } else {
            view.showError("Error: User data missing in Database.");
        }
    }

    /**
     * Hides most of the credit card number, showing only the last 4 digits.
     * @param fullCard The full 16-digit credit card number.
     * @return A string like "**** **** **** 1234" for security.
     */
    private String maskCardNumber(String fullCard) {
        if (fullCard == null || fullCard.length() < 4) {
            return "****";
        }
        String lastFour = fullCard.substring(fullCard.length() - 4);
        return "**** **** **** " + lastFour;
    }

    /**
     * Tells the view to close the profile screen and go back.
     */
    public void onBackRequested() {
        view.navigateBack();
    }

    /**
     * Handles the logout action.
     * If the user is a driver, it sets their status to "unavailable" before logging out.
     */
    public void onLogoutRequested() {
        // Special logic: if the user is a driver, make them offline
        if(user instanceof TaxiDriver){
            TaxiDriverDAOMemory taxiDriverDAO = new TaxiDriverDAOMemory();
            taxiDriverDAO.find(user.getUserName()).setAvailability(false);
        }
        // Finally, tell the view to perform the logout
        view.logout();
    }
}