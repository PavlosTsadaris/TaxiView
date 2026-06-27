package gr.softeng.team19.view.customer.bookride.trackdriver;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import gr.softeng.team19.R;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.view.customer.evaluation.EvaluationActivity;
import gr.softeng.team19.view.customer.homeactivity.HomeCustomerActivity;
import gr.softeng.team19.view.customer.payment.PaymentActivity;

/**
 * Activity for tracking the driver's location and ride progress on a map.
 */
public class TrackPickUpActivity extends AppCompatActivity implements TrackPickUpView {

    private TrackPickUpPresenter presenter;
    private MapView map;
    private TextView txtStatus, txtETA, txtDriverName, txtCarInfo, txtEvaluation;
    private MaterialButton btnCallDriver, btnCancelRide, btnSkip;
    private Marker pickupMarker, driverMarker, destinationMarker;
    private ImageView notificationIcon;
    private androidx.cardview.widget.CardView cardReviewPrompt;

    /**
     * Initializes OSM settings, UI components, and the presenter.
     * @param savedInstanceState Saved activity state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        setContentView(R.layout.activity_track_pick_up_activity);

        initViews();
        setupMap();

        String bookingId = getIntent().getStringExtra("bookingID");
        presenter = new TrackPickUpPresenter(this, bookingId);
        presenter.startTracking();

        btnCallDriver.setOnClickListener(v -> presenter.onCallDriver());
        btnCancelRide.setOnClickListener(v -> presenter.onCancelRide());
        btnSkip.setOnClickListener(v -> presenter.onSkip());

        notificationIcon = findViewById(R.id.imgNotification);
        notificationIcon.setOnClickListener(v -> presenter.onNotificationIcon());
    }

    /**
     * Binds UI elements to class variables.
     */
    private void initViews() {
        map = findViewById(R.id.map);
        txtStatus = findViewById(R.id.txtStatus);
        txtETA = findViewById(R.id.txtETA);
        txtDriverName = findViewById(R.id.txtDriverName);
        txtCarInfo = findViewById(R.id.txtCarInfo);
        txtEvaluation = findViewById(R.id.txtEvaluation);
        btnCallDriver = findViewById(R.id.btnCallDriver);
        btnCancelRide = findViewById(R.id.btnCancelRide);
        btnSkip = findViewById(R.id.btnSkip);
    }

    /**
     * Sets up the MapView properties and initial center point.
     */
    private void setupMap() {
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.getController().setZoom(17.0);

        GeoPoint startPoint = new GeoPoint(37.9755, 23.7348);
        map.getController().setCenter(startPoint);
    }

    /**
     * Creates and adds markers for driver, customer, and destination on the map.
     * @param driverPoint Coordinates of the driver.
     * @param customerPoint Coordinates of the customer.
     * @param destinationPoint Coordinates of the destination.
     */
    @Override
    public void setupDriverAndCustomerMarkers(GeoPoint driverPoint, GeoPoint customerPoint, GeoPoint destinationPoint) {
        pickupMarker = new Marker(map);
        pickupMarker.setPosition(customerPoint);
        pickupMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        pickupMarker.setTitle("Pick-up Point");
        map.getOverlays().add(pickupMarker);
        map.getController().setCenter(customerPoint);

        driverMarker = new Marker(map);
        driverMarker.setPosition(driverPoint);
        driverMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        driverMarker.setTitle("Your Driver");

        try {
            driverMarker.setIcon(ContextCompat.getDrawable(this, R.drawable.baseline_local_taxi_24));
        } catch (Exception e) { }

        destinationMarker = new Marker(map);
        GeoPoint destMarker = new GeoPoint(destinationPoint.getLatitude() + 0.00031, destinationPoint.getLongitude());
        destinationMarker.setPosition(destMarker);
        destinationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        destinationMarker.setTitle("Your Destination");

        try {
            destinationMarker.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_nav_pointer));
        } catch (Exception e) { }

        map.getOverlays().add(driverMarker);
        map.getOverlays().add(destinationMarker);
    }

    /**
     * Displays driver and vehicle details in the UI.
     * @param name Driver's name.
     * @param carModel Model of the car.
     * @param plate License plate number.
     * @param rating Numerical rating of the driver.
     */
    @Override
    public void setDriverInfo(String name, String carModel, String plate, double rating) {
        txtDriverName.setText(name);
        txtCarInfo.setText(carModel + " • " + plate);
        txtEvaluation.setText(String.valueOf(rating));
    }

    @Override
    public void setETA(String text) { txtETA.setText(text); }

    @Override
    public void setStatus(String status) { txtStatus.setText(status); }

    /**
     * Updates the driver marker position dynamically.
     * @param lat New latitude.
     * @param lon New longitude.
     */
    @Override
    public void updateDriverLocationOnMap(double lat, double lon) {
        if (driverMarker != null) {
            GeoPoint newPos = new GeoPoint(lat, lon);
            driverMarker.setPosition(newPos);
            map.invalidate();
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Returns the user to the home screen.
     * @param username The current user's identifier.
     */
    @Override
    public void navigateToHome(String username) {
        Intent intent = new Intent(this, HomeCustomerActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) presenter.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    /**
     * Shows the prompt for driver evaluation.
     * @param booking The associated booking.
     */
    @Override
    public void showReviewPrompt(TaxiBooking booking) {
        View blurScrim = findViewById(R.id.viewBlurScrim);
        cardReviewPrompt = findViewById(R.id.cardReviewPrompt);

        blurScrim.setVisibility(View.VISIBLE);
        cardReviewPrompt.setVisibility(View.VISIBLE);
        cardReviewPrompt.bringToFront();

        findViewById(R.id.btnReviewNow).setOnClickListener(v -> {
            blurScrim.setVisibility(View.GONE);
            presenter.onReviewNow(booking);
        });

        findViewById(R.id.btnReviewLater).setOnClickListener(v -> {
            cardReviewPrompt.setVisibility(View.GONE);
            blurScrim.setVisibility(View.GONE);
            presenter.onReviewLater(booking);
        });
    }

    @Override
    public void deleteReviewPrompt() {
        notificationIcon.setVisibility(View.GONE);
        cardReviewPrompt.setVisibility(View.GONE);
    }

    /**
     * Navigates to the evaluation screen.
     * @param booking The booking to review.
     */
    @Override
    public void navigateToDriverEvaluation(TaxiBooking booking) {
        Intent intent = new Intent(this, EvaluationActivity.class);
        intent.putExtra("bookingID", booking.getBookingID());
        startActivity(intent);
    }

    @Override
    public void setCallButtonOff() { btnCallDriver.setVisibility(View.GONE); }

    @Override
    public void setCancelButtonOff() { btnCancelRide.setVisibility(View.GONE); }

    /**
     * Navigates to the payment screen.
     * @param amount The total cost.
     * @param booking The current booking.
     */
    @Override
    public void navigateToPayment(double amount, TaxiBooking booking) {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("bookingID", booking.getBookingID());
        intent.putExtra("amount", amount);
        startActivity(intent);
        finish();
    }

    @Override
    public void showSkip() {
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            findViewById(R.id.btnSkip).setVisibility(View.VISIBLE);
        }, 2000);
    }

    @Override
    public void hideSkip() { findViewById(R.id.btnSkip).setVisibility(View.GONE); }
}