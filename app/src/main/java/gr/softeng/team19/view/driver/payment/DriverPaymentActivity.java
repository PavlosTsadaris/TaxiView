package gr.softeng.team19.view.driver.payment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.button.MaterialButton;

import gr.softeng.team19.R;
import gr.softeng.team19.view.driver.homeactivity.HomeDriverActivity;

/**
 * Screen where the driver confirms the final payment for a ride.
 * It handles the total amount input and shows a loading screen while
 * the transaction is being processed.
 */
public class DriverPaymentActivity extends AppCompatActivity implements DriverPaymentView {

    private DriverPaymentPresenter presenter;

    private TextView txtCustomerName;
    private EditText edtAmount;
    private MaterialButton btnConfirm;
    private View viewBlurScrim;
    private View cardWaitForPayment;
    private ProgressBar progressBarPayment;
    private ImageView imgPaymentSuccess;
    private TextView txtPaymentStatus;

    /**
     * Sets up the payment screen, binds UI elements, and prepares the presenter.
     * @param savedInstanceState Data from a previous session, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Force light mode for clear visibility of payment colors
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_driver_payment);

        progressBarPayment = findViewById(R.id.progressBarPayment);
        imgPaymentSuccess = findViewById(R.id.imgPaymentSuccess);
        txtPaymentStatus = findViewById(R.id.txtPaymentStatus);
        viewBlurScrim = findViewById(R.id.viewBlurScrim);
        cardWaitForPayment = findViewById(R.id.cardWaitForPayment);
        txtCustomerName = findViewById(R.id.txtCustomerName);
        edtAmount = findViewById(R.id.edtAmount);
        btnConfirm = findViewById(R.id.btnConfirmPayment);

        // Get ride details sent from the previous screen
        String bookingID = getIntent().getStringExtra("bookingID");
        Double amount = getIntent().getDoubleExtra("amount", 0.0);

        presenter = new DriverPaymentPresenter(this, bookingID, amount);

        // When driver clicks confirm, tell the presenter to validate the amount
        btnConfirm.setOnClickListener(v -> {
            presenter.onConfirmPayment(edtAmount.getText().toString());
        });
    }

    /**
     * Displays the name of the customer who is paying.
     * @param name The customer's full name.
     */
    @Override
    public void setCustomerName(String name) {
        txtCustomerName.setText(name);
    }

    /**
     * Fills the amount field with the calculated fare.
     * @param amount The cost of the ride.
     */
    @Override
    public void setAmount(double amount) {
        edtAmount.setText(String.format(java.util.Locale.US, "%.2f", amount));
    }

    /**
     * Shows a quick notification message.
     * @param message The text to display.
     */
    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Returns the driver to the home dashboard and clears the payment screen from history.
     * @param username The driver's ID for the home screen.
     */
    @Override
    public void navigateToHome(String username) {
        Intent intent = new Intent(this, HomeDriverActivity.class);
        intent.putExtra("username", username);
        // Ensure the driver cannot go "back" to the payment screen after finishing
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Shows an error message directly on the input field (e.g., if the price is invalid).
     * @param errorMsg The error text to display.
     */
    @Override
    public void showInputError(String errorMsg) {
        edtAmount.setError(errorMsg);
        edtAmount.requestFocus();
    }

    /**
     * Displays a "Please Wait" overlay to block interaction while the payment processes.
     */
    @Override
    public void showWaitingState() {
        viewBlurScrim.setVisibility(View.VISIBLE);
        cardWaitForPayment.setVisibility(View.VISIBLE);
    }

    /**
     * Removes the "Please Wait" overlay.
     */
    @Override
    public void hideWaitingState() {
        viewBlurScrim.setVisibility(View.GONE);
        cardWaitForPayment.setVisibility(View.GONE);
    }

    /**
     * Shows a success icon and updates the status text when the payment is done.
     */
    @Override
    public void showSuccessState() {
        progressBarPayment.setVisibility(View.GONE);
        imgPaymentSuccess.setVisibility(View.VISIBLE);
        txtPaymentStatus.setText("Payment Completed");
    }
}