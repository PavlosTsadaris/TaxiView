package gr.softeng.team19.memorydao;

import org.osmdroid.util.GeoPoint;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import gr.softeng.team19.domain.Address;
import gr.softeng.team19.domain.Customer;
import gr.softeng.team19.domain.DriverRating;
import gr.softeng.team19.domain.GPSLocation;
import gr.softeng.team19.domain.Payment;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.domain.TaxiDriver;
import gr.softeng.team19.domain.TaxiRideRequest;

/**
 * Utility class that populates the application with initial mock data.
 */
public abstract class MemoryInitializer {

    private static boolean initialized = false;
    private static final Random random = new Random();

    /**
     * Inner class representing a simple coordinate pair for driver spawning.
     */
    private static class InitLocation {
        double lat, lon;

        public InitLocation(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }
    }

    /** List of main locations used to group customers and drivers. */
    private static final List<InitLocation> TARGET_LOCATIONS = new ArrayList<>();

    static {
        TARGET_LOCATIONS.add(new InitLocation(37.9755, 23.7348)); // Syntagma
        TARGET_LOCATIONS.add(new InitLocation(38.0320, 23.7915)); // Golden Hall
        TARGET_LOCATIONS.add(new InitLocation(37.9356, 23.9484)); // Airport
        TARGET_LOCATIONS.add(new InitLocation(37.9386, 23.6925)); // Niarchos
        TARGET_LOCATIONS.add(new InitLocation(37.9429, 23.6469)); // Piraeus
        TARGET_LOCATIONS.add(new InitLocation(38.0263, 23.7876)); // Hygeia
        TARGET_LOCATIONS.add(new InitLocation(37.9684, 23.7285)); // Acropolis
        TARGET_LOCATIONS.add(new InitLocation(37.9783, 23.7138)); // Gazi
        TARGET_LOCATIONS.add(new InitLocation(38.0361, 23.7876)); // OAKA
        TARGET_LOCATIONS.add(new InitLocation(37.9683, 23.7668)); // Zografou
    }

    private static final String[] FIRST_NAMES = {"Giannis", "Maria", "Kostas", "Eleni", "Dimitris", "Sofia", "Nikos", "Katerina", "Giorgos", "Vaso"};
    private static final String[] LAST_NAMES = {"Papadopoulos", "Georgiou", "Nikolaou", "Makris", "Dimitriou", "Ioannou", "Konstantinou", "Vlachou"};
    private static final String[] CAR_BRANDS = {"Mercedes", "Toyota", "Skoda", "Nissan", "Tesla", "VW", "Peugeot"};
    private static final String[] CAR_MODELS = {"E-Class", "Corolla", "Octavia", "Qashqai", "Model 3", "Passat", "308"};

    /**
     * Main method to prepare all mock data for the in-memory database.
     */
    public static void prepareData() {
        if (initialized) return;

        UserDAOMemory userDAO = new UserDAOMemory();
        CustomerDAOMemory customerDAO = new CustomerDAOMemory();
        TaxiDriverDAOMemory driverDAO = new TaxiDriverDAOMemory();

        LocalDate birthDate = LocalDate.of(1990, 5, 15);

        // --- 1. Create Mock Customers at specific landmarks ---
        Customer c1 = new Customer("d", "d", "d@test.com", "Dimitris", "Test", "6900000000", birthDate, "1234123412341234", TARGET_LOCATIONS.get(0).lat, TARGET_LOCATIONS.get(0).lon, "Ermou", "Athens", 10, 10563);
        Customer c2 = new Customer("maria", "1234", "maria@test.com", "Maria", "Papadopoulou", "6911111111", birthDate, "1111222233334444", TARGET_LOCATIONS.get(1).lat, TARGET_LOCATIONS.get(1).lon, "Kifisias", "Marousi", 37, 15123);
        Customer c3 = new Customer("kostas", "1234", "kostas@test.com", "Kostas", "Nikolaou", "6922222222", birthDate, "5555666677778888", TARGET_LOCATIONS.get(2).lat, TARGET_LOCATIONS.get(2).lon, "Attiki Odos", "Spata", 1, 19019);
        Customer c4 = new Customer("eleni", "1234", "eleni@test.com", "Eleni", "Georgiou", "6933333333", birthDate, "9999888877776666", TARGET_LOCATIONS.get(3).lat, TARGET_LOCATIONS.get(3).lon, "Leof. Siggrou", "Kallithea", 364, 17674);
        Customer c5 = new Customer("giannis", "1234", "giannis@test.com", "Giannis", "Antoniou", "6944444444", birthDate, "1234567890123456", TARGET_LOCATIONS.get(4).lat, TARGET_LOCATIONS.get(4).lon, "Akti Miaouli", "Piraeus", 10, 18535);
        Customer c6 = new Customer("katerina", "1234", "katerina@test.com", "Katerina", "Dimitriou", "6955555555", birthDate, "6543210987654321", TARGET_LOCATIONS.get(5).lat, TARGET_LOCATIONS.get(5).lon, "Kifisias", "Marousi", 56, 15123);
        Customer c7 = new Customer("nikos", "1234", "nikos@test.com", "Nikos", "Makris", "6966666666", birthDate, "1122334455667788", TARGET_LOCATIONS.get(6).lat, TARGET_LOCATIONS.get(6).lon, "Dionysiou Areopagitou", "Athens", 15, 11742);
        Customer c8 = new Customer("sofia", "1234", "sofia@test.com", "Sofia", "Lymperopoulou", "6977777777", birthDate, "8877665544332211", TARGET_LOCATIONS.get(7).lat, TARGET_LOCATIONS.get(7).lon, "Iakchou", "Gazi", 22, 11854);
        Customer c9 = new Customer("alex", "1234", "alex@test.com", "Alexandros", "Raptis", "6988888888", birthDate, "0000111122223333", TARGET_LOCATIONS.get(8).lat, TARGET_LOCATIONS.get(8).lon, "Nerantziotissis", "Marousi", 1, 15122);
        Customer c10 = new Customer("vasiliki", "1234", "vasiliki@test.com", "Vasiliki", "Stergiou", "6999999999", birthDate, "4444555566667777", TARGET_LOCATIONS.get(9).lat, TARGET_LOCATIONS.get(9).lon, "Iroon Polytechneiou", "Zografou", 9, 15773);

        Customer[] customers = {c1, c2, c3, c4, c5, c6, c7, c8, c9, c10};
        for (Customer c : customers) {
            userDAO.add(c);
            customerDAO.save(c);
        }

        // --- 2. Generate Drivers within 1.5 - 3.5 km of landmarks ---
        int driverCount = 1;
        for (InitLocation loc : TARGET_LOCATIONS) {
            for (int i = 0; i < 3; i++) {
                double radiusKm = 1.5 + (random.nextDouble() * 2.0);
                double angleRad = random.nextDouble() * 2 * Math.PI;
                double dy = radiusKm / 111.0;
                double dx = radiusKm / (111.0 * Math.cos(Math.toRadians(loc.lat)));

                TaxiDriver driver = new TaxiDriver(
                        "driver" + driverCount, "1234", "driver" + driverCount + "@test.com",
                        FIRST_NAMES[random.nextInt(FIRST_NAMES.length)], LAST_NAMES[random.nextInt(LAST_NAMES.length)],
                        "69" + String.format("%08d", driverCount), birthDate, "IBAN" + driverCount,
                        loc.lat + (dy * Math.sin(angleRad)), loc.lon + (dx * Math.cos(angleRad)),
                        "Street " + driverCount, "Area", 10, 10000, "TAB-" + (1000 + driverCount),
                        CAR_BRANDS[random.nextInt(CAR_BRANDS.length)], CAR_MODELS[random.nextInt(CAR_MODELS.length)]
                );
                userDAO.add(driver);
                driverDAO.save(driver);
                driverCount++;
            }
        }

        TaxiRideRequestDAOMemory taxiRideRequestDAO = new TaxiRideRequestDAOMemory();
        TaxiBookingDAOMemory taxiBookingDAO = new TaxiBookingDAOMemory();
        DriverRatingDAOMemory driverRatingDAO = new DriverRatingDAOMemory();

        // --- 3. Create 3 types of requests for every customer (Pending & Denied) ---
        List<Customer> allCustomers = customerDAO.findAll();
        int k = random.nextInt(MockRideData.LOCATIONS.size());
        for (Customer customer : allCustomers) {
            for (int i = 0; i < 3; i++) {
                // Random start location that is not the same as the destination
                int startIdx = random.nextInt(MockRideData.LOCATIONS.size());
                GPSLocation currentStart= new GPSLocation(MockRideData.LOCATIONS.get(startIdx).point.getLatitude(), MockRideData.LOCATIONS.get(startIdx).point.getLongitude());

                // Random destination that is not the same as the start location
                int destIdx = random.nextInt(MockRideData.LOCATIONS.size());
                if (destIdx == startIdx) destIdx = (destIdx + 1) % MockRideData.LOCATIONS.size();
                GPSLocation currentDest = new GPSLocation(MockRideData.LOCATIONS.get(destIdx).point.getLatitude(), MockRideData.LOCATIONS.get(destIdx).point.getLongitude());
                Address testAddress = new Address("Street", "Area", 10, 10000);
                testAddress.setPoint(currentDest.getPoint());
                // Create the request
                TaxiRideRequest request = new TaxiRideRequest(currentStart, testAddress, customer);

                if (i > 0) request.setStatus("DENIED");
                taxiRideRequestDAO.save(request);
            }
        }

        // --- 4. Generate random Booking History for each driver ---
        List<TaxiDriver> allDrivers = driverDAO.findAll();
        for (TaxiDriver driver : allDrivers) {
            int pastBookingsCount = random.nextInt(3) + 1;
            ArrayList<Customer> fakeCustomers = new ArrayList<>(allCustomers);

            for (int j = 0; j < pastBookingsCount; j++) {
                Customer randomCustomer = fakeCustomers.remove(random.nextInt(fakeCustomers.size()));
                int startIdx = random.nextInt(MockRideData.LOCATIONS.size());
                GPSLocation currentStart= new GPSLocation(MockRideData.LOCATIONS.get(startIdx).point.getLatitude(), MockRideData.LOCATIONS.get(startIdx).point.getLongitude());

                // Random destination that is not the same as the start location
                int destIdx = random.nextInt(MockRideData.LOCATIONS.size());
                if (destIdx == startIdx) destIdx = (destIdx + 1) % MockRideData.LOCATIONS.size();
                GPSLocation currentDest = new GPSLocation(MockRideData.LOCATIONS.get(destIdx).point.getLatitude(), MockRideData.LOCATIONS.get(destIdx).point.getLongitude());
                Address testAddress = new Address("Street", "Area", 10, 10000);
                testAddress.setPoint(currentDest.getPoint());
                // Create the request
                TaxiRideRequest request = new TaxiRideRequest(currentStart, testAddress, randomCustomer);

                request.setStatus("ACCEPTED");

                TaxiBooking oldBooking = new TaxiBooking(request.getPickupPoint(), request.getDestination(), randomCustomer, driver, request);
                oldBooking.payment = oldBooking.payRoute(Math.round((10 + Math.random() * 10) * 100.0) / 100.0, Payment.PaymentMethod.PayByCash);
                oldBooking.setStatus("BookingCompleted");
                oldBooking.setEvaluated(true);

                taxiRideRequestDAO.save(request);
                taxiBookingDAO.save(oldBooking);

                // --- 5. Generate random Evaluations/Ratings ---
                ArrayList<Double> ratings = new ArrayList<>();
                for (int w = 0; w < 3; w++) ratings.add(Math.round((3.0 + 2.0 * random.nextDouble()) * 10.0) / 10.0);
                String[] randomComments = {"Excellent!", "Good ride", "Average", "Clean car", "Very polite driver"};
                driverRatingDAO.save(oldBooking.createEvaluation(ratings, randomComments[random.nextInt(randomComments.length)], oldBooking));
            }
        }

        initialized = true;
    }
}