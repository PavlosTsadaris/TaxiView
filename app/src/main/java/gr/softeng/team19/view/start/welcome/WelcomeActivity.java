package gr.softeng.team19.view.start.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import gr.softeng.team19.R;
import gr.softeng.team19.view.start.login.LoginMainActivity;
import gr.softeng.team19.view.start.signup.SignUpActivity;

/**
 * The landing screen that users see when they first open the app.
 * It lets the user choose between starting a new registration or logging into an existing account.
 */
public class WelcomeActivity extends AppCompatActivity implements WelcomeView {

    private WelcomePresenter presenter;

    /**
     * Sets up the screen and connects the buttons to the presenter.
     * @param savedInstanceState Data from a previous session, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        presenter = new WelcomePresenter(this);

        // Find the buttons for creating an account and logging in
        Button btnCreate = findViewById(R.id.btnCreateAccount);
        Button btnLogin = findViewById(R.id.btnLogin);

        // Tell the presenter when a button is clicked
        btnCreate.setOnClickListener(v -> presenter.onCreateAccount());
        btnLogin.setOnClickListener(v -> presenter.onLogin());
    }

    /**
     * Opens the Login screen.
     */
    @Override
    public void navigateToLogin() {
        Intent intent = new Intent(WelcomeActivity.this, LoginMainActivity.class);
        startActivity(intent);
    }

    /**
     * Opens the Sign-Up (Registration) screen.
     */
    @Override
    public void navigateToSignUp() {
        Intent intent = new Intent(WelcomeActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}