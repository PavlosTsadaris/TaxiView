package gr.softeng.team19.memorydao;

import org.osmdroid.util.GeoPoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import gr.softeng.team19.domain.TaxiDriver;

/**
 * Utility class providing mock location data for testing and demonstration.
 */
public class MockRideData {

    /** List of static landmark locations in Athens. */
    public static final List<DemoLocation> LOCATIONS = new ArrayList<>();

    static {
        LOCATIONS.add(new DemoLocation("Syntagma Square", 37.9755, 23.7348));
        LOCATIONS.add(new DemoLocation("Golden Hall (Marousi)", 38.0320, 23.7915));
        LOCATIONS.add(new DemoLocation("Athens Airport (El. Venizelos)", 37.9356, 23.9484));
        LOCATIONS.add(new DemoLocation("Stavros Niarchos Foundation", 37.9386, 23.6925));
        LOCATIONS.add(new DemoLocation("Piraeus Port", 37.9429, 23.6469));
        LOCATIONS.add(new DemoLocation("Hygeia Hospital", 38.0263, 23.7876));
        LOCATIONS.add(new DemoLocation("Acropolis Museum", 37.9684, 23.7285));
        LOCATIONS.add(new DemoLocation("Technopolis (Gazi)", 37.9783, 23.7138));
        LOCATIONS.add(new DemoLocation("OAKA Stadium", 38.0361, 23.7876));
        LOCATIONS.add(new DemoLocation("University Campus (Zografou)", 37.9683, 23.7668));
    }

    /**
     * Inner class representing a mock geographic location.
     */
    public static class DemoLocation {
        public String name;
        public GeoPoint point;

        /**
         * @param name The display name of the location.
         * @param lat Geographic latitude.
         * @param lon Geographic longitude.
         */
        public DemoLocation(String name, double lat, double lon) {
            this.name = name;
            this.point = new GeoPoint(lat, lon);
        }

        /**
         * @return The location name as a string.
         */
        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Generates randomized starting locations for drivers based on static landmarks.
     * @return A list of randomized DemoLocation objects.
     */
    public static List<DemoLocation> getDriverStartingLocations() {
        List<DemoLocation> driverLocs = new ArrayList<>();
        java.util.Random random = new java.util.Random();

        for (DemoLocation loc : LOCATIONS) {
            double distanceKm = 1.0 + (random.nextDouble() * 1.9);
            double angle = random.nextDouble() * 2 * Math.PI;

            double latOffset = (distanceKm * Math.sin(angle)) / 111.0;
            double lonOffset = (distanceKm * Math.cos(angle)) / (111.0 * 0.78);

            driverLocs.add(new DemoLocation(
                    "Near " + loc.name,
                    loc.point.getLatitude() + latOffset,
                    loc.point.getLongitude() + lonOffset
            ));
        }
        return driverLocs;
    }
}