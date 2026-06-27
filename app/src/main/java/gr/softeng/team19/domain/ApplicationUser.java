package gr.softeng.team19.domain;

import java.time.LocalDate;
import java.util.ArrayList;

public abstract class ApplicationUser extends User {
    private String name;
    private String surname;
    private String phoneNumber;
    private LocalDate birthDate;
    protected String accountID;
    private Integer completedRides;
    private String creditCardNumber;
    private Integer numberOfRatings;
    private Address address;
    protected GPSLocation userLocation;
    protected ArrayList<TaxiBooking> bookingHistory;
    protected ArrayList<DriverRating> driverRatings;
    

    public ApplicationUser(String userName, String password, String email,
                       String name, String surname,
                       String phoneNumber, LocalDate birthDate,
                       String creditCardNumber,
                       Double latitude, Double longitude,
                       String street, String city, Integer streetNumber, Integer postalCode) {
        super(userName, password, email);
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.completedRides = 0;
        this.creditCardNumber = creditCardNumber;
        this.numberOfRatings = 0;
        this.userLocation = new GPSLocation(latitude, longitude);
        this.address = new Address(street, city, streetNumber, postalCode);

        this.bookingHistory = new ArrayList<>();
        this.driverRatings = new ArrayList<>();
    }

    public String getFullDetails() {
        return "User Details:\n" +
        "Username: " + getUserName()  + "\n" +
            "Password: " + getPassword() + "\n" +
            "Name: " + name + "\n" +
            "Surname: " + surname + "\n" + 
            "Email: " + getEmail() + "\n" +
            "Phone: " + phoneNumber + "\n" +
            "Birth Date: " + birthDate.toString() + "\n" +
            "Credit Card Number: " + creditCardNumber + "\n" +
            "Address: " + address.toString() + "\n" +
            "GPS: " + userLocation.toString() + "\n";
}

    public ArrayList<TaxiBooking> getBookingHistory() {
        return bookingHistory;
    }


    // Adds a new booking to the user's history
    public void addBooking(TaxiBooking booking) {
        // Prevent adding the same booking twice
        if(bookingHistory.contains(booking)) return;

        bookingHistory.add(booking);

        // If the booking already has a rating, save it to the user's rating list
        if (booking.getDriverRating() != null) {
            this.driverRatings.add(booking.getDriverRating());
        }
    }

    // Updates an existing booking's data in the list
    public void updateBooking(TaxiBooking booking) {
        String bookingID = booking.getBookingID();
        int indexToReplace = -1;

        // Find the index of the booking we want to update by its ID
        for (int i = 0; i < bookingHistory.size(); i++) {
            if (bookingHistory.get(i).getBookingID().equals(bookingID)) {
                indexToReplace = i;
                break;
            }
        }

        if (indexToReplace != -1) {
            // If the booking is marked as completed, increment the ride counter
            if (booking.getStatus().equals("RouteCompleted")) {
                this.setCompletedRides(this.getCompletedRides() + 1);
            }
            // Replace the old booking object with the updated one
            bookingHistory.set(indexToReplace, booking);
            // Also update any rating changes associated with this booking
            updateDriverRatings(booking.getDriverRating());
        }
    }

    // Logic to either replace or add a new rating
    public void updateDriverRatings(DriverRating rating) {
        if (rating == null) return;

        String ratingID = rating.getRatingID();
        int indexToReplace = -1;

        // Look for an existing rating with the same ID
        for (int i = 0; i < driverRatings.size(); i++) {
            if (driverRatings.get(i).getRatingID().equals(ratingID)) {
                indexToReplace = i;
                break;
            }
        }

        if (indexToReplace != -1) {
            // Update the existing rating
            driverRatings.set(indexToReplace, rating);
        } else {
            // If not found, add it as a new entry
            driverRatings.add(rating);
        }
    }

   public ArrayList<DriverRating> getDriverRatings() {
        return driverRatings;
    }

    // Adds a rating and increments the total rating count
    public void addDriverRatings(DriverRating driverRating) {
        if (driverRatings.contains(driverRating)) return;
        this.driverRatings.add(driverRating);
        this.setNumberOfRatings(this.getNumberOfRatings() + 1);
    }


    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public Integer getCompletedRides() {
        return completedRides;
    }

    public void setCompletedRides(Integer completedRides) {
        this.completedRides = completedRides;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public Integer getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(Integer numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    public GPSLocation getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(Double latitude, Double longitude) {
        userLocation.setLatitude(latitude);
        userLocation.setLongitude(longitude);
    }
}
