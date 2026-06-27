package gr.softeng.team19.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class TaxiRideRequest {
    private String requestID ;
    private LocalTime requestTime;
    private LocalDate date;
    private GPSLocation pickupPoint;
    private Address destination;
    private String status;
    private Customer customer;
    private TaxiDriver chosenDriver;

    private TaxiBooking booking;
    private ArrayList<TaxiDriver> availableDrivers;
    

    public TaxiRideRequest(GPSLocation pickupPoint,
            Address destination, Customer customer) {
        this.requestID = GlobalID.getNextTaxiRideID();
        this.requestTime = LocalTime.now();
        this.date = LocalDate.now();
        this.pickupPoint = pickupPoint;
        this.destination = destination;
        this.status = "PENDING";
        this.customer = customer;
        if (this.customer != null) {
            this.customer.addRideRequest(this);
        }
    }


    public Customer getCustomer(){
        return customer;
    }
    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public LocalTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalTime requestTime) {
        this.requestTime = requestTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Address getDestination() {
        return destination;
    }

    public void setDestination(Address destination) {
        this.destination = destination;
    }

    public GPSLocation getPickupPoint() {
        return pickupPoint;
    }

    public void setPickupPoint(GPSLocation pickupPoint) {
        this.pickupPoint = pickupPoint;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TaxiBooking getBooking() {
        return booking;
    }

    public void setBooking(TaxiBooking booking) {
        this.booking = booking;
    }

    public void setChosenDriver(TaxiDriver chosenDriver) {
        this.chosenDriver = chosenDriver;
    }
    public TaxiDriver getChosenDriver() {
        return chosenDriver;
    }


    public ArrayList<TaxiDriver> findAllDrivers(ArrayList<TaxiDriver> allDrivers) {
        availableDrivers = new ArrayList<>();
        for (TaxiDriver driver : allDrivers) {
            if (driver.getAvailability() && driver.getUserLocation().distanceTo(pickupPoint) <= 3.0) {
                availableDrivers.add(driver);
            }
        }

        return availableDrivers;
    }

    
    public TaxiDriver chooseTaxi(int index) {
        if (index < 0 || index >= availableDrivers.size()) {
            throw new IllegalArgumentException("Invalid choice index");
        }
        this.chosenDriver =  availableDrivers.get(index);

        return chosenDriver;
    }

    public void denyRequest(){
        setStatus("DENIED");
        customer.updateRideRequest(this);
   }

    public TaxiBooking acceptRequest(TaxiDriver taxiDriver) {
        if (taxiDriver == null) {
            throw new IllegalArgumentException("Driver cannot be null");
        }

        this.chosenDriver = taxiDriver;
        setStatus("ACCEPTED");
        TaxiBooking taxiBooking= new TaxiBooking(getPickupPoint(), getDestination(), customer, chosenDriver, this );
        chosenDriver.setAvailability(false);
        booking = taxiBooking;

        customer.updateRideRequest(this);

        return taxiBooking;

    }

}