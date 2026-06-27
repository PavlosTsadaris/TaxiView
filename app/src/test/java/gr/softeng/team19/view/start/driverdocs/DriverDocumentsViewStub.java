package gr.softeng.team19.view.start.driverdocs;

/**
 * Manual stub implementation of DriverDocumentsView for testing state changes and navigation.
 */
public class DriverDocumentsViewStub implements DriverDocumentsView {
    public String manufacturer = "", model = "", licensePlate = "";
    public int lastErrorMessage = 0;
    public boolean successNavigated = false;

    public boolean licenseMarked = false;
    public boolean idMarked = false;
    public boolean vehicleMarked = false;
    public boolean proLicenseMarked = false;

    /** @return Vehicle manufacturer from stub. */
    @Override public String getManufacturer() { return manufacturer; }

    /** @return Vehicle model from stub. */
    @Override public String getModel() { return model; }

    /** @return License plate from stub. */
    @Override public String getLicensePlate() { return licensePlate; }

    /** Marks driver's license as uploaded. */
    @Override public void markLicenseUploaded() { licenseMarked = true; }

    /** Marks personal ID as uploaded. */
    @Override public void markIDUploaded() { idMarked = true; }

    /** Marks vehicle registration as uploaded. */
    @Override public void markVehicleUploaded() { vehicleMarked = true; }

    /** Marks professional license as uploaded. */
    @Override public void markProLicenseUploaded() { proLicenseMarked = true; }

    /** Flags that the success screen navigation was triggered. */
    @Override public void navigateToSuccess() { successNavigated = true; }

    /** * Captures error resource IDs.
     * @param messageId The Android string resource ID.
     */
    @Override public void showErrorMessage(int messageId) { this.lastErrorMessage = messageId; }
}