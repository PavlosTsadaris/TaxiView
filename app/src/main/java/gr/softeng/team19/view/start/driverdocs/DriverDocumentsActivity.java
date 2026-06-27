package gr.softeng.team19.view.start.driverdocs;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import gr.softeng.team19.R;
import gr.softeng.team19.view.start.success_create.SuccessActivity;

/**
 * Screen where drivers upload their documents and enter vehicle details.
 * It manages the buttons for uploading files and the final account creation.
 */
public class DriverDocumentsActivity extends AppCompatActivity implements DriverDocumentsView {

    private DriverDocumentPresenter presenter;
    private Button btnLicense, btnID, btnVehicle, btnProLicense;
    private EditText etManufacturer, etModel, etLicensePlate;

    /**
     * Sets up the screen, finds the buttons and text fields, and connects them to the presenter.
     * @param savedInstanceState Data from a previous session, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_documents);

        // Create the presenter to handle the screen logic
        presenter = new DriverDocumentPresenter(this);

        // Link the vehicle information fields
        etManufacturer = findViewById(R.id.etManufacturer);
        etModel = findViewById(R.id.etModel);
        etLicensePlate = findViewById(R.id.etLicensePlate);

        // Link the upload buttons and the finalize button
        btnLicense = findViewById(R.id.btnUploadLicense);
        btnID = findViewById(R.id.btnUploadID);
        btnVehicle = findViewById(R.id.btnUploadVehicle);
        btnProLicense = findViewById(R.id.btnUploadProLicense);
        Button btnSubmit = findViewById(R.id.btnFinalizeDriver);

        // Tell the presenter when an upload button or the submit button is clicked
        btnLicense.setOnClickListener(v -> presenter.onUploadLicense());
        btnID.setOnClickListener(v -> presenter.onUploadID());
        btnVehicle.setOnClickListener(v -> presenter.onUploadVehicle());
        btnProLicense.setOnClickListener(v -> presenter.onUploadProLicense());

        btnSubmit.setOnClickListener(v -> presenter.onSubmit());
    }

    /**
     * Gets the vehicle brand/manufacturer from the input field.
     * @return The text entered in the manufacturer field.
     */
    @Override
    public String getManufacturer() {
        return etManufacturer.getText().toString();
    }

    /**
     * Gets the vehicle model from the input field.
     * @return The text entered in the model field.
     */
    @Override
    public String getModel() {
        return etModel.getText().toString();
    }

    /**
     * Gets the vehicle's license plate from the input field.
     * @return The text entered in the license plate field.
     */
    @Override
    public String getLicensePlate() {
        return etLicensePlate.getText().toString();
    }

    /**
     * Changes a button's style to show that a document was successfully uploaded.
     * It changes the text to "Uploaded" and turns the button green.
     * @param btn The specific button to change.
     */
    private void changeButtonVisuals(Button btn) {
        btn.setText(getString(R.string.btn_uploaded));
        btn.setBackgroundColor(Color.parseColor("#1B664D"));
        btn.setTextColor(Color.WHITE);
    }

    /**
     * Marks the Driver's License button as uploaded.
     */
    @Override
    public void markLicenseUploaded() {
        changeButtonVisuals(btnLicense);
    }

    /**
     * Marks the ID Card button as uploaded.
     */
    @Override
    public void markIDUploaded() {
        changeButtonVisuals(btnID);
    }

    /**
     * Marks the Vehicle Registration button as uploaded.
     */
    @Override
    public void markVehicleUploaded() {
        changeButtonVisuals(btnVehicle);
    }

    /**
     * Marks the Professional License button as uploaded.
     */
    @Override
    public void markProLicenseUploaded() {
        changeButtonVisuals(btnProLicense);
    }

    /**
     * Opens the Success screen after registration is complete and closes this screen.
     */
    @Override
    public void navigateToSuccess() {
        Intent intent = new Intent(DriverDocumentsActivity.this, SuccessActivity.class);
        intent.putExtra("MESSAGE", getString(R.string.msg_driver_success));

        startActivity(intent);
        finish();
    }

    /**
     * Shows an error message using a short Toast notification.
     * @param messageId The ID of the string resource to show.
     */
    @Override
    public void showErrorMessage(int messageId) {
        Toast.makeText(this, getString(messageId), Toast.LENGTH_SHORT).show();
    }
}