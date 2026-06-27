package gr.softeng.team19.view.start.signup;

import android.util.Log;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import gr.softeng.team19.R;
import gr.softeng.team19.domain.Customer;
import gr.softeng.team19.domain.RegistrationData;
import gr.softeng.team19.memorydao.CustomerDAOMemory;
import gr.softeng.team19.memorydao.UserDAOMemory;

/**
 * Presenter class that handles the logic for registering new users.
 * It checks if the input data is correct and decides if the user
 * should go to the success screen or the driver documents screen.
 */
public class SignUpPresenter {
    private SignUpView view;

    /**
     * Constructor that links the presenter with the signup screen.
     * @param view The UI interface for the registration screen.
     */
    public SignUpPresenter(SignUpView view) {
        this.view = view;
    }

    /**
     * Handles the signup process when the user clicks the submit button.
     * It validates all fields (email, phone, dates, etc.) and saves
     * the user to the database if everything is correct.
     */
    public void onSignUp() {
        // Collect all data from the screen
        String username = view.getUsername().trim();
        String password = view.getPassword().trim();
        String email = view.getEmail().trim();
        String name = view.getName().trim();
        String surname = view.getSurname().trim();
        String phone = view.getPhone().trim();
        String birthDateString = view.getBirthDate().trim();
        String street = view.getStreet().trim();
        String streetNumber = view.getStreetNumber().trim();
        String city = view.getCity().trim();
        String postalCode = view.getPostalCode().trim();
        String card = view.getCreditCard().trim();
        String role = view.getSelectedRole();

        // Basic validation for required account fields
        if (username.isEmpty()) {
            view.showFieldError("username", R.string.error_username_required);
            return;
        }
        if (password.isEmpty()) {
            view.showFieldError("password", R.string.error_password_required);
            return;
        }
        if (email.isEmpty() || !email.contains("@")) {
            view.showFieldError("email", R.string.error_email_invalid);
            return;
        }

        if (name.isEmpty()) {
            view.showFieldError("name", R.string.error_firstname_required);
            return;
        }
        if (surname.isEmpty()) {
            view.showFieldError("surname", R.string.error_lastname_required);
            return;
        }

        // Phone must be exactly 10 digits
        if (phone.length() != 10 || !phone.matches("\\d+")) {
            view.showFieldError("phone", R.string.error_phone_invalid);
            return;
        }

        // Check if the birth date format is correct (dd/MM/yyyy)
        LocalDate birthDate = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            birthDate = LocalDate.parse(birthDateString, formatter);
        } catch (DateTimeParseException e) {
            view.showFieldError("birthdate", R.string.error_date_invalid);
            return;
        }

        if (street.isEmpty()) {
            view.showFieldError("street", R.string.error_street_required);
            return;
        }
        if (streetNumber.isEmpty()) {
            view.showFieldError("streetNumber", R.string.error_street_number_required);
            return;
        }
        if (postalCode.isEmpty()) {
            view.showFieldError("postalCode", R.string.error_postal_code_required);
            return;
        }

        if (city.isEmpty()) {
            view.showFieldError("city", R.string.error_city_required);
            return;
        }

        // Card must be exactly 16 digits
        if (card.length() != 16 || !card.matches("\\d+")) {
            view.showFieldError("card", R.string.error_card_invalid);
            return;
        }

        // Check if the user selected "Customer" or "Driver"
        if (role == null) {
            view.showErrorMessage(R.string.error_role_required);
            return;
        }

        // Check if the username is already taken by another user
        UserDAOMemory userDAOMemory = new UserDAOMemory();
        if (userDAOMemory.find(username) != null) {
            view.showFieldError("username", R.string.error_username_taken);
            return;
        }

        // If the user is a Driver, save data temporarily and ask for car documents
        if (role.equals("Driver")) {
            RegistrationData.username = username;
            RegistrationData.password = password;
            RegistrationData.email = email;
            RegistrationData.name = name;
            RegistrationData.surname = surname;
            RegistrationData.phone = phone;
            RegistrationData.birthDate = birthDate;
            RegistrationData.card = card;
            RegistrationData.street = street;
            RegistrationData.city = city;
            RegistrationData.streetNumber = Integer.parseInt(streetNumber);
            RegistrationData.postalCode = Integer.parseInt(postalCode);

            view.navigateToDriverDocs();
        }
        // If the user is a Customer, save them immediately and go to success screen
        else {
            Customer newCustomer = new Customer(
                    username, password, email, name, surname, phone, birthDate,
                    card, 0.0, 0.0, street, city, Integer.parseInt(streetNumber), Integer.parseInt(postalCode)
            );
            RegistrationData.clear();
            CustomerDAOMemory memoryCustomer = new CustomerDAOMemory();
            UserDAOMemory memoryUser = new UserDAOMemory();

            memoryUser.add(newCustomer);
            memoryCustomer.save(newCustomer);

            view.navigateToSuccess();
        }
    }
}