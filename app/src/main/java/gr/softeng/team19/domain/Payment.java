package gr.softeng.team19.domain;

import java.time.LocalDateTime;

public class Payment {
    private String paymentID;
    private Double amount;
    private LocalDateTime dateTime;
    private PaymentMethod paymentType;
    private String status;
    private TaxiBooking booking;


    public Payment(Double amount, PaymentMethod type, TaxiBooking booking) {
        this.paymentID = GlobalID.getNextPaymentID();
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }
        this.amount = amount;
        this.dateTime = LocalDateTime.now();
        this.paymentType = type;
        this.status = "Pending";
        this.booking = booking;
    }
    
    public String toString() {
        return "Payment ID: " + paymentID + ",\n Amount: " + amount + ",\n DateTime: " + dateTime + ",\n Payment Type: " + paymentType + ",\n Status: " + status;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public PaymentMethod getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentMethod type) {
        this.paymentType = type;
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

    public enum PaymentMethod {
        
        PayByCard,
        PayByCash
    }

    
}
