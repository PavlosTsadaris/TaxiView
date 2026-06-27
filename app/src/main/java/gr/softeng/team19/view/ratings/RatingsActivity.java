package gr.softeng.team19.view.ratings;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gr.softeng.team19.R;
import gr.softeng.team19.domain.TaxiBooking;

/**
 * Activity class that displays the list of ratings and feedback for a driver.
 * It uses a RecyclerView to show the data or an empty screen if no ratings exist.
 */
public class RatingsActivity extends AppCompatActivity implements RatingsView {
    private RecyclerView recyclerRatings;
    private LinearLayout layoutEmptyState;
    private ImageView btnBack;

    private RatingsPresenter presenter;
    private RatingsAdapter adapter;

    /**
     * Sets up the activity, finds UI components, and starts the presenter.
     * @param savedInstanceState Data from a previous session, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_ratings);

        recyclerRatings = findViewById(R.id.recyclerRatings);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        btnBack = findViewById(R.id.btnBack);

        recyclerRatings.setLayoutManager(new LinearLayoutManager(this));

        // Get the driver's username to load their specific ratings
        String username = getIntent().getStringExtra("username");

        presenter = new RatingsPresenter(this, username);

        // Set the back button to tell the presenter to return to the previous screen
        btnBack.setOnClickListener(v -> presenter.goBack());
    }

    /**
     * Shows the list of ratings on the screen and hides the empty message.
     * @param bookings The list of bookings that contain rating details.
     */
    @Override
    public void showHistoryList(List<TaxiBooking> bookings) {
        layoutEmptyState.setVisibility(View.GONE);
        recyclerRatings.setVisibility(View.VISIBLE);

        adapter = new RatingsAdapter(bookings);
        recyclerRatings.setAdapter(adapter);
    }

    /**
     * Shows a message on the screen when no ratings are available for this driver.
     */
    @Override
    public void showEmptyState() {
        recyclerRatings.setVisibility(View.GONE);
        layoutEmptyState.setVisibility(View.VISIBLE);
    }

    /**
     * Displays a short toast message to the user.
     * @param message The text content to show.
     */
    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Closes this screen and goes back to the previous one.
     */
    @Override
    public void navigateBack() {
        finish();
    }
}