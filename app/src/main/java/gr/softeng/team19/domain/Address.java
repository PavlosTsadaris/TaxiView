package gr.softeng.team19.domain;

import org.osmdroid.util.GeoPoint;

public class Address {
    private String street;
    private String city;
    private Integer streetNumber;
    private Integer postalCode;
    private GeoPoint point;


    public Address(String street, String city, Integer streetNumber, Integer postalCode) {
        this.street = street;
        this.city = city;
        this.streetNumber = streetNumber;
        this.postalCode = postalCode;
    }

    public String toString() {
        return street + " " + streetNumber + ", " + city + " " + postalCode;
    }

    public GeoPoint getPoint() {
        if(point != null){
            return point;
        }else{
            return new GeoPoint(0,0);
        }
    }

    public void setPoint(GeoPoint point) {
        this.point = point;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(Integer streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }


}