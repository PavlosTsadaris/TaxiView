package gr.softeng.team19.view.customer.homeactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gr.softeng.team19.R;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.view.customer.bookride.destination.ChooseDestinationActivity;
import gr.softeng.team19.view.customer.evaluation.EvaluationActivity;
import gr.softeng.team19.view.history.HistoryActivity;
import gr.softeng.team19.view.ratings.RatingsActivity;
import gr.softeng.team19.view.rideRequests.RideRequestsActivity;
import gr.softeng.team19.view.viewmyprofile.MyProfileActivity;

/**
 * Main dashboard Activity for the Customer.
 */
public class HomeCustomerActivity extends AppCompatActivity implements HomeCustomerView {
    private HomeCustomerPresenter presenter;
    private TextView txtUserName, txtNotificationBadge;
    private CardView cardCallTaxi, cardProfile, cardHistory, cardPendingEvaluations, cardMyRatings, cardMyRequests;
    private ImageView imgProfileSmall, notificationIcon;
    private View blurScrim;

    /**
     * Initializes the activity, UI, and presenter.
     * @param savedInstanceState Saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_customer);

        initViews();
        setupListeners();

        presenter = new HomeCustomerPresenter(this);
        String username = getIntent().getStringExtra("username");
        presenter.onHomePageLoad(username);

        presenter.checkForPendingEvaluations();
    }

    /**
     * Binds UI components to variables.
     */
    private void initViews() {
        txtUserName = findViewById(R.id.txtUserName);
        cardCallTaxi = findViewById(R.id.cardCallTaxi);
        cardProfile = findViewById(R.id.cardProfile);
        cardHistory = findViewById(R.id.cardHistory);
        imgProfileSmall = findViewById(R.id.imgProfileSmall);
        notificationIcon = findViewById(R.id.imgNotification);
        txtNotificationBadge = findViewById(R.id.txtNotificationBadge);
        cardPendingEvaluations = findViewById(R.id.cardPendingEvaluations);
        blurScrim = findViewById(R.id.viewBlurScrimHome);
        cardMyRatings = findViewById(R.id.cardMyRatings);
        cardMyRequests = findViewById(R.id.cardMyRequests);
    }

    /**
     * Connects UI elements to presenter actions.
     */
    private void setupListeners() {
        cardCallTaxi.setOnClickListener(v -> presenter.onBookRideSelected());
        cardProfile.setOnClickListener(v -> presenter.onProfileSelected());
        imgProfileSmall.setOnClickListener(v -> presenter.onProfileSelected());
        cardHistory.setOnClickListener(v -> presenter.onHistorySelected());
        cardPendingEvaluations.setOnClickListener(v -> presenter.onNotificationBadgeClicked());
        cardMyRatings.setOnClickListener((v) -> presenter.onRatingsSelected());
        cardMyRequests.setOnClickListener((v) -> presenter.onRequestsSelected());

        notificationIcon.setOnClickListener(v -> {
            cardPendingEvaluations.setVisibility(View.VISIBLE);
            blurScrim.setVisibility(View.VISIBLE);
            presenter.onNotificationBadgeClicked();
        });

        findViewById(R.id.btnClosePopup).setOnClickListener(v -> {
            cardPendingEvaluations.setVisibility(View.GONE);
            blurScrim.setVisibility(View.GONE);
        });
    }

    /**
     * Updates the UI notification counter.
     * @param size Number of pending items.
     */
    @Override
    public void showNotificationBadge(int size) {
        CardView container = findViewById(R.id.cardNotificationContainer);
        if (size > 0) {
            container.setVisibility(View.VISIBLE);
            txtNotificationBadge.setVisibility(View.VISIBLE);
            txtNotificationBadge.setText(String.valueOf(size));
        } else {
            container.setVisibility(View.VISIBLE);
            txtNotificationBadge.setVisibility(View.GONE);
        }
    }

    /**
     * Removes the evaluation overlay from the screen.
     */
    @Override
    public void hidePendingList() {
        if (cardPendingEvaluations != null) cardPendingEvaluations.setVisibility(View.GONE);
        if (blurScrim != null) blurScrim.setVisibility(View.GONE);

        RecyclerView recycler = findViewById(R.id.recyclerPendingBookings);
        if (recycler != null) recycler.setAdapter(null);
    }

    /**
     * Prepares the RecyclerView with pending ride evaluations.
     * @param pendingBookings List of bookings to evaluate.
     */
    @Override
    public void setupPendingList(List<TaxiBooking> pendingBookings) {
        RecyclerView recycler = findViewById(R.id.recyclerPendingBookings);
        ReviewAdapter adapter = new ReviewAdapter(pendingBookings, booking -> {
            cardPendingEvaluations.setVisibility(View.GONE);
            blurScrim.setVisibility(View.GONE);
            presenter.onBookingSelectedForReview(booking);
        });
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
    }

    /**
     * Refreshes pending data when activity is visible.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null) presenter.checkForPendingEvaluations();
    }

    /**
     * Updates the dashboard header.
     * @param nameToDisplay Formatted name of the customer.
     */
    @Override
    public void setWelcomeMessage(String nameToDisplay) {
        txtUserName.setText(nameToDisplay);
    }

    /**
     * Launches the ride booking flow.
     * @param username Target user identifier.
     */
    @Override
    public void navigateToBookRide(String username) {
        Intent intent = new Intent(this, ChooseDestinationActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    /**
     * Launches the profile management screen.
     * @param username Target user identifier.
     */
    @Override
    public void navigateToProfile(String username) {
        Intent intent = new Intent(this, MyProfileActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    /**
     * Launches the history screen.
     */
    @Override
    public void navigateToHistory() {
        Intent intent = new Intent(this, HistoryActivity.class);
        intent.putExtra("username", getIntent().getStringExtra("username"));
        startActivity(intent);
    }

    /**
     * Launches the ratings screen.
     */
    @Override
    public void navigateToRatings() {
        Intent intent = new Intent(this, RatingsActivity.class);
        intent.putExtra("username", getIntent().getStringExtra("username"));
        startActivity(intent);
    }

    /**
     * Launches the ride requests screen.
     */
    @Override
    public void navigateToRequests() {
        Intent intent = new Intent(this, RideRequestsActivity.class);
        intent.putExtra("username", getIntent().getStringExtra("username"));
        startActivity(intent);
    }

    /**
     * Launches the evaluation screen for a specific ride.
     * @param booking Booking to review.
     */
    @Override
    public void navigateToDriverEvaluation(TaxiBooking booking) {
        Intent intent = new Intent(this, EvaluationActivity.class);
        intent.putExtra("bookingID", booking.getBookingID());
        startActivity(intent);
    }

    /**
     * Displays an alert when no reviews are found.
     */
    @Override
    public void showNoReviewsMessage() {
        Toast.makeText(this, "No Pending Reviews!", Toast.LENGTH_SHORT).show();
    }
}