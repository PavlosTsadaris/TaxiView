package gr.softeng.team19.view.start.success_create;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import gr.softeng.team19.R;
import gr.softeng.team19.view.start.login.LoginMainActivity;

/**
 * Screen that shows a "Success" message after a user finishes registration.
 * It confirms the action was successful and provides a button to go to the login screen.
 */
public class SuccessActivity extends AppCompatActivity implements SuccessView {

    private SuccessPresenter presenter;
    private TextView tvMessage;

    /**
     * Sets up the success screen and displays the message sent from the previous screen.
     * @param savedInstanceState Data from a previous session, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        presenter = new SuccessPresenter(this);

        tvMessage = findViewById(R.id.tvSuccessMessage);
        Button btnLogin = findViewById(R.id.btnGoToLogin);

        // Get the specific success message (e.g., "Account Created!") from the Intent
        String message = getIntent().getStringExtra("MESSAGE");
        presenter.onViewCreated(message);

        // Tell the presenter when the user wants to go to the login screen
        btnLogin.setOnClickListener(v -> presenter.onLoginPressed());
    }

    /**
     * Updates the text on the screen with the success message.
     * @param message The text to show the user.
     */
    @Override
    public void setSuccessMessage(String message) {
        tvMessage.setText(message);
    }

    /**
     * Takes the user back to the Login screen.
     * It clears all previous screens so the user cannot go back to the registration forms.
     */
    @Override
    public void navigateToLogin() {
        Intent intent = new Intent(SuccessActivity.this, LoginMainActivity.class);

        // This flag deletes the app history so the "Back" button won't return here
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }
}