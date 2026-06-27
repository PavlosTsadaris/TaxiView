package gr.softeng.team19.view.customer.bookride.searchride;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

import gr.softeng.team19.R;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.domain.TaxiDriver;
import gr.softeng.team19.memorydao.MockRideData;
import gr.softeng.team19.view.customer.bookride.trackdriver.TrackPickUpActivity;
import gr.softeng.team19.view.customer.evaluation.EvaluationActivity;
import gr.softeng.team19.view.customer.homeactivity.HomeCustomerActivity;

/**
 * Screen that handles the taxi booking process.
 * It manages confirming the pickup point, showing nearby drivers on the map,
 * and handling the request-response flow when a driver is selected.
 */
public class BookRideActivity extends AppCompatActivity implements BookRideView {

    private BookRidePresenter presenter;

    // UI Elements
    private TextView txtStatus, txtAddress;
    private MaterialButton btnAction;
    private MapView map;
    private ImageView btnChangeLocation;
    private Marker startMarker;
    private RecyclerView recyclerDrivers;
    private MaterialButton btnCancel;
    private MaterialButton btnChangeDestination;
    private androidx.cardview.widget.CardView cardWaitingResponse;
    private android.widget.TextView txtWaitingResponse;
    private ProgressBar progressBarWaiting;
    private ImageView imgSuccessCheck;
    private ImageView imgCancelCheck;
    private androidx.cardview.widget.CardView cardReviewPrompt;

    /**
     * Sets up the activity, configures the map, and initializes all UI components.
     * @param savedInstanceState Data from a previous session, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        setContentView(R.layout.activity_book_ride);

        // Link UI elements
        txtStatus = findViewById(R.id.txtStatus);
        txtAddress = findViewById(R.id.txtAddress);
        btnAction = findViewById(R.id.btnAction);
        map = findViewById(R.id.map);
        btnCancel = findViewById(R.id.btnCancelBookRide);
        btnChangeDestination = findViewById(R.id.btnChangeDestination);
        cardWaitingResponse = findViewById(R.id.cardWaitingResponse);
        txtWaitingResponse = findViewById(R.id.txtWaitingResponse);
        imgSuccessCheck = findViewById(R.id.imgSuccessCheck);
        imgCancelCheck = findViewById(R.id.imgCancelCheck);
        btnChangeLocation = findViewById(R.id.btnChangeLocation);
        progressBarWaiting = findViewById(R.id.progressBarWaiting);

        setupMap();

        presenter = new BookRidePresenter(this);
        presenter.startLocationSearch(getIntent().getStringExtra("destination"));

        // Listeners for picking a location and confirming
        String destination = getIntent().getStringExtra("destination");
        btnChangeLocation.setOnClickListener(v -> presenter.showLocationSelectionDialog(destination));
        btnAction.setOnClickListener(v -> presenter.onActionButtonClicked(getIntent().getStringExtra("username")));

        recyclerDrivers = findViewById(R.id.recyclerDrivers);
        recyclerDrivers.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));

        btnCancel.setOnClickListener(v -> presenter.onCancelBookRide());
        btnChangeDestination.setOnClickListener(v -> presenter.onChangeDestination());
    }

    /**
     * Sets map zoom and adds the pickup pin at a default starting point.
     */
    private void setupMap() {
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        GeoPoint startPoint = MockRideData.LOCATIONS.get(0).point;
        map.getController().setZoom(18.0);
        map.getController().setCenter(startPoint);

        startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setTitle("Pick-up Point");
        map.getOverlays().add(startMarker);
    }

    /**
     * Resets the screen to the initial address-confirmation state.
     * @param lastAddress The address to show in the text view.
     */
    @Override
    public void resetUI(String lastAddress) {
        recyclerDrivers.setVisibility(View.GONE);
        txtStatus.setVisibility(View.VISIBLE);
        btnAction.setVisibility(View.VISIBLE);
        btnChangeDestination.setVisibility(View.VISIBLE);
        btnChangeLocation.setVisibility(View.VISIBLE);

        txtStatus.setText(getString(R.string.status_confirm));
        btnAction.setText(getString(R.string.btn_confirm_location));
        txtAddress.setText(lastAddress);
        btnAction.setEnabled(true);
    }

    /**
     * Shows a popup list for selecting a different pickup address.
     * @param locationNames Array of location names to display.
     */
    @Override
    public void showLocationSelectionDialog(String[] locationNames) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Select Pick-up Location")
                .setItems(locationNames, (dialog, which) -> {
                    presenter.onLocationSelected(which, map, startMarker, getIntent().getStringExtra("username"));
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    /**
     * Updates the main status text.
     */
    @Override
    public void setStatusText(int resId) {
        txtStatus.setText(getString(resId));
    }

    /**
     * Updates the address label.
     */
    @Override
    public void setAddressText(String address) {
        txtAddress.setText(address);
    }

    /**
     * Updates the main action button text.
     */
    @Override
    public void setButtonText(int resId) {
        btnAction.setText(getString(resId));
    }

    /**
     * Enables or disables the primary confirm button.
     */
    @Override
    public void setButtonEnabled(boolean isEnabled) {
        btnAction.setEnabled(isEnabled);
    }

    /**
     * Locks or unlocks the driver list interactions.
     */
    @Override
    public void setRecycleListEnabled(boolean isEnabled) {
        recyclerDrivers.setEnabled(isEnabled);
        recyclerDrivers.suppressLayout(!isEnabled);
    }

    /**
     * Shows the list of nearby drivers and places their icons on the map.
     * @param drivers List of available taxi drivers.
     */
    @Override
    public void showDriverList(ArrayList<TaxiDriver> drivers) {
        if (drivers == null || drivers.isEmpty()) {
            handleNoDriversFound();
            return;
        }

        txtAddress.setText(getString(R.string.msg_drivers_nearby) + " (" + drivers.size() + ")");
        hideInitialUIElements();
        recyclerDrivers.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);

        map.getOverlays().clear();
        if (startMarker != null) map.getOverlays().add(startMarker);

        // Add taxi markers to the map
        for (TaxiDriver driver : drivers) {
            GeoPoint driverPoint = new GeoPoint(driver.getUserLocation().getLatitude(), driver.getUserLocation().getLongitude());
            Marker driverMarker = new Marker(map);
            driverMarker.setPosition(driverPoint);
            driverMarker.setTitle(driver.getName() + " " + driver.getSurname());
            driverMarker.setIcon(getResources().getDrawable(R.drawable.baseline_local_taxi_24));
            map.getOverlays().add(driverMarker);
        }
        map.invalidate();

        GeoPoint mapPoint = startMarker.getPosition();
        gr.softeng.team19.domain.GPSLocation currentPickup =
                new gr.softeng.team19.domain.GPSLocation(mapPoint.getLatitude(), mapPoint.getLongitude());

        recyclerDrivers.setAdapter(new DriverAdapter(drivers, selectedDriver -> presenter.onChooseDriver(selectedDriver), currentPickup));
    }

    /**
     * Internal helper to handle the UI when no drivers are nearby.
     */
    private void handleNoDriversFound() {
        int errorColor = androidx.core.content.ContextCompat.getColor(this, R.color.red_error);
        txtWaitingResponse.setText("No Drivers Found!\nPlease change your ride details!");
        txtWaitingResponse.setTextColor(errorColor);
        imgCancelCheck.setImageTintList(android.content.res.ColorStateList.valueOf(errorColor));
        progressBarWaiting.setVisibility(View.GONE);
        imgCancelCheck.setVisibility(View.VISIBLE);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            hideWaitingOverlay();
            navigateToDestinationScreen();
        }, 2000);
    }

    /**
     * Hides initial confirm buttons to make room for the driver list.
     */
    private void hideInitialUIElements() {
        txtStatus.setVisibility(View.GONE);
        btnAction.setVisibility(View.GONE);
        btnChangeDestination.setVisibility(View.GONE);
        btnChangeLocation.setVisibility(View.GONE);
    }

    /**
     * Closes the driver list and returns the UI to the address confirmation state.
     */
    @Override
    public void closeDriverList() {
        recyclerDrivers.setVisibility(View.GONE);
        txtStatus.setVisibility(View.VISIBLE);
        btnAction.setVisibility(View.VISIBLE);
        btnChangeDestination.setVisibility(View.VISIBLE);
        btnChangeLocation.setVisibility(View.VISIBLE);

        map.getOverlays().clear();
        if (startMarker != null) map.getOverlays().add(startMarker);
        map.invalidate();

        txtStatus.setText(getString(R.string.status_confirm));
        btnAction.setText(getString(R.string.btn_confirm_location));
        btnAction.setEnabled(true);
    }

    /**
     * Restarts map resources.
     */
    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    /**
     * Pauses map resources to save battery.
     */
    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    /**
     * Closes this screen and returns to destination selection.
     */
    @Override
    public void navigateToDestinationScreen() {
        finish();
    }

    /**
     * Moves the user back to the home dashboard.
     */
    @Override
    public void navigateToHomeScreen() {
        Intent intent = new Intent(this, HomeCustomerActivity.class);
        intent.putExtra("username", getIntent().getStringExtra("username"));
        startActivity(intent);
        finish();
    }

    /**
     * Moves to the pickup tracking screen once a booking is confirmed.
     */
    @Override
    public void navigateToRideTracking(TaxiBooking booking) {
        Intent intent = new Intent(this, TrackPickUpActivity.class);
        intent.putExtra("bookingID", booking.getBookingID());
        startActivity(intent);
        finish();
    }

    /**
     * Opens the screen for the customer to rate the driver.
     */
    @Override
    public void navigateToDriverEvaluation(TaxiBooking booking) {
        Intent intent = new Intent(this, EvaluationActivity.class);
        intent.putExtra("bookingID", booking.getBookingID());
        intent.putExtra("open", true);
        startActivity(intent);
    }

    /**
     * Shows a toast message about the picked destination.
     */
    @Override
    public void showRemoveSelectedDestination(String destination) {
        Toast.makeText(this, "Selected Destination: " + destination, Toast.LENGTH_SHORT).show();
    }

    /**
     * Shows a popup overlay while waiting for the driver to respond.
     */
    @Override
    public void showWaitingOverlay(String driverName) {
        txtWaitingResponse.setText("You chose " + driverName + ".\nWaiting for response...");
        cardWaitingResponse.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the waiting overlay.
     */
    @Override
    public void hideWaitingOverlay() {
        cardWaitingResponse.setVisibility(View.GONE);
    }

    /**
     * Toast feedback for a canceled selection.
     */
    @Override
    public void showCanceledDriver(String name) {
        Toast.makeText(this, "Canceled : " + name, Toast.LENGTH_SHORT).show();
    }

    /**
     * Toast feedback for a successful driver choice.
     */
    @Override
    public void showSelectedDriver(String name) {
        Toast.makeText(this, "Selected : " + name, Toast.LENGTH_SHORT).show();
    }

    /**
     * Updates status text when a driver rejects the ride.
     */
    @Override
    public void showDriverRejectedMessage(String name) {
        txtWaitingResponse.setText("Driver " + name + " rejected your request.");
    }

    /**
     * Updates status text when a driver accepts the ride.
     */
    @Override
    public void showDriverAcceptedMessage(String name) {
        txtWaitingResponse.setText("Driver " + name + " accepted your request.");
    }

    /**
     * Shows a green checkmark when the process is successful.
     */
    @Override
    public void showSuccessState() {
        progressBarWaiting.setVisibility(View.GONE);
        imgSuccessCheck.setVisibility(View.VISIBLE);
    }

    /**
     * Shows a red error icon when the driver rejects or something goes wrong.
     */
    @Override
    public void showCancelState() {
        progressBarWaiting.setVisibility(View.GONE);
        imgCancelCheck.setVisibility(View.VISIBLE);
    }

    /**
     * Resets the waiting overlay to the default search state.
     */
    @Override
    public void hideCancelState() {
        progressBarWaiting.setVisibility(View.VISIBLE);
        imgCancelCheck.setVisibility(View.GONE);
        txtWaitingResponse.setText("Choose a different driver!");
    }

    /**
     * Displays a prompt asking the user to rate the completed ride.
     * Includes a background blur effect.
     * @param booking The completed booking data.
     */
    @Override
    public void showReviewPrompt(TaxiBooking booking) {
        View blurScrim = findViewById(R.id.viewBlurScrim);
        blurScrim.setVisibility(View.VISIBLE);
        cardReviewPrompt = findViewById(R.id.cardReviewPrompt);
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

    /**
     * Removes the review prompt window.
     */
    @Override
    public void deleteReviewPrompt() {
        if (cardReviewPrompt != null) cardReviewPrompt.setVisibility(View.GONE);
    }
}