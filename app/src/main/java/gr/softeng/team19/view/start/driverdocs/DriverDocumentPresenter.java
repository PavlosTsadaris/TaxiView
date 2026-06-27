package gr.softeng.team19.view.start.driverdocs;

import gr.softeng.team19.R;
import gr.softeng.team19.domain.RegistrationData;
import gr.softeng.team19.domain.TaxiDriver;
import gr.softeng.team19.memorydao.TaxiDriverDAOMemory;
import gr.softeng.team19.memorydao.UserDAOMemory;
import gr.softeng.team19.view.start.signup.SignUpPresenter;

/**
 * Presenter class that manages the document upload screen for new drivers.
 * It tracks if all required files (license, ID, etc.) are uploaded and saves
 * the final driver profile to the database.
 */
public class DriverDocumentPresenter extends SignUpPresenter {
    private DriverDocumentsView view;

    // Flags to keep track of which documents the user has uploaded
    private boolean isLicenseUploaded = false;
    private boolean isIDUploaded = false;
    private boolean isVehicleUploaded = false;
    private boolean isProLicenseUploaded = false;

    /**
     * Constructor that links the presenter with the document upload screen.
     * @param view The UI interface for the driver documents screen.
     */
    public DriverDocumentPresenter(DriverDocumentsView view) {
        super(null);
        this.view = view;
    }

    /**
     * Called when the driver uploads their driving license.
     */
    public void onUploadLicense() {
        isLicenseUploaded = true;
        view.markLicenseUploaded();
    }

    /**
     * Called when the driver uploads their identity card (ID).
     */
    public void onUploadID() {
        isIDUploaded = true;
        view.markIDUploaded();
    }

    /**
     * Called when the driver uploads their vehicle registration documents.
     */
    public void onUploadVehicle() {
        isVehicleUploaded = true;
        view.markVehicleUploaded();
    }

    /**
     * Called when the driver uploads their professional driving license.
     */
    public void onUploadProLicense() {
        isProLicenseUploaded = true;
        view.markProLicenseUploaded();
    }

    /**
     * Validates the vehicle information and document status before finishing registration.
     * If everything is correct, it creates the TaxiDriver account and saves it.
     */
    public void onSubmit() {
        // Get vehicle details from the input fields in the view
        String manufacturer = view.getManufacturer();
        String model = view.getModel();
        String licensePlate = view.getLicensePlate();

        // Step 1: Check if vehicle text fields are filled
        if (manufacturer.isEmpty() || model.isEmpty() || licensePlate.isEmpty()) {
            view.showErrorMessage(R.string.error_empty_fields);
            return;
        }

        // Step 2: Check if all 4 required documents have been uploaded
        if (isLicenseUploaded && isIDUploaded && isVehicleUploaded && isProLicenseUploaded) {
            view.navigateToSuccess();
        } else {
            view.showErrorMessage(R.string.error_upload_required);
            return; // Stop here if documents are missing
        }

        // Step 3: Create the new TaxiDriver object using data collected from all signup steps
        TaxiDriver driver = new TaxiDriver(
                RegistrationData.username, RegistrationData.password, RegistrationData.email,
                RegistrationData.name, RegistrationData.surname, RegistrationData.phone,
                RegistrationData.birthDate, RegistrationData.card,
                0.0, 0.0, RegistrationData.street, RegistrationData.city,
                RegistrationData.streetNumber, RegistrationData.postalCode,
                licensePlate, manufacturer, model
        );

        // Step 4: Save the driver to the memory database and clear temporary registration data
        RegistrationData.clear();
        UserDAOMemory userDAOMemory = new UserDAOMemory();
        userDAOMemory.add(driver);
        TaxiDriverDAOMemory driverDAOMemory = new TaxiDriverDAOMemory();
        driverDAOMemory.save(driver);
    }
}