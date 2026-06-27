package gr.softeng.team19.domain;
import java.time.LocalDate;
import java.util.ArrayList;

public class Customer extends ApplicationUser {
    
    
    private ArrayList<TaxiRideRequest> rideRequests;
    
    
    public Customer(String userName, String password, String email, String name, String surname,
            String phoneNumber, LocalDate birthDate,
            String creditCardNumber, Double latitude, Double longitude, String street, String city, Integer streetNumber, Integer postalCode) {
        super(userName, password, email, name, surname, phoneNumber, birthDate, creditCardNumber, latitude,
                longitude, street, city, streetNumber, postalCode);
        this.accountID = GlobalID.getNextUserID();

        rideRequests = new ArrayList<>();

    }

    // method to create a new ride request using the customer's current GPS location
    public TaxiRideRequest callTaxi(Address destinationAddress) { // From controller
        TaxiRideRequest request = new TaxiRideRequest(getUserLocation(),
                destinationAddress, this);
        addRideRequest(request);

        return request;
    }
    public ArrayList<TaxiRideRequest> getRideRequests() {
        return rideRequests;
    }

    public void addRideRequest(TaxiRideRequest request) {
        this.rideRequests.add(request);
    }



    // Updates a ride request in the list by matching IDs
    public void updateRideRequest(TaxiRideRequest request) {
        String requestId = request.getRequestID();
        int indexToReplace = -1;

        for (int i = 0; i < rideRequests.size(); i++) {
            if (rideRequests.get(i).getRequestID().equals(requestId)) {
                indexToReplace = i;
                break;
            }
        }

        if (indexToReplace != -1) {
            rideRequests.set(indexToReplace, request);
        }
    }

}
