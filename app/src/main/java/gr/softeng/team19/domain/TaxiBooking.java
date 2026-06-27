package gr.softeng.team19.domain;

import gr.softeng.team19.domain.Payment.PaymentMethod;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class TaxiBooking {

    private String bookingID;
    private LocalTime bookingTime;
    private LocalDate date;
    private Address destination;
    private DriverRating driverRating;
    private GPSLocation pickupPoint;
    private String status;
    public Payment payment;
    private Customer customer;
    private TaxiDriver taxiDriver;
    private Route route;
    private TaxiRideRequest rideRequest;
    private boolean isEvaluated = false;



    public TaxiBooking(GPSLocation pickupPoint, Address destination, Customer customer, TaxiDriver taxiDriver, TaxiRideRequest rideRequest) {
        this.bookingID = GlobalID.getNextBookingID();
        this.bookingTime = LocalTime.now();
        this.date = LocalDate.now();
        this.destination = destination;
        this.pickupPoint = pickupPoint;
        this.status = "WaitingForCustomer";
        this.customer = customer;
        this.taxiDriver = taxiDriver;
        this.rideRequest = rideRequest;


        taxiDriver.addBooking(this);
        customer.addBooking(this);


    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public TaxiRideRequest getRideRequest() {
        return rideRequest;
    }

    public void setRideRequest(TaxiRideRequest rideRequest) {
        this.rideRequest = rideRequest;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public GPSLocation getPickupPoint() {
        return pickupPoint;
    }

    public TaxiDriver getTaxiDriver() {
        return taxiDriver;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalTime bookingTime) {
        this.bookingTime = bookingTime;
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

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public DriverRating getDriverRating() {
        return driverRating; // Return null if no rating has been given yet
    }

    public void setDriverRating(DriverRating driverRating) {
        this.driverRating = driverRating;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public Route startRoute(){
        setStatus("OngoingRoute");
        this.route = new Route (pickupPoint, this);

        taxiDriver.updateBooking(this);
        customer.updateBooking(this);
        rideRequest.setBooking(this);

        return route;
    }

    public double endRoute(Route ongoingRoute, Double latitude, Double longitude){
        setStatus("RouteCompleted");
        double amount = ongoingRoute.findRouteCost(LocalTime.now(), latitude, longitude);
        setStatus("WaitingForPayment");
        
        taxiDriver.updateBooking(this);
        taxiDriver.setCompletedRides(taxiDriver.getCompletedRides() + 1);

        customer.updateBooking(this);
        customer.setCompletedRides(customer.getCompletedRides() + 1);

        rideRequest.setBooking(this);

        return amount;
    }
   

    public Payment payRoute( Double amount, PaymentMethod paymentType){
        this.payment = new Payment(amount, paymentType, this);
        
        if (paymentType == PaymentMethod.PayByCash) {
            payment.setStatus("PaymentCompleted");
            setStatus("BookingCompleted");
        }else if (paymentType == PaymentMethod.PayByCard) {
            // Simulate card payment processing
            payment.setStatus("ConnectingWithBank");
        }




        taxiDriver.updateBooking(this);
        customer.updateBooking(this);
        rideRequest.setBooking(this);

        return payment;
    }

    public DriverRating createEvaluation( ArrayList<Double> costumerRatings, String comment, TaxiBooking booking){
       DriverRating rating = new DriverRating(this);
       rating.fillDriverRating(costumerRatings, comment);
       setDriverRating(rating);

        taxiDriver.updateBooking(this);
        customer.updateBooking(this);
        rideRequest.setBooking(this);

        return rating;
       
    }

    public void setEvaluated(boolean evaluated) { this.isEvaluated = evaluated; }
    public boolean isEvaluated() { return isEvaluated; }

}