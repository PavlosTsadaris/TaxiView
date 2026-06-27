package gr.softeng.team19.domain;

public class GlobalID {
    private static int userID = 1;
    private static int driverID = 1;
    private static int taxiRideID = 1;
    private static int vehicleID = 1;
    private static int bookingID = 1;
    private static int routeID = 1;
    private static int paymentID = 1;
    private static int ratingID = 1;
    private static int documentID = 1;

    public static String getNextUserID() {
        return "C" + (++userID);
    }
    public static String getNextDriverID() {
        return "D" + (++driverID);
    }
    public static String getNextTaxiRideID() {
        return "T" + (++taxiRideID);
    }
    public static String getNextVehicleID() {
        return "V" + (++vehicleID);
    }
    public static String getNextBookingID() {
        return "B" + (++bookingID);
    }
    public static String getNextRouteID() {
        return "R" + (++routeID);
    }
    public static String getNextPaymentID() {
        return "P" + (++paymentID);
    }
    public static String getNextRatingID() {
        return "RA" + (++ratingID);
    }
    public static String getNextDocumentID() {
        return "D" + (++documentID);
    }
}
