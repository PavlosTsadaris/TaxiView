package gr.softeng.team19.domain;

import java.time.LocalTime;
import java.time.Duration;

public class Route {
    private String routeID;
    private Double totalDistance;
    private LocalTime startTime;
    private LocalTime endTime;
    private Double cost;
    private GPSLocation pickupPoint;
    private Double totalTime;

    private TaxiBooking booking;

    


    public Route(GPSLocation pickupPoint, TaxiBooking booking) {
        this.routeID = GlobalID.getNextRouteID();
        this.startTime = LocalTime.now();
        this.pickupPoint = pickupPoint;
        this.booking = booking;
     

    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public GPSLocation getPickupPoint() {
        return pickupPoint;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Double totalDistance){
        this.totalDistance = totalDistance;
    }

    public Double findTotalDistance(Double latitude, Double longitude) {
        GPSLocation destination = new GPSLocation(latitude,longitude);
        return destination.distanceTo(pickupPoint) ;
    }
    
    public Double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Double totalTime){
        this.totalTime = totalTime;
    }

    public Double findTotalTime(LocalTime endTime, LocalTime startTime){
        Duration duration = Duration.between(startTime, endTime);
        return (double) duration.toMinutes();
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public TaxiBooking getBooking() {
        return booking;
    }

    public void setBooking(TaxiBooking Booking) {
        this.booking = Booking;
    }


    public double findRouteCost(LocalTime endTime, Double latitude, Double longitude){
        setEndTime(endTime);
        setTotalTime(findTotalTime(endTime, startTime));
        setTotalDistance(findTotalDistance(latitude, longitude));

        return calculateCost(totalTime, totalDistance);
    }



    public Double calculateCost(double totalTime, double totalDistance){
        double baseFare = 3.0;   // Starting fee
        double costPerKm = 1.5;  // Price per kilometer
        double costPerMin = 0.5; // Price per minute

        // Calculate total based on time and distance
        double totalCost = baseFare + (totalTime * costPerMin) + (totalDistance * costPerKm);

        // Apply minimum fare rule
        if (totalCost < 5.2) {
            totalCost = 5.2;
        }

        setCost(totalCost);

        return totalCost;
    }

}
