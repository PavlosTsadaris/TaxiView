package gr.softeng.team19.view.start.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import gr.softeng.team19.R;
import gr.softeng.team19.view.customer.homeactivity.HomeCustomerActivity;
import gr.softeng.team19.view.driver.homeactivity.HomeDriverActivity;
import gr.softeng.team19.view.start.signup.SignUpActivity;

/**
 * Screen that allows users to log in to their account.
 * It handles username/password input and sends the user to the correct
 * dashboard (Driver or Customer) based on their account type.
 */
public class LoginMainActivity extends AppCompatActivity implements LoginView {

    private LoginPresenter presenter;
    private EditText etUsername, etPassword;

    /**
     * Sets up the login UI and links the buttons to the presenter.
     * @param savedInstanceState Data from a previous session, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        presenter = new LoginPresenter(this);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView btnSignUp = findViewById(R.id.tvSignUp);

        // Send click events to the presenter for processing
        btnLogin.setOnClickListener(v -> presenter.onLogin());
        btnSignUp.setOnClickListener(v -> presenter.onSignUp());
    }

    /**
     * Gets the text from the username field and removes extra spaces.
     * @return The entered username.
     */
    @Override
    public String getUsername() { return etUsername.getText().toString().trim(); }

    /**
     * Gets the text from the password field.
     * @return The entered password.
     */
    @Override
    public String getPassword() { return etPassword.getText().toString(); }

    /**
     * Shows a popup window with an error message.
     * @param title The bold text at the top of the popup.
     * @param message The detailed explanation of the error.
     */
    @Override
    public void showErrorMessage(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    /**
     * Shows a quick success message at the bottom of the screen.
     * @param message The text content of the message.
     */
    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Opens the registration screen for new users.
     */
    @Override
    public void navigateToSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    /**
     * Takes the logged-in customer to their home screen and clears the login history.
     */
    @Override
    public void navigateToCustomerMenu() {
        Intent intent = new Intent(this, HomeCustomerActivity.class);
        // Clear history so the user can't "go back" to the login screen after entering
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("username", getUsername());
        startActivity(intent);
        finish();
    }

    /**
     * Takes the logged-in driver to their home screen and clears the login history.
     */
    @Override
    public void navigateToDriverMenu() {
        Intent intent = new Intent(this, HomeDriverActivity.class);
        // Clear history for security and better navigation flow
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("username", getUsername());
        startActivity(intent);
        finish();
    }
}