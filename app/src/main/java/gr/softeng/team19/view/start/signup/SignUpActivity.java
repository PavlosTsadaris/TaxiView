package gr.softeng.team19.view.start.signup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import gr.softeng.team19.R;
import gr.softeng.team19.view.start.driverdocs.DriverDocumentsActivity;
import gr.softeng.team19.view.start.login.LoginMainActivity;
import gr.softeng.team19.view.start.success_create.SuccessActivity;

/**
 * Screen that handles the registration of new users.
 * It collects personal data, address, and payment info, then directs
 * Customers to success and Drivers to the document upload screen.
 */
public class SignUpActivity extends AppCompatActivity implements SignUpView {

    private SignUpPresenter presenter;

    private EditText etUsername, etPassword, etEmail, etName, etSurname, etPhone,
            etBirthDate, etStreet, etStreetNumber, etCity, etPostalCode, etCard;
    private RadioGroup rgRole;

    /**
     * Sets up the registration screen and connects all input fields and buttons.
     * @param savedInstanceState Data from a previous session, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        presenter = new SignUpPresenter(this);

        // Link all UI fields to their IDs
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etPhone = findViewById(R.id.etPhone);
        etBirthDate = findViewById(R.id.etBirthDate);
        etStreet = findViewById(R.id.etStreet);
        etStreetNumber = findViewById(R.id.etStreetNumber);
        etCity = findViewById(R.id.etCity);
        etPostalCode = findViewById(R.id.etPostalCode);
        etCard = findViewById(R.id.etCreditCard);
        rgRole = findViewById(R.id.rgRole);

        // Submit button click
        findViewById(R.id.btnCompleteSignUp).setOnClickListener(v -> presenter.onSignUp());

        // Back to login link
        findViewById(R.id.tvLoginLink).setOnClickListener(v -> {
            startActivity(new Intent(this, LoginMainActivity.class));
            finish();
        });
    }

    // --- Data Retrieval Methods ---

    @Override
    public String getUsername() { return etUsername.getText().toString(); }

    @Override
    public String getPassword() { return etPassword.getText().toString(); }

    @Override
    public String getEmail() { return etEmail.getText().toString(); }

    @Override
    public String getName() { return etName.getText().toString(); }

    @Override
    public String getSurname() { return etSurname.getText().toString(); }

    @Override
    public String getPhone() { return etPhone.getText().toString(); }

    @Override
    public String getBirthDate() { return etBirthDate.getText().toString(); }

    @Override
    public String getStreet() { return etStreet.getText().toString(); }

    @Override
    public String getStreetNumber() { return etStreetNumber.getText().toString(); }

    @Override
    public String getCity() { return etCity.getText().toString(); }

    @Override
    public String getPostalCode() { return etPostalCode.getText().toString(); }

    @Override
    public String getCreditCard() { return etCard.getText().toString(); }

    /**
     * Checks which role was selected (Customer or Driver).
     * @return The text of the selected radio button.
     */
    @Override
    public String getSelectedRole() {
        int id = rgRole.getCheckedRadioButtonId();
        if (id == -1) return null;
        RadioButton rb = findViewById(id);
        return rb.getText().toString();
    }

    /**
     * Shows a standard toast error message.
     * @param messageId The ID of the string resource to show.
     */
    @Override
    public void showErrorMessage(int messageId) {
        Toast.makeText(this, getString(messageId), Toast.LENGTH_SHORT).show();
    }

    /**
     * Highlights a specific field that has an error (e.g., missing name).
     * @param fieldName The name of the field to highlight.
     * @param resourceId The error message to display on that field.
     */
    @Override
    public void showFieldError(String fieldName, int resourceId) {
        String message = getString(resourceId);

        // Find which field failed validation and show the error icon/message
        switch (fieldName) {
            case "username":
                etUsername.setError(message);
                etUsername.requestFocus();
                break;
            case "password":
                etPassword.setError(message);
                etPassword.requestFocus();
                break;
            case "email":
                etEmail.setError(message);
                etEmail.requestFocus();
                break;
            case "name":
                etName.setError(message);
                etName.requestFocus();
                break;
            case "surname":
                etSurname.setError(message);
                etSurname.requestFocus();
                break;
            case "phone":
                etPhone.setError(message);
                etPhone.requestFocus();
                break;
            case "birthdate":
                etBirthDate.setError(message);
                etBirthDate.requestFocus();
                break;
            case "street":
                etStreet.setError(message);
                etStreet.requestFocus();
                break;
            case "streetNumber":
                etStreetNumber.setError(message);
                etStreetNumber.requestFocus();
                break;
            case "city":
                etCity.setError(message);
                etCity.requestFocus();
                break;
            case "postalCode":
                etPostalCode.setError(message);
                etPostalCode.requestFocus();
                break;
            case "card":
                etCard.setError(message);
                etCard.requestFocus();
                break;
        }
    }

    /**
     * Redirects a new Driver to the document upload screen.
     */
    @Override
    public void navigateToDriverDocs() {
        Intent intent = new Intent(this, DriverDocumentsActivity.class);
        startActivity(intent);
    }

    /**
     * Redirects a new Customer to the success screen.
     */
    @Override
    public void navigateToSuccess() {
        Intent intent = new Intent(this, SuccessActivity.class);
        intent.putExtra("MESSAGE", getString(R.string.msg_customer_success));
        startActivity(intent);
        finish();
    }
}