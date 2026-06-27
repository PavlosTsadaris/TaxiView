package gr.softeng.team19.view.driver.chooselocation;

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

/**
 * Screen that lets a driver pick their starting location on a map.
 * Once a location is chosen, the driver can go "online" to start receiving ride requests.
 */
public class DriverChooseLocationActivity extends AppCompatActivity implements DriverChooseLocationView {

    private DriverChooseLocationPresenter presenter;
    private TextView txtDriverStatus, txtCurrentLocation;
    private MaterialButton btnGoOnline, btnCancel;
    private MapView map;
    private ImageView btnEditLocation;
    private Marker startMarker;
    private String driverUsername;

    private MockRideData.DemoLocation selectedLocation;

    /**
     * Initializes the screen, sets up the map, and gets the driver's username.
     * @param savedInstanceState Saved data from a previous session.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Required setup for OpenStreetMap (OSMDroid)
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        driverUsername = getIntent().getStringExtra("username");
        setContentView(R.layout.activity_choose_location);

        // Link UI components
        txtDriverStatus = findViewById(R.id.txtDriverStatus);
        txtCurrentLocation = findViewById(R.id.txtCurrentLocation);
        btnGoOnline = findViewById(R.id.btnGoOnline);
        btnCancel = findViewById(R.id.btnCancel);
        map = findViewById(R.id.mapDriver);
        btnEditLocation = findViewById(R.id.btnEditLocation);



        setupMap();
        presenter = new DriverChooseLocationPresenter(this);


        // Button actions
        btnEditLocation.setOnClickListener(v -> presenter.showLocationSelectionDialog());
        btnGoOnline.setOnClickListener(v -> presenter.onGoOnline(driverUsername, selectedLocation));
        btnCancel.setOnClickListener(v -> presenter.onCancel());
    }

    /**
     * Called when a driver selects a new location from the list.
     * @param newLocation The location object picked by the driver.
     */
    public void onLocationSelected(MockRideData.DemoLocation newLocation) {
        this.selectedLocation = newLocation;
        txtCurrentLocation.setText(newLocation.name);

    }

    /**
     * Configures map settings like zoom level and initial focus on Athens.
     */
    private void setupMap() {
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        GeoPoint athens = new GeoPoint(37.9838, 23.7275);
        map.getController().setZoom(12.0);
        map.getController().setCenter(athens);

        startMarker = new Marker(map);
    }

    /**
     * Shows a popup list of available addresses for the driver to choose from.
     * @param locationNames Array of address names.
     */
    @Override
    public void showLocationDialog(String[] locationNames) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Select Location")
                .setItems(locationNames, (dialog, which) -> {
                    presenter.onChooseLocation(which);
                    btnGoOnline.setEnabled(true);
                })
                .show();
    }

    /**
     * Enables or disables the "Go Online" button.
     * @param isEnabled True to make it clickable.
     */
    @Override
    public void setButtonEnabled(boolean isEnabled) {
        btnGoOnline.setEnabled(isEnabled);
    }

    /**
     * Changes the text on the screen to show the selected address.
     * @param address The name of the location.
     */
    @Override
    public void updateLocationText(String address) {
        txtCurrentLocation.setText(address);
        txtCurrentLocation.setTextColor(android.graphics.Color.parseColor("#1B664D"));
    }

    /**
     * Moves the map camera to the selected spot and places a taxi icon marker.
     * @param location The coordinates and name for the marker.
     */
    @Override
    public void updateMapLocation(MockRideData.DemoLocation location) {
        if (!map.getOverlays().contains(startMarker)) {
            map.getOverlays().add(startMarker);
        }

        // Animate the camera and set the pin
        map.getController().animateTo(location.point);
        map.getController().setZoom(16.5);

        startMarker.setPosition(location.point);
        startMarker.setTitle(location.name);
        startMarker.setIcon(getResources().getDrawable(R.drawable.baseline_local_taxi_24));

        map.invalidate(); // Refresh map graphics
        startMarker.showInfoWindow();
    }

    /**
     * Finishes this screen once the driver is successfully online.
     * @param locationName The final location chosen.
     */
    @Override
    public void navigateToOnlineMode(String locationName) {
        Toast.makeText(this, "You are now Online at: " + locationName, Toast.LENGTH_LONG).show();
        finish();
    }

    /**
     * Closes the screen and returns to the previous menu.
     */
    @Override
    public void navigateBack() {
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume(); // Needed for the map to work after returning to app
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause(); // Pause map to save battery
    }
}