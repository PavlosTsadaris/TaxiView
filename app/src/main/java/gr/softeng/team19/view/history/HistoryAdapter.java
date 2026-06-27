package gr.softeng.team19.view.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;
import gr.softeng.team19.R;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.memorydao.MockRideData;

/**
 * Adapter class that manages the list of past trips in the history screen.
 * It takes TaxiBooking objects and turns them into visual cards for the user to see.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<TaxiBooking> bookingList;

    /**
     * Constructor that initializes the adapter with the user's trip data.
     * @param bookingList A list containing all previous completed or canceled trips.
     */
    public HistoryAdapter(List<TaxiBooking> bookingList) {
        this.bookingList = bookingList;
    }

    /**
     * Creates the visual container (ViewHolder) for a single trip item.
     * @param parent The group that will hold the trip item view.
     * @param viewType The type of view (standard for this list).
     * @return A new HistoryViewHolder containing the layout.
     */
    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ride_history, parent, false);
        return new HistoryViewHolder(view);
    }

    /**
     * Connects the data of a specific trip to a specific row in the list.
     * @param holder The ViewHolder that should be updated with trip details.
     * @param position The position of the trip in the data list.
     */
    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        TaxiBooking booking = bookingList.get(position);
        holder.bind(booking);
    }

    /**
     * Returns the total number of trips in the history list.
     * @return The total count of booking items.
     */
    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    /**
     * Class that finds and holds the UI components (text, labels) for one trip row.
     */
    static class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate, txtPrice, txtPickup, txtDestination, txtStatusBadge;

        /**
         * Constructor that links Java variables to the XML layout IDs.
         * @param itemView The view of a single row in the list.
         */
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtPickup = itemView.findViewById(R.id.txtPickup);
            txtDestination = itemView.findViewById(R.id.txtDestination);
            txtStatusBadge = itemView.findViewById(R.id.txtStatusBadge);
        }

        /**
         * Fills the trip row with actual data like names, prices, and dates.
         * It also changes colors based on whether the ride was finished or canceled.
         * @param booking The specific trip data to display.
         */
        public void bind(TaxiBooking booking) {
            // Convert GPS coordinates to friendly place names
            MockRideData.DemoLocation pickupLoc = findLocation(booking.getPickupPoint().getLatitude());
            txtPickup.setText(pickupLoc != null ? pickupLoc.name : "Unknown");

            MockRideData.DemoLocation destLoc = findLocation(booking.getDestination().getPoint().getLatitude());
            txtDestination.setText(destLoc != null ? destLoc.name : "Unknown");

            // Check if trip was paid or canceled to set colors and status
            double price;
            if (booking.payment == null) {
                // Trip was canceled: show red badge and 0 cost
                txtStatusBadge.setText("Canceled");
                txtStatusBadge.setTextColor(android.graphics.Color.parseColor("#D32F2F"));
                txtStatusBadge.setBackgroundResource(R.drawable.badge_canceled);
                price = 0.0;
            } else {
                // Trip was completed: show green badge and the real price
                price = booking.payment.getAmount();
                txtStatusBadge.setText("Completed");
                txtStatusBadge.setTextColor(android.graphics.Color.parseColor("#1B664D"));
                txtStatusBadge.setBackgroundResource(R.drawable.badge_completed);
            }

            // Format price with Euro symbol (e.g., € 12.50)
            txtPrice.setText(String.format(Locale.US, "€ %.2f", price));

            // Format trip date (e.g., 12 Jan 2024)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                java.time.format.DateTimeFormatter formatter =
                        java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.US);
                txtDate.setText(booking.getDate().format(formatter));
            }
        }

        /**
         * Helper method to search the mock database for a location name using latitude.
         * @param lat The latitude coordinate.
         * @return The location object if found, otherwise null.
         */
        private MockRideData.DemoLocation findLocation(double lat) {
            for (MockRideData.DemoLocation loc : MockRideData.LOCATIONS) {
                if (Math.abs(loc.point.getLatitude() - lat) < 0.0001) return loc;
            }
            return null;
        }
    }
}