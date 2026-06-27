package gr.softeng.team19.domain;
public class Document {
    
    private DocumentType documentType;
    private String documentID;

    private TaxiDriver driver;


    public Document(DocumentType documentType, TaxiDriver driver) {
        this.documentID = GlobalID.getNextDocumentID();
        this.documentType = documentType;
        this.driver = driver;
    }
    public String getDocumentID() {
        return documentID;
    }
    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String toString() {
        return "Document Type: " + documentType;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public TaxiDriver getDriver() {
        return driver;
    }

    public void setDriver(TaxiDriver driver) {
        this.driver = driver;
    }

    public enum DocumentType {
        drivingLicense,
        IDCard,
        VehicleRegistration,
        ProfessionalTaxiDriverLicense
    }
}
