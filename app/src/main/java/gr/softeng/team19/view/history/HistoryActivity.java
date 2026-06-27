package gr.softeng.team19.view.history;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gr.softeng.team19.R;
import gr.softeng.team19.domain.TaxiBooking;

/**
 * Activity class that displays the list of all past taxi rides.
 * It shows the details of each trip and the total amount of money earned or spent.
 */
public class HistoryActivity extends AppCompatActivity implements HistoryView {

    private RecyclerView recyclerHistory;
    private LinearLayout layoutEmptyState;
    private ImageView btnBack;

    private HistoryPresenter presenter;
    private HistoryAdapter adapter;
    private TextView txtTotalAmount;

    /**
     * Sets up the screen, finds the UI components, and connects the presenter.
     * @param savedInstanceState A bundle containing data from a previous session, if it exists.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force light mode theme for consistency
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_history);

        // Find UI elements by their ID
        recyclerHistory = findViewById(R.id.recyclerHistory);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        btnBack = findViewById(R.id.btnBack);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);

        // Set how the list items should be positioned (vertically)
        recyclerHistory.setLayoutManager(new LinearLayoutManager(this));

        // Get the username from the previous screen and start the presenter
        String username = getIntent().getStringExtra("username");
        presenter = new HistoryPresenter(this, username);

        // Tell the presenter to go back when the back arrow is clicked
        btnBack.setOnClickListener(v -> presenter.goBack());
    }

    /**
     * Displays the list of rides on the screen and makes the list visible.
     * @param bookings A list of TaxiBooking objects containing all past trip data.
     */
    @Override
    public void showHistoryList(List<TaxiBooking> bookings) {
        layoutEmptyState.setVisibility(View.GONE);
        recyclerHistory.setVisibility(View.VISIBLE);

        // Create the adapter and connect it to the list
        adapter = new HistoryAdapter(bookings);
        recyclerHistory.setAdapter(adapter);
    }

    /**
     * Shows a special layout when the user has no past rides to display.
     */
    @Override
    public void showEmptyState() {
        recyclerHistory.setVisibility(View.GONE);
        layoutEmptyState.setVisibility(View.VISIBLE);
    }

    /**
     * Shows a short message at the bottom of the screen.
     * @param message The text content that the user will see.
     */
    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Shows the final sum of money formatted with the Euro symbol.
     * @param total The total numeric value to show on the screen.
     */
    @Override
    public void showTotalAmount(double total) {
        txtTotalAmount.setText(String.format(java.util.Locale.getDefault(), " %.2f€", total));
    }

    /**
     * Closes the current screen to go back to the previous one.
     */
    @Override
    public void navigateBack() {
        finish();
    }
}