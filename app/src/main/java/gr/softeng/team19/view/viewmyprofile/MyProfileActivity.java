package gr.softeng.team19.view.viewmyprofile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import gr.softeng.team19.R;
import gr.softeng.team19.view.start.login.LoginMainActivity;

import gr.softeng.team19.memorydao.UserDAOMemory;

/**
 * Screen that shows the user's personal profile information.
 * It displays details like name, contact info, and allows the user to log out.
 */
public class MyProfileActivity extends AppCompatActivity implements MyProfileView {

    private MyProfilePresenter presenter;
    private TextView txtName, txtUsername, txtEmail, txtPhone, txtAddress, txtCard;
    private String username;

    /**
     * Sets up the activity, connects the UI elements, and asks the presenter to load the data.
     * @param savedInstanceState Data from a previous session, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        // Link Java variables to XML layout IDs
        initUI();

        // Create the presenter and give it access to the user database
        presenter = new MyProfilePresenter(this, new UserDAOMemory());

        // Get the logged-in username and load their profile
        username = getIntent().getStringExtra("username");
        presenter.onLoadProfile(username);
    }

    /**
     * Finds all the text fields and buttons on the screen and sets up click listeners.
     */
    private void initUI() {
        txtName = findViewById(R.id.txtProfileName);
        txtUsername = findViewById(R.id.txtProfileUsername);
        txtEmail = findViewById(R.id.txtProfileEmail);
        txtPhone = findViewById(R.id.txtProfilePhone);
        txtAddress = findViewById(R.id.txtProfileAddress);
        txtCard = findViewById(R.id.txtProfileCard);

        ImageView btnBack = findViewById(R.id.btnBack);
        MaterialButton btnLogout = findViewById(R.id.btnLogout);

        // Define what happens when buttons are clicked
        btnBack.setOnClickListener(v -> presenter.onBackRequested());
        btnLogout.setOnClickListener(v -> presenter.onLogoutRequested());
    }

    /**
     * Fills the text fields on the screen with the user's information.
     * @param name     Full name of the user.
     * @param username Unique account name.
     * @param email    Contact email.
     * @param phone    Phone number.
     * @param address  Physical address.
     * @param card     Masked card number for security.
     */
    @Override
    public void showUserDetails(String name, String username, String email, String phone, String address, String card) {
        txtName.setText(name);
        txtUsername.setText(username);
        txtEmail.setText(email);
        txtPhone.setText(phone);
        txtAddress.setText(address);
        txtCard.setText(card);
    }

    /**
     * Shows a short error message at the bottom of the screen.
     * @param message The error text to show.
     */
    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Closes the profile screen and returns to the previous page.
     */
    @Override
    public void navigateBack() {
        finish();
    }

    /**
     * Logs the user out, clears the app's history, and takes them back to the login screen.
     */
    @Override
    public void logout() {
        // Create an intent to go to the login screen
        Intent intent = new Intent(this, LoginMainActivity.class);

        // Clear all previous screens so the user can't "go back" into the profile
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
        finish();
    }
}