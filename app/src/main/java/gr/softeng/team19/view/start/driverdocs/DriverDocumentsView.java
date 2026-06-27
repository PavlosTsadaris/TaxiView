package gr.softeng.team19.view.start.driverdocs;

/**
 * Interface that defines the UI actions for the Driver Documents screen.
 * It is implemented by the Activity to handle vehicle data input and document status.
 */
public interface DriverDocumentsView {

    /**
     * Gets the vehicle manufacturer entered by the user in the text field.
     * @return A string representing the brand of the car.
     */
    String getManufacturer();

    /**
     * Gets the vehicle model entered by the user in the text field.
     * @return A string representing the specific car model.
     */
    String getModel();

    /**
     * Gets the license plate number entered by the user.
     * @return A string representing the car's registration plate.
     */
    String getLicensePlate();

    /**
     * Updates the UI to show that the driver's license has been uploaded.
     */
    void markLicenseUploaded();

    /**
     * Updates the UI to show that the identity card (ID) has been uploaded.
     */
    void markIDUploaded();

    /**
     * Updates the UI to show that the vehicle registration has been uploaded.
     */
    void markVehicleUploaded();

    /**
     * Updates the UI to show that the professional driving license has been uploaded.
     */
    void markProLicenseUploaded();

    /**
     * Opens the success screen after a successful registration.
     */
    void navigateToSuccess();

    /**
     * Displays an error message using a string resource ID.
     * @param messageId The ID of the error message to be shown.
     */
    void showErrorMessage(int messageId);
}