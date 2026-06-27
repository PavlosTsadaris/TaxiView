package gr.softeng.team19.domain;

import org.osmdroid.util.GeoPoint;

import java.time.LocalDateTime;

public class GPSLocation {

    private LocalDateTime detectionDateTime;
    private Double latitude;
    private Double longitude;

    private GeoPoint point;

    public GPSLocation(Double latitude, Double longitude) {
        detectionDateTime = LocalDateTime.now();
        this.latitude = latitude;
        this.longitude = longitude;
        this.point = new GeoPoint(latitude, longitude);
    }

    public String toString() {
        return "Latitude: " + latitude + ",\n Longitude: " + longitude + ",\n Detected at: " + detectionDateTime;
    }

    public LocalDateTime getDetectionTime() {
        return detectionDateTime;
    }

    public void setDetectionTime(LocalDateTime detectionDateTime) {
        this.detectionDateTime = detectionDateTime;
    }

    public GeoPoint getPoint() {
        return point;
    }

    public void setPoint(GeoPoint point) {
        this.point = point;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
        if (this.point != null) {
            this.point.setLatitude(latitude);
        } else {
            this.point = new GeoPoint(latitude, this.longitude != null ? this.longitude : 0.0);
        }
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
        if (this.point != null) {
            this.point.setLongitude(longitude);
        } else {
            this.point = new GeoPoint(this.latitude != null ? this.latitude : 0.0, longitude);
        }
    }

    public double distanceTo(GPSLocation otherLocation) {
        final int R = 6371000; // ακτίνα Γης σε μέτρα

        double lat1 = Math.toRadians(this.latitude);
        double lon1 = Math.toRadians(this.longitude);
        double lat2 = Math.toRadians(otherLocation.getLatitude());
        double lon2 = Math.toRadians(otherLocation.getLongitude());

        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;

        double a = Math.sin(dlat / 2) * Math.sin(dlat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(dlon / 2) * Math.sin(dlon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c/1000; // km
    }
}
