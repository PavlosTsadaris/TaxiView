package gr.softeng.team19.view.customer.bookride.destination;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import gr.softeng.team19.R;
import gr.softeng.team19.memorydao.MockRideData;
import gr.softeng.team19.view.customer.bookride.searchride.BookRideActivity;

/**
 * Screen where customers pick where they want to go.
 * It shows a map and a list of locations to choose from.
 */
public class ChooseDestinationActivity extends AppCompatActivity implements ChooseDestinationView {

    private ChooseDestinationPresenter presenter;

    private TextView txtStatus, txtAddress;
    private MaterialButton btnAction, btnCancelBook;
    private MapView map;
    private ImageView btnChangeLocation;
    private Marker destMarker;

    /**
     * Sets up the screen, links UI buttons, and prepares the map.
     * @param savedInstanceState Data from a previous session, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Required setup for OpenStreetMap
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        setContentView(R.layout.activity_choose_destination);

        txtStatus = findViewById(R.id.txtStatus);
        txtAddress = findViewById(R.id.txtAddress);
        btnAction = findViewById(R.id.btnAction);
        btnCancelBook = findViewById(R.id.btnCancelBookRide);
        map = findViewById(R.id.map);
        btnChangeLocation = findViewById(R.id.btnChangeLocation);

        setupMap();

        presenter = new ChooseDestinationPresenter(this);

        // Button actions
        btnChangeLocation.setOnClickListener(v -> presenter.showLocationSelectionDialog());
        btnAction.setOnClickListener(v -> presenter.onConfirmDestination(txtAddress.getText().toString()));
        btnCancelBook.setOnClickListener(v -> presenter.onCancelBooking());
    }

    /**
     * Sets the map zoom level and places the destination pin.
     */
    private void setupMap() {
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        // Set initial map center
        GeoPoint startPoint = MockRideData.LOCATIONS.get(0).point;
        map.getController().setZoom(15.0);
        map.getController().setCenter(startPoint);

        // Setup the marker icon
        destMarker = new Marker(map);
        destMarker.setPosition(startPoint);
        destMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        destMarker.setTitle("Destination");
        destMarker.setIcon(getResources().getDrawable(R.drawable.ic_nav_pointer));

        map.getOverlays().add(destMarker);
    }

    /**
     * Shows a popup list of addresses for the customer to pick from.
     * @param locationNames Array of location names.
     */
    @Override
    public void showLocationDialog(String[] locationNames) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Select Destination")
                .setItems(locationNames, (dialog, which) -> {
                    presenter.onChooseDestination(which);
                    btnAction.setEnabled(true);
                })
                .show();
    }

    /**
     * Enables or disables the "Confirm" button.
     * @param isEnabled True if the customer can click it.
     */
    @Override
    public void setButtonEnabled(boolean isEnabled) {
        btnAction.setEnabled(isEnabled);
    }

    /**
     * Changes the address text shown on the screen.
     * @param address The name of the location.
     */
    @Override
    public void updateAddressText(String address) {
        txtAddress.setText(address);
    }

    /**
     * Moves the map camera and the pin to a new location.
     * @param location The coordinates and name of the destination.
     */
    @Override
    public void updateMapLocation(MockRideData.DemoLocation location) {
        map.getController().animateTo(location.point);
        destMarker.setPosition(location.point);
        destMarker.setTitle(location.name);
        map.invalidate(); // Refresh the map graphics
    }


    /**
     * Opens the next screen to search for a taxi driver.
     * @param destinationName The final destination picked by the user.
     */
    @Override
    public void navigateToDriverSelection(String destinationName) {
        Intent intent = new Intent(this, BookRideActivity.class);
        intent.putExtra("destination", destinationName);
        intent.putExtra("username", getIntent().getStringExtra("username"));
        startActivity(intent);
    }

    /**
     * Closes this screen and returns to the home dashboard.
     */
    @Override
    public void navigateBackToHome() {
        finish();
    }

    /**
     * Resumes the map components when the user returns to the app.
     */
    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    /**
     * Pauses the map to save battery and memory when the app is in the background.
     */
    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }
}