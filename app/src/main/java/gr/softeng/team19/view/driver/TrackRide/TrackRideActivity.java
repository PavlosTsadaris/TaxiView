package gr.softeng.team19.view.driver.TrackRide;

import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import gr.softeng.team19.R;
import gr.softeng.team19.view.driver.payment.DriverPaymentActivity;

/**
 * Screen that shows the ride in progress.
 * It tracks the driver's movement on the map, shows the route to the pickup point,
 * and then the route to the final destination.
 */
public class TrackRideActivity extends AppCompatActivity implements TrackRideView {

    private TrackRidePresenter presenter;
    private MapView map;
    private TextView txtRideStatus, txtRideInfo, txtETA;
    private MaterialButton btnArrivedAtPickup, btnEndRide, btnSkipRide;
    private Marker pickupMarker, driverMarker, destinationMarker;

    /**
     * Sets up the tracking screen, map settings, and UI buttons.
     * @param savedInstanceState Data from a previous session, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force light mode for map clarity
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Initialize map configuration
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        setContentView(R.layout.activity_track_ride);

        initViews();

        presenter = new TrackRidePresenter(this, getIntent().getStringExtra("bookingID"));

        // Start by moving the driver toward the customer's pickup point
        presenter.goToCustomer();

        // Button click listeners
        btnArrivedAtPickup.setOnClickListener(v -> presenter.onArrivedAtPickup());
        btnEndRide.setOnClickListener(v -> presenter.onEndRide());
        btnSkipRide.setOnClickListener(v -> presenter.onSkipRide());
    }

    /**
     * Finds and prepares all text views and buttons on the screen.
     */
    private void initViews() {
        map = findViewById(R.id.mapTrackRide);
        txtRideStatus = findViewById(R.id.txtRideStatus);
        txtRideInfo = findViewById(R.id.txtRideInfo);
        txtETA = findViewById(R.id.txtETA);

        btnArrivedAtPickup = findViewById(R.id.btnArrivedAtPickup);
        btnEndRide = findViewById(R.id.btnEndRide);
        btnSkipRide = findViewById(R.id.btnSkip);

        // Default state: Heading to pick up the customer
        btnEndRide.setVisibility(View.GONE);
        txtRideStatus.setText("Heading to Customer");
    }

    /**
     * Sets up the map markers for the taxi, the customer, and the destination.
     * @param driverPoint      The current location of the taxi.
     * @param customerPoint    The spot where the customer is waiting.
     * @param destinationPoint The final drop-off address.
     */
    @Override
    public void setupMap(GeoPoint driverPoint, GeoPoint customerPoint, GeoPoint destinationPoint) {
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.getController().setZoom(18.0);

        // Add the customer's pickup marker
        pickupMarker = new Marker(map);
        pickupMarker.setPosition(customerPoint);
        pickupMarker.setTitle("Pick-up Point");
        map.getOverlays().add(pickupMarker);

        // Add the taxi marker with a specific car icon
        driverMarker = new Marker(map);
        driverMarker.setPosition(driverPoint);
        driverMarker.setTitle("Your Position");
        driverMarker.setIcon(ContextCompat.getDrawable(this, R.drawable.baseline_local_taxi_24));

        // Add the destination marker
        destinationMarker = new Marker(map);
        destinationMarker.setPosition(destinationPoint);
        destinationMarker.setTitle("Destination");
        destinationMarker.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_nav_pointer));

        map.getOverlays().add(driverMarker);
        map.getOverlays().add(destinationMarker);

        map.getController().setCenter(driverPoint);
    }

    /**
     * Updates the status labels to show if the driver is going to a pickup or a drop-off.
     * @param pickup      True if heading to pickup, false if heading to destination.
     * @param destination The name of the target location.
     */
    @Override
    public void setTxtRideInfo(boolean pickup, String destination) {
        if (pickup) {
            txtRideInfo.setText("Pickup: " + destination);
        } else {
            txtRideInfo.setText("Destination: " + destination);
            txtRideStatus.setText("Heading to " + destination);
        }
    }

    /**
     * Moves the car icon on the map as the driver moves.
     * @param lat Current latitude.
     * @param lon Current longitude.
     */
    @Override
    public void updateDriverLocationOnMap(double lat, double lon) {
        if (map != null && driverMarker != null) {
            GeoPoint newPos = new GeoPoint(lat, lon);
            driverMarker.setPosition(newPos);
            map.getController().setCenter(newPos); // Keep the camera on the car
            map.invalidate(); // Refresh map graphics
        }
    }

    /**
     * Shows a quick toast message.
     */
    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Sends the driver to the payment screen once the ride is over.
     * @param amount    The final fare to charge.
     * @param bookingID The ID of the completed trip.
     */
    @Override
    public void navigateToPayment(Double amount, String bookingID) {
        Intent intent = new Intent(this, DriverPaymentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("amount", amount);
        intent.putExtra("bookingID", bookingID);
        startActivity(intent);
        finish();
    }

    /**
     * Shows or hides the "I have Arrived" button.
     */
    @Override
    public void setButtonArrival(boolean b) {
        btnArrivedAtPickup.setVisibility(b ? VISIBLE : View.GONE);
    }

    /**
     * Shows or hides the "Finish Ride" button.
     */
    @Override
    public void setButtonEndRide(boolean b) {
        btnEndRide.setVisibility(b ? VISIBLE : View.GONE);
    }

    /**
     * Updates the time left until the taxi reaches the target.
     * @param b   True to show the timer.
     * @param ETA The time in minutes (e.g., "5").
     */
    @Override
    public void setTextETA(boolean b, String ETA) {
        if (b) {
            txtETA.setVisibility(VISIBLE);
            txtETA.setText(ETA.equals("Arriving now...") ? ETA : "Arriving in " + ETA + " min");
        } else {
            txtETA.setVisibility(View.GONE);
        }
    }

    /**
     * Displays the skip button after a short delay.
     * This is used for demo purposes to quickly finish a ride simulation.
     */
    @Override
    public void showSkip() {
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            btnSkipRide.setVisibility(View.VISIBLE);
        }, 2000);
    }

    /**
     * Hides the skip button from the screen.
     */
    @Override
    public void hideSkip() {
        btnSkipRide.setVisibility(View.GONE);
    }

    /**
     * Resumes the map components when the user returns to the app.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (map != null) map.onResume();
    }

    /**
     * Pauses the map to save battery and memory when the app is in the background.
     */
    @Override
    public void onPause() {
        super.onPause();
        if (map != null) map.onPause();
    }
}