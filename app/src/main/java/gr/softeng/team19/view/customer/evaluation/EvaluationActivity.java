package gr.softeng.team19.view.customer.evaluation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import gr.softeng.team19.R;
import gr.softeng.team19.view.customer.bookride.trackdriver.TrackPickUpActivity;

/**
 * Activity for the Driver Evaluation screen.
 */
public class EvaluationActivity extends AppCompatActivity implements EvaluationView {
    private EvaluationPresenter presenter;
    private TextView tvDriverName, tvCarInfo, tvCustomerName, tvBookingDetails;
    private RatingBar rbBehavior, rbVehicle, rbSpeed;
    private EditText etComment;
    private MaterialButton btnCancel;
    private MaterialButton btnSubmitRating;

    /**
     * Initializes UI components and sets up the presenter.
     * @param savedInstanceState Saved state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_evaluation);

        String bookingId = getIntent().getStringExtra("bookingID");

        tvDriverName = findViewById(R.id.tvDriverName);
        tvCarInfo = findViewById(R.id.tvCarInfo);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvBookingDetails = findViewById(R.id.tvBookingDetails);
        rbBehavior = findViewById(R.id.rbBehavior);
        rbVehicle = findViewById(R.id.rbVehicle);
        rbSpeed = findViewById(R.id.rbSpeed);
        etComment = findViewById(R.id.etRatingComment);
        btnCancel = findViewById(R.id.btnCancelEvaluation);
        btnCancel.setOnClickListener(v -> presenter.onCancelRating());

        btnSubmitRating = findViewById(R.id.btnSubmitRating);
        btnSubmitRating.setOnClickListener(v -> presenter.onSubmitRating());

        presenter = new EvaluationPresenter(this, bookingId);
    }

    /**
     * Collects values from all rating bars.
     * @return List of rating values for behavior, vehicle, and speed.
     */
    @Override
    public ArrayList<Double> getRatings() {
        ArrayList<Double> ratings = new ArrayList<>();
        ratings.add((double) rbBehavior.getRating());
        ratings.add((double) rbVehicle.getRating());
        ratings.add((double) rbSpeed.getRating());
        return ratings;
    }

    /**
     * Retrieves the text from the comment input field.
     * @return User's written feedback as a string.
     */
    @Override
    public String getComment() {
        return etComment.getText().toString();
    }

    @Override
    public void setDriverName(String name) { tvDriverName.setText(name); }

    @Override
    public void setCarInfo(String info) { tvCarInfo.setText(info); }

    @Override
    public void setCustomerName(String name) { tvCustomerName.setText(name); }

    @Override
    public void setBookingDetails(String details) { tvBookingDetails.setText(details); }

    /**
     * Displays a brief error notification.
     * @param message The text to show in the toast.
     */
    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Navigates to ride tracking or closes the screen after submission.
     * @param bookingID The identifier of the relevant booking.
     */
    @Override
    public void navigateToRideTracking(String bookingID) {
        Toast.makeText(this, "Feedback Submitted!", Toast.LENGTH_SHORT).show();
        boolean shouldOpen = getIntent().getBooleanExtra("open", false);
        if (shouldOpen) {
            Intent intent = new Intent(this, TrackPickUpActivity.class);
            intent.putExtra("bookingID", bookingID);
            startActivity(intent);
        }
        finish();
    }

    @Override
    public void navigateToCancelRating(String bookingID) {
        boolean shouldOpen = getIntent().getBooleanExtra("open", false);
        if (shouldOpen) {
            Intent intent = new Intent(this, TrackPickUpActivity.class);
            intent.putExtra("bookingID", bookingID);
            startActivity(intent);
        }
        finish();
    }


    /**
     * Checks if all rating categories have been filled.
     * @return True if all ratings are greater than zero.
     */
    @Override
    public boolean areRatingsValid() {
        return rbBehavior.getRating() > 0 &&
                rbVehicle.getRating() > 0 &&
                rbSpeed.getRating() > 0;
    }
}