package gr.softeng.team19.view.driver.riderequestsactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import gr.softeng.team19.R;
import java.util.ArrayList;
import gr.softeng.team19.domain.TaxiRideRequest;
import gr.softeng.team19.view.driver.rideexecution.RideNavigationActivity;

/**
 * Screen that shows a list of nearby ride requests to the driver.
 * Drivers can look through the available trips and choose which one to accept.
 */
public class RideRequestsActivity extends AppCompatActivity implements RideRequestsView {

    private RideRequestsPresenter presenter;
    private RideRequestsAdapter adapter;

    /**
     * Sets up the list (RecyclerView) and starts searching for nearby requests.
     * @param savedInstanceState Data from a previous session, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_requests);

        // Back button to return to the home dashboard
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Setup the scrolling list
        RecyclerView recyclerView = findViewById(R.id.recyclerRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create the adapter to handle the items in the list
        adapter = new RideRequestsAdapter(new ArrayList<>(), request -> presenter.onAcceptRequest(request), new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Get the driver's ID and tell the presenter to load the data
        String username = getIntent().getStringExtra("username");
        presenter = new RideRequestsPresenter(this, username);
        presenter.startLoadingProcess();
    }

    /**
     * Refreshes the screen with the latest list of requests and how far away they are.
     * @param requests  The list of customers looking for a taxi.
     * @param distances How many kilometers away each customer is.
     */
    @Override
    public void updateRequestList(ArrayList<TaxiRideRequest> requests, ArrayList<Double> distances) {
        adapter.setRequests(requests);
        adapter.setDistance(distances);
        adapter.notifyDataSetChanged(); // Refresh the UI to show new data
    }

    /**
     * Shows a quick message on the screen.
     * @param message The text to show.
     */
    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Closes this screen.
     */
    @Override
    public void finishActivity() {
        finish();
    }

    /**
     * Opens the map navigation screen for a specific ride.
     * This screen is closed after moving to navigation so the driver
     * doesn't see the old request list.
     * @param requestID The unique ID of the trip the driver accepted.
     */
    @Override
    public void navigateToNavigationMap(String requestID) {
        Intent intent = new Intent(this, RideNavigationActivity.class);
        intent.putExtra("requestID", requestID);
        startActivity(intent);
        finish();
    }
}