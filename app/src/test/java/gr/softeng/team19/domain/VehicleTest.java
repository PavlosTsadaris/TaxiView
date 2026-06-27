package gr.softeng.team19.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

public class VehicleTest {
        private Vehicle vehicle;
        private TaxiDriver driver;


        @Before
        public void setUp() throws Exception{
            driver = new TaxiDriver(
                    "driverUser", "1234", "makhsaritos@email.com",
                    "Makhs", "Aritos", "123123123",
                    LocalDate.of(2005, 3, 4),
                    "3425",
                    37.9, 23.7,
                    "Akadimias", "Athens", 10, 10564,
                    "XYZ1224", "Toyota", "Corolla"
            );
            vehicle = new Vehicle("YZA1234", "Toyota", "Corolla", driver);
        }

        @Test
        public void constructor_initializesCorrectly() {
            assertNotNull(vehicle.getVehicleID()); // from GlobalID
            assertEquals("YZA1234", vehicle.getLisencePlate());
            assertEquals("Toyota", vehicle.getManufacturer());
            assertEquals("Corolla", vehicle.getModel());
            assertNotNull(vehicle.getLisencePlate());
        }

        @Test
        public void vehicleID_startsWithV() {
            assertTrue(vehicle.getVehicleID().startsWith("V"));
        }

        @Test
        public void testToString() {
            String out = vehicle.toString();

            assertTrue(out.contains("Vehicle ID"));
            assertTrue(out.contains("License"));
            assertTrue(out.contains("Toyota"));
            assertTrue(out.contains("Corolla"));
        }


    @Test
    public void setManufacturer() {
            vehicle.setManufacturer("Ford");
            assertEquals("Ford", vehicle.getManufacturer());

    }

    @Test
    public void licencePlate_isNull(){
            Vehicle vehicle2 = new Vehicle( null, "Toyota", "Corolla", driver);

            assertNull(vehicle2.getLisencePlate());
    }
}