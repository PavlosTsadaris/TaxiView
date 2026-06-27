package gr.softeng.team19.view.start.driverdocs;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDate;
import gr.softeng.team19.R;
import gr.softeng.team19.domain.RegistrationData;
import gr.softeng.team19.memorydao.TaxiDriverDAOMemory;
import gr.softeng.team19.memorydao.UserDAOMemory;

/**
 * Unit Tests for DriverDocumentPresenter focusing on document validation and registration.
 */
public class DriverDocumentPresenterTest {
    private DriverDocumentPresenter presenter;
    private DriverDocumentsViewStub viewStub;

    /**
     * Initializes testing environment and mocks registration data.
     */
    @Before
    public void setUp() {
        viewStub = new DriverDocumentsViewStub();
        presenter = new DriverDocumentPresenter(viewStub);

        new UserDAOMemory().findAll().clear();
        new TaxiDriverDAOMemory().findAll().clear();

        RegistrationData.username = "testDriver";
        RegistrationData.password = "pass";
        RegistrationData.phone = "1234567890";
        RegistrationData.birthDate = LocalDate.now();
    }

    /**
     * Verifies that each document upload action correctly updates the UI state.
     */
    @Test
    public void testDocumentUploadMarkers() {
        presenter.onUploadLicense();
        Assert.assertTrue(viewStub.licenseMarked);

        presenter.onUploadID();
        Assert.assertTrue(viewStub.idMarked);

        presenter.onUploadVehicle();
        Assert.assertTrue(viewStub.vehicleMarked);

        presenter.onUploadProLicense();
        Assert.assertTrue(viewStub.proLicenseMarked);
    }

    /**
     * Verifies error handling when vehicle information is missing.
     */
    @Test
    public void testSubmitWithEmptyFields() {
        viewStub.manufacturer = "";
        presenter.onSubmit();
        Assert.assertEquals(R.string.error_empty_fields, viewStub.lastErrorMessage);
    }

    /**
     * Verifies that the system prevents submission if not all documents are uploaded.
     */
    @Test
    public void testSubmitMissingDocuments() {
        viewStub.manufacturer = "Tesla";
        viewStub.model = "Model 3";
        viewStub.licensePlate = "ELE-1234";

        presenter.onUploadLicense();

        presenter.onSubmit();
        Assert.assertEquals(R.string.error_upload_required, viewStub.lastErrorMessage);
        Assert.assertFalse(viewStub.successNavigated);
    }

    /**
     * Verifies successful driver creation and data persistence.
     */
    @Test
    public void testSuccessfulSubmit() {
        viewStub.manufacturer = "Toyota";
        viewStub.model = "Corolla";
        viewStub.licensePlate = "YZZ-5566";

        presenter.onUploadLicense();
        presenter.onUploadID();
        presenter.onUploadVehicle();
        presenter.onUploadProLicense();

        presenter.onSubmit();

        Assert.assertTrue(viewStub.successNavigated);

        TaxiDriverDAOMemory driverDAO = new TaxiDriverDAOMemory();
        Assert.assertNotNull(driverDAO.find("testDriver"));
        Assert.assertNull(RegistrationData.username);
    }
}