package gr.softeng.team19.view.driver.homeactivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import gr.softeng.team19.R;
import gr.softeng.team19.view.driver.chooselocation.DriverChooseLocationActivity;
import gr.softeng.team19.view.driver.riderequestsactivity.RideRequestsActivity;
import gr.softeng.team19.view.history.HistoryActivity;
import gr.softeng.team19.view.ratings.RatingsActivity;
import gr.softeng.team19.view.viewmyprofile.MyProfileActivity;

/**
 * The main dashboard for the taxi driver.
 * From here, drivers can manage their profile, view ride history,
 * check new requests, and toggle their online/offline status.
 */
public class HomeDriverActivity extends AppCompatActivity implements HomeDriverView {

    private HomeDriverPresenter presenter;
    private TextView txtDriverName, txtDriverRating;
    private MaterialButton btnToggleLocation;
    private CardView cardProfile, cardHistory, cardRequests, cardRatings;
    private ImageView imgProfileSmall;

    /**
     * Sets up the dashboard UI and links all menu cards and buttons to the presenter.
     * @param savedInstanceState Data from a previous session, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_driver);

        // Link UI components to the Java code
        txtDriverName = findViewById(R.id.txtDriverName);
        txtDriverRating = findViewById(R.id.txtDriverRating);
        btnToggleLocation = findViewById(R.id.btnToggleLocation);
        cardProfile = findViewById(R.id.cardProfile);
        cardHistory = findViewById(R.id.cardHistory);
        imgProfileSmall = findViewById(R.id.imgProfileLarge);
        cardRequests = findViewById(R.id.cardViewRequests);
        cardRatings = findViewById(R.id.cardMyRatings);

        presenter = new HomeDriverPresenter(this);
        String username = getIntent().getStringExtra("username");
        presenter.onHomePageLoad(username);

        // Set up click listeners for the dashboard cards and buttons
        btnToggleLocation.setOnClickListener(v -> presenter.onLocationToggleSelected());
        cardProfile.setOnClickListener(v -> presenter.onProfileSelected());
        cardRequests.setOnClickListener(v -> presenter.onRequestsListSelected());
        imgProfileSmall.setOnClickListener(v -> presenter.onProfileSelected());
        cardHistory.setOnClickListener(v -> presenter.onHistorySelected());
        cardRatings.setOnClickListener(v -> presenter.onRatingsSelected());

        // Adjust padding for system bars (like the status bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Displays the driver's name on the screen.
     * @param nameToDisplay The full name of the driver.
     */
    @Override
    public void setWelcomeMessage(String nameToDisplay) {
        txtDriverName.setText(nameToDisplay);
    }

    /**
     * Updates the rating number shown on the dashboard.
     * @param rating The driver's star rating as a string.
     */
    @Override
    public void setRating(String rating) {
        txtDriverRating.setText(rating);
    }

    /**
     * Changes the UI based on whether the driver is working (online) or not (offline).
     * It updates the button color and text.
     * @param isActive True if the driver is online.
     */
    @Override
    public void updateLocationStatus(boolean isActive) {
        if (isActive) {
            btnToggleLocation.setText(R.string.TurnOffLocation);
            btnToggleLocation.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D32F2F"))); // Red for Offline
            Toast.makeText(this, R.string.msg_online_driver, Toast.LENGTH_SHORT).show();
        } else {
            btnToggleLocation.setText(R.string.TurnOnLocation);
            btnToggleLocation.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1B664D"))); // Green for Online
            Toast.makeText(this, R.string.msg_offline_driver, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Opens the profile screen.
     * @param username The driver's unique username.
     */
    @Override
    public void navigateToProfile(String username) {
        Intent intent = new Intent(this, MyProfileActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    /**
     * Opens the history screen to see past rides.
     */
    @Override
    public void navigateToHistory() {
        Intent intent = new Intent(this, HistoryActivity.class);
        intent.putExtra("username", getIntent().getStringExtra("username"));
        startActivity(intent);
    }

    /**
     * Shows a popup at the bottom of the screen when a new ride request arrives.
     * @param message The text to display in the notification.
     */
    @Override
    public void showRequestNotification(String message) {
        com.google.android.material.snackbar.Snackbar.make(
                        findViewById(R.id.main), message, 5000)
                .setAction("VIEW", v -> {
                    presenter.onRequestsListSelected();
                }).show();
    }

    /**
     * Opens the list of pending ride requests.
     */
    @Override
    public void navigateToRequests() {
        Intent intent = new Intent(this, RideRequestsActivity.class);
        intent.putExtra("username", getIntent().getStringExtra("username"));
        startActivity(intent);
    }

    /**
     * Displays an error message using a long toast notification.
     * @param message The error text.
     */
    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Opens the location selection screen to start the work day.
     */
    @Override
    public void navigateToChooseLocation(){
        Intent intent = new Intent(this, DriverChooseLocationActivity.class);
        intent.putExtra("username", getIntent().getStringExtra("username"));
        startActivity(intent);
    }

    /**
     * Opens the screen where the driver can see reviews and star ratings.
     */
    @Override
    public void navigateToRatings() {
        Intent intent = new Intent(this, RatingsActivity.class);
        intent.putExtra("username", getIntent().getStringExtra("username"));
        startActivity(intent);
    }
}