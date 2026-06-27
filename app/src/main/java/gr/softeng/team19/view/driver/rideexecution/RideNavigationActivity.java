package gr.softeng.team19.view.driver.rideexecution;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import gr.softeng.team19.R;
import gr.softeng.team19.view.driver.TrackRide.TrackRideActivity;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

/**
 * Screen that guides the driver to the customer's pickup location.
 * It shows a map with a pin at the customer's address and allows the driver
 * to confirm when the customer has entered the taxi.
 */
public class RideNavigationActivity extends AppCompatActivity implements RideNavigationView {
    private RideNavigationPresenter presenter;
    private MapView map;

    /**
     * Initializes the map and links the buttons for picking up the customer or canceling.
     * @param savedInstanceState Data from a previous session, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_navigation);

        // Basic map setup
        map = findViewById(R.id.mapNavigation);
        map.setMultiTouchControls(true);
        map.getController().setZoom(17.0);

        presenter = new RideNavigationPresenter(this);

        // Start the navigation logic using the request ID sent from the previous screen
        presenter.startNavigation(getIntent().getStringExtra("requestID"));

        // Button to confirm the customer is in the car
        findViewById(R.id.btnSelectCustomer).setOnClickListener(v -> presenter.onSelectCustomer());

        // Button to stop the navigation and go back
        findViewById(R.id.btnCancel).setOnClickListener(v -> presenter.onCancel());
    }

    /**
     * Sets the map camera to the customer's location and places a marker pin.
     * @param lat The latitude of the pickup spot.
     * @param lon The longitude of the pickup spot.
     * @param locationName The name of the street or area.
     */
    @Override
    public void setupMap(double lat, double lon, String locationName) {
        GeoPoint point = new GeoPoint(lat, lon);
        map.getController().setCenter(point);

        // Add a marker to show the driver exactly where to go
        Marker marker = new Marker(map);
        marker.setPosition(point);
        marker.setTitle("Customer: " + locationName);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(marker);

        // Update the text label with the address
        ((TextView)findViewById(R.id.txtCustomerInfo)).setText("PickUp: " + locationName);
    }

    /**
     * Shows a quick message on the screen.
     * @param message The text to display.
     */
    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Moves to the "Track Ride" screen once the customer is successfully picked up.
     * @param bookingID The ID used to track the progress of the active trip.
     */
    @Override
    public void navigateToOngoingRide(String bookingID) {
        Intent intent = new Intent(this, TrackRideActivity.class);
        intent.putExtra("bookingID", bookingID);
        startActivity(intent);
    }

    /**
     * Closes the navigation screen and stops the current process.
     */
    @Override
    public void cancel() {
        finish();
    }
}