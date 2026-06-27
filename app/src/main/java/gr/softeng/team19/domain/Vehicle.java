package gr.softeng.team19.domain;
public class Vehicle {
    private String vehicleID;
    private String lisencePlate;
    private String manufacturer;
    private String model;
    private TaxiDriver driver;


    public Vehicle(String lisencePlate, String manufacturer, String model, TaxiDriver driver) {
        this.vehicleID = GlobalID.getNextVehicleID();
        this.lisencePlate = lisencePlate;
        this.manufacturer = manufacturer;
        this.model = model;
        this.driver = driver;
    }

    public String toString() {
        return "Vehicle ID: " + vehicleID + ",\n License Plate: " + lisencePlate + ",\n Manufacturer: " + manufacturer + ",\n Model: " + model;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getLisencePlate() {
        return lisencePlate;
    }

    public void setLisencePlate(String lisencePlate) {
        this.lisencePlate = lisencePlate;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public TaxiDriver getDriver() {
        return driver;
    }
    public void setDriver(TaxiDriver driver) {
        this.driver = driver;
    }
}
