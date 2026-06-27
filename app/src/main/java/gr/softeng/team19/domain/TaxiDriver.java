package gr.softeng.team19.domain;

import java.time.LocalDate;
import java.util.ArrayList;

public class TaxiDriver extends ApplicationUser {

    private Double averageRating;
    private Boolean availabity;
    private ArrayList<Document> requiredDocuments;
    private Vehicle vehicle;

    public TaxiDriver(String userName, String password, String email, String name, String surname,
            String phoneNumber, LocalDate birthDate,
            String creditCardNumber, Double latitude, Double longitude, String street, String city, Integer streetNumber, Integer postalCode,
             String licensePlate, String manufacturer, String model) {
        super(userName, password, email, name, surname, phoneNumber, birthDate, creditCardNumber,  latitude,
                longitude, street, city, streetNumber, postalCode);
        this.averageRating = 0.0;
        this.availabity = true;
        this.vehicle = new Vehicle(licensePlate, manufacturer, model, this);
        this.requiredDocuments = new ArrayList<Document>();
        this.accountID = GlobalID.getNextDriverID();

    }

    @Override
    public String getFullDetails() {
        return 
            "Average Rating: " + averageRating + "\n" +
            "Availability: " + availabity + "\n" +
            "Vehicle: " + vehicle.toString() + "\n" +
            "Required Documents: " + requiredDocuments.toString() + "\n"
            ;
    }

    // Extends the base addBooking to also trigger an average rating recalculation
    @Override
    public void addBooking(TaxiBooking booking) {
        super.addBooking(booking);
        if (booking.getDriverRating() != null) {
            updateAverageRating(booking.getDriverRating().getAverageRating());
        }
    }

    // Extends the base updateBooking to refresh the average rating if a review was updated
    @Override
    public void updateBooking(TaxiBooking booking) {
        super.updateBooking(booking);
        if (bookingHistory.contains(booking)) {
            if (booking.getDriverRating() != null) {
                updateAverageRating(booking.getDriverRating().getAverageRating());
            }
        }
    }

    // Math for recalculating the average: (OldAverage * PreviousCount + NewRating) / TotalCount
    public void updateAverageRating(double newAverageRating) {
        averageRating = (averageRating * (bookingHistory.size() - 1) + newAverageRating) / bookingHistory.size();
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Boolean getAvailability() {
        return availabity;
    }
    
    public void setAvailability(Boolean availabity) {
        this.availabity = availabity;
    }


   public Vehicle getVehicle() {
       return vehicle;
   }

    public void setVehicle(Vehicle vehicle) {
         this.vehicle = vehicle;
    }
   
    public ArrayList<Document> getRequiredDocuments() {
        return requiredDocuments;
    }

    public void setNewDocument(Document document) {
        this.requiredDocuments.add(document);
    }

    
}
