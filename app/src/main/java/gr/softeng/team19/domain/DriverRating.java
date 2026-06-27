package gr.softeng.team19.domain;
import java.util.ArrayList;

public class DriverRating {
    private ArrayList<Double> serviceRatings;
    private Double averageRating;
    private String customerComment;
    private String ratingID;

    private TaxiBooking booking;

    public DriverRating(TaxiBooking booking) {
        this.serviceRatings = new ArrayList<>();
        this.averageRating = 0.0;
        this.customerComment = "";
        this.ratingID = GlobalID.getNextRatingID();
        this.booking = booking;
    }

    public String getRatingID() {
        return ratingID;
    }

    public void setRatingID(String ratingID) {
        this.ratingID = ratingID;
    }

    public TaxiBooking getBooking() {
        return booking;
    }

    public void setBooking(TaxiBooking booking) {
        this.booking = booking;
    }

    public ArrayList<Double> getServiceRatings() {
        return serviceRatings;
    }

    public Boolean addServiceRating(Double rating) {

        if (rating != null && rating >= 0.0 && rating <= 5.0) {
            this.serviceRatings.add(rating);
            updateAverageRating();
            return true;
        }
        return false;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    private void updateAverageRating() {
        if (serviceRatings.isEmpty()) {
            this.averageRating = 0.0;
            return;
        }
        double sum = 0.0;
        for (Double rating : serviceRatings) {
            sum += rating;
        }
        this.averageRating = Math.round((sum / serviceRatings.size()) * 100.0) / 100.0;
    }

    public String getCustomerComment() {
        return customerComment;
    }

    public void setCustomerComment(String customerComment) {
        this.customerComment = customerComment;
    }

    public void fillDriverRating(ArrayList<Double> customerRatings, String comment) {
        if (customerRatings != null && !customerRatings.isEmpty()) {
            for (Double rating : customerRatings) {
                addServiceRating(rating);
            }
        }
        setCustomerComment(comment);
        updateAverageRating();
    }
}

