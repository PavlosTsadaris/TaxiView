package gr.softeng.team19.view.rideRequests;

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
import gr.softeng.team19.domain.TaxiRideRequest;

/**
 * Activity class that displays the list of ride requests for a specific customer.
 * It connects the UI with the RideRequestsPresenter.
 */
public class RideRequestsActivity extends AppCompatActivity implements RideRequestsView {

    private RecyclerView recyclerRequests;
    private LinearLayout layoutEmptyState;
    private ImageView btnBack;
    private RideRequestsPresenter presenter;
    private RideRequestsAdapter adapter;

    /**
     * Initializes the activity, sets up the UI components, and the presenter.
     * @param savedInstanceState Bundle containing the activity's previously frozen state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_ride_requests);

        recyclerRequests = findViewById(R.id.recyclerRequests);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        btnBack = findViewById(R.id.btnBack);

        recyclerRequests.setLayoutManager(new LinearLayoutManager(this));

        // Get the username passed from the previous activity
        String username = getIntent().getStringExtra("username");
        presenter = new RideRequestsPresenter(this, username);

        // Set up the back button click listener
        btnBack.setOnClickListener(v -> presenter.goBack());
    }

    /**
     * Updates the UI to show the list of requests using the adapter.
     * @param requests The list of TaxiRideRequest objects to be displayed.
     */
    @Override
    public void showRequestsList(List<TaxiRideRequest> requests) {
        layoutEmptyState.setVisibility(View.GONE);
        recyclerRequests.setVisibility(View.VISIBLE);
        adapter = new RideRequestsAdapter(requests);
        recyclerRequests.setAdapter(adapter);
    }

    /**
     * Updates the UI to show an empty state when no requests are found.
     */
    @Override
    public void showEmptyState() {
        recyclerRequests.setVisibility(View.GONE);
        layoutEmptyState.setVisibility(View.VISIBLE);
    }

    /**
     * Displays a short toast message to the user.
     * @param message The text to be displayed in the toast.
     */
    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Closes the current activity and returns to the previous screen.
     */
    @Override
    public void navigateBack() {
        finish();
    }
}