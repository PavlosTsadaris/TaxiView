package gr.softeng.team19.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DocumentTest {

    private Document document;
    private TaxiDriver driver;

    @Before
    public void setUp() throws Exception{
        driver = new TaxiDriver(
                "driver1", "pass", "driver@test.com",
                "John", "Doe", "6900000000",
                java.time.LocalDate.of(1990, 1, 1),
                "123456789",
                37.9838, 23.7275, // Συντεταγμένες
                "Stadiou", "Athens", 5, 10561,
                "ABC-1234", "Skoda", "Octavia"
        );
        document = new Document(Document.DocumentType.IDCard, driver);
    }



    @Test
    public void constructor_initializesCorrectly(){
        assertNotNull(document.getDocumentID());
        assertEquals(Document.DocumentType.IDCard, document.getDocumentType());
        assertEquals(driver, document.getDriver());
    }

    @Test
    public void testToString() {
        String result = document.toString();
        assertTrue(result.contains("IDCard"));
    }

    @Test
    public void getDocumentType() {
        Document.DocumentType type = document.getDocumentType();

        assertTrue(type == Document.DocumentType.IDCard ||
                type == Document.DocumentType.drivingLicense ||
                type == Document.DocumentType.VehicleRegistration ||
                type == Document.DocumentType.ProfessionalTaxiDriverLicense);
    }
    
    @Test
    public void documentID_startsWithD() {
        String id = document.getDocumentID();
        assertTrue(id.startsWith("D"));
    }
}