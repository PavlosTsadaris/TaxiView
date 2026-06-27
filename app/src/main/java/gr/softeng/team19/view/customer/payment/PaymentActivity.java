package gr.softeng.team19.view.customer.payment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import gr.softeng.team19.R;
import gr.softeng.team19.domain.Payment;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.view.customer.homeactivity.HomeCustomerActivity;

/**
 * Activity that handles the payment process for a completed ride.
 * <p>
 * Displays the final booking details (time, distance, cost) and allows the user
 * to pay via Cash or Card. It simulates bank processing and displays success/error prompts.
 * </p>
 */
public class PaymentActivity extends AppCompatActivity implements PaymentView {

    private PaymentPresenter presenter;
    private TextView tvBookingID, tvTimeStart, tvTimeEnd, tvDistance, txtTotalAmount;
    private RadioGroup radioGroupPayment;
    private MaterialButton btnPayNow;
    private View bankOverlay;
    private androidx.cardview.widget.CardView cardSuccessPrompt;
    private androidx.cardview.widget.CardView cardErrorPrompt;
    private androidx.cardview.widget.CardView cardNewCardInput;
    private androidx.cardview.widget.CardView cardCardPrompt;
    private androidx.cardview.widget.CardView cardCashPrompt;

    /**
     * Called when the activity is starting.
     * Initializes the view, receives booking data, and sets up the presenter.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     * this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // 1. Initialize Views
        initViews();

        // 2. Receive data from the previous Intent
        String bookingID = getIntent().getStringExtra("bookingID");
        double amount = getIntent().getDoubleExtra("amount", 0.0);

        // 3. Create the Presenter
        presenter = new PaymentPresenter(this, bookingID, amount);

        // 4. Setup Listener for "Pay Now" button
        btnPayNow.setOnClickListener(v -> {
            int checkedId = radioGroupPayment.getCheckedRadioButtonId();
            Payment.PaymentMethod method;

            // Determine method based on selected RadioButton
            if (checkedId == R.id.radioCash) {
                method = Payment.PaymentMethod.PayByCash;
            } else {
                // Both Saved and New card map to the PayByCard domain method
                method = Payment.PaymentMethod.PayByCard;
            }

            presenter.onConfirmClicked(method);
        });
    }

    /**
     * Finds and assigns all UI components to their respective variables.
     */
    private void initViews() {
        tvBookingID = findViewById(R.id.tvBookingID);
        tvTimeStart = findViewById(R.id.tvTimeInfoBegin);
        tvTimeEnd = findViewById(R.id.End);
        tvDistance = findViewById(R.id.tvDistance);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);
        radioGroupPayment = findViewById(R.id.radioGroupPayment);
        btnPayNow = findViewById(R.id.btnPayNow);
        bankOverlay = findViewById(R.id.layoutBankConnecting);
    }

    // --- VIEW INTERFACE IMPLEMENTATION ---

    /**
     * Updates the UI with the final details of the completed ride.
     *
     * @param id    The booking ID.
     * @param start The start time string.
     * @param end   The end time string.
     * @param dist  The total distance in kilometers.
     * @param amt   The total amount to be paid.
     */
    @Override
    public void displayBookingDetails(String id, String start, String end, double dist, double amt) {
        tvBookingID.setText("Booking ID: #" + id);
        tvTimeStart.setText("Start Time: " + start);
        tvTimeEnd.setText("End Time: " + end);
        tvDistance.setText(String.format("Total Distance: %.2f km", dist));
        txtTotalAmount.setText(String.format("€ %.2f", amt));
    }

    /**
     * Displays a prompt asking the user to confirm cash payment.
     */
    @Override
    public void showCashPaymentPrompt() {
        View blurScrim = findViewById(R.id.paymentBlurScrim);
        cardCashPrompt = findViewById(R.id.cardCashPayment);

        // Show "curtain" and window
        blurScrim.setVisibility(View.VISIBLE);
        cardCashPrompt.setVisibility(android.view.View.VISIBLE);
        cardCashPrompt.bringToFront();

        findViewById(R.id.btnOkCash).setOnClickListener(v -> {
            presenter.onCashConfirmed();
        });

        findViewById(R.id.btnBackCash).setOnClickListener(v -> {
            cardCashPrompt.setVisibility(android.view.View.GONE);
            blurScrim.setVisibility(View.GONE);
            cardCashPrompt = null;
        });
    }

    /**
     * Displays a prompt allowing the user to choose between a saved card or a new card.
     */
    @Override
    public void showCardPaymentPrompt() {
        View blurScrim = findViewById(R.id.paymentBlurScrim);
        cardCardPrompt = findViewById(R.id.cardCardPayment);

        // Show "curtain" and window
        blurScrim.setVisibility(View.VISIBLE);
        cardCardPrompt.setVisibility(android.view.View.VISIBLE);
        cardCardPrompt.bringToFront();

        findViewById(R.id.btnNewCard).setOnClickListener(v -> {
            cardCardPrompt.setVisibility(android.view.View.GONE);
            showNewCardPrompt();
        });

        findViewById(R.id.btnSavedCard).setOnClickListener(v -> {
            cardCardPrompt.setVisibility(android.view.View.GONE);
            presenter.onSavedCardConfirmed();
        });

        findViewById(R.id.btnBackCard).setOnClickListener(v -> {
            cardCardPrompt.setVisibility(android.view.View.GONE);
            blurScrim.setVisibility(View.GONE);
            cardCardPrompt = null;
        });
    }

    /**
     * Displays a success overlay indicating the payment was processed correctly.
     */
    @Override
    public void showSuccessPaymentPrompt() {
        if (bankOverlay != null) bankOverlay.setVisibility(View.GONE);

        View blurScrim = findViewById(R.id.paymentBlurScrim);
        cardSuccessPrompt = findViewById(R.id.cardSuccessPayment);

        blurScrim.setVisibility(View.VISIBLE);
        cardSuccessPrompt.setVisibility(android.view.View.VISIBLE);
        cardSuccessPrompt.bringToFront();
    }

    /**
     * Displays an error overlay indicating the payment failed.
     * Automatically hides after a short delay.
     */
    @Override
    public void showErrorPaymentPrompt() {
        if (bankOverlay != null) bankOverlay.setVisibility(View.GONE);

        View blurScrim = findViewById(R.id.paymentBlurScrim);
        cardErrorPrompt = findViewById(R.id.cardErrorPayment);

        blurScrim.setVisibility(View.VISIBLE);
        cardErrorPrompt.setVisibility(android.view.View.VISIBLE);
        cardErrorPrompt.bringToFront();

        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            blurScrim.setVisibility(android.view.View.GONE);
            cardErrorPrompt.setVisibility(android.view.View.GONE);
        }, 2500);
    }

    /**
     * Shows a processing overlay (e.g., "Connecting with bank...").
     * @param message The message to display.
     */
    @Override
    public void showProcessing(String message) {
        if (cardCashPrompt != null) cardCashPrompt.setVisibility(View.GONE);
        if (cardCardPrompt != null) cardCardPrompt.setVisibility(View.GONE);

        View blurScrim = findViewById(R.id.paymentBlurScrim);
        if (blurScrim != null) blurScrim.setVisibility(View.VISIBLE);

        TextView tvStatus = findViewById(R.id.tvBankStatus);

        if (bankOverlay != null && tvStatus != null) {
            tvStatus.setText(message);
            bankOverlay.setVisibility(View.VISIBLE);
            bankOverlay.bringToFront();
        }
    }

    /**
     * Displays a prompt for entering new credit card details.
     */
    @Override
    public void showNewCardPrompt() {
        View blurScrim = findViewById(R.id.paymentBlurScrim);
        cardNewCardInput = findViewById(R.id.cardNewCardInput);

        // Show "curtain" and window
        blurScrim.setVisibility(View.VISIBLE);
        cardNewCardInput.setVisibility(android.view.View.VISIBLE);
        cardNewCardInput.bringToFront();

        findViewById(R.id.btnSubmitNewCard).setOnClickListener(v -> {
            String cardNumber = ((com.google.android.material.textfield.TextInputEditText)
                    findViewById(R.id.etCardNumber)).getText().toString();
            if (cardNumber.isEmpty()){
                android.widget.Toast.makeText(this, "Fill The Gaps!", android.widget.Toast.LENGTH_SHORT).show();
            }else{
                cardNewCardInput.setVisibility(android.view.View.GONE);
                presenter.onNewCardConfirmed(cardNumber);
            }
        });

        findViewById(R.id.btnBackFromCards).setOnClickListener(v -> {
            cardNewCardInput.setVisibility(android.view.View.GONE);
            cardCardPrompt.setVisibility(android.view.View.VISIBLE);
            blurScrim.setVisibility(View.GONE);
            cardNewCardInput = null;
        });
    }

    /**
     * Navigates back to the Home Customer Activity after a successful payment.
     * Clears the activity stack so the user cannot go back to the payment screen.
     *
     * @param booking The completed booking object.
     */
    @Override
    public void navigateToHome(TaxiBooking booking) {
        Intent intent = new Intent(this, HomeCustomerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("username", booking.getCustomer().getUserName());
        startActivity(intent);
        finish();
    }
}