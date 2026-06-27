package gr.softeng.team19.view.ratings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import gr.softeng.team19.R;
import gr.softeng.team19.domain.TaxiBooking;
import gr.softeng.team19.memorydao.MockRideData;

/**
 * Adapter that connects the list of rated bookings with the UI.
 * It manages how each rating item is created and displayed in the list.
 */
public class RatingsAdapter extends RecyclerView.Adapter<RatingsAdapter.RatingsViewHolder> {

    private List<TaxiBooking> ratedBookings;

    /**
     * Constructor that sets up the adapter with a list of rated bookings.
     * @param ratedBookings The list of bookings that have ratings.
     */
    public RatingsAdapter(List<TaxiBooking> ratedBookings) {
        this.ratedBookings = ratedBookings;
    }

    /**
     * Creates a new visual item (ViewHolder) for the list.
     * @param parent The group that will hold the new view.
     * @param viewType The type of the view.
     * @return A new RatingsViewHolder containing the item layout.
     */
    @NonNull
    @Override
    public RatingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rating, parent, false);
        return new RatingsViewHolder(view);
    }

    /**
     * Connects data from a specific booking to a visual item in the list.
     * @param holder The ViewHolder to update with data.
     * @param position The index of the booking in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull RatingsViewHolder holder, int position) {
        holder.bind(ratedBookings.get(position));
    }

    /**
     * Tells the list how many items to display.
     * @return The total number of rated bookings.
     */
    @Override
    public int getItemCount() {
        return ratedBookings.size();
    }

    /**
     * Class that holds and manages the UI elements for a single rating item.
     */
    static class RatingsViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtComment, txtPickup, txtDestination, txtBookingID, txtNumericalRating;
        RatingBar ratingBar;

        /**
         * Constructor that finds UI components by their ID.
         * @param itemView The view of a single list item.
         */
        public RatingsViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBookingID = itemView.findViewById(R.id.txtBookingID);
            txtDate = itemView.findViewById(R.id.txtRatingDate);
            txtComment = itemView.findViewById(R.id.txtRatingComment);
            txtPickup = itemView.findViewById(R.id.txtPickup);
            txtDestination = itemView.findViewById(R.id.txtDestination);
            ratingBar = itemView.findViewById(R.id.ratingBarStars);
            txtNumericalRating = itemView.findViewById(R.id.txtNumericRating);
        }

        /**
         * Fills the UI components with data from a specific booking.
         * @param booking The booking object containing coordinates, date, and rating.
         */
        public void bind(TaxiBooking booking) {
            // Set the ID text
            txtBookingID.setText("Booking #" + booking.getBookingID());

            // Convert coordinates to location names
            MockRideData.DemoLocation pickupLoc = findLocation(booking.getPickupPoint().getLatitude());
            txtPickup.setText(pickupLoc != null ? pickupLoc.name : "Unknown Pickup");

            MockRideData.DemoLocation destLoc = findLocation(booking.getDestination().getPoint().getLatitude());
            txtDestination.setText(destLoc != null ? destLoc.name : "Unknown Destination");

            // Format the date
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.US);
                txtDate.setText(booking.getDate().format(formatter));
            }

            // Set the rating stars and the customer comment
            if (booking.isEvaluated() && booking.getDriverRating() != null) {
                double originalRating = booking.getDriverRating().getAverageRating();
                float displayRating;

                // Rounding logic for stars: if 4.7, show as 4.5
                if (originalRating % 1 != 0) {
                    displayRating = (float) Math.floor(originalRating) + 0.5f;
                } else {
                    displayRating = (float) originalRating;
                }

                ratingBar.setRating(displayRating);
                txtComment.setText(booking.getDriverRating().getCustomerComment());
                txtNumericalRating.setText("("+String.format("%.1f", originalRating) + ")");
            }
        }

        /**
         * Helper method to find a location name from its latitude.
         * @param lat The latitude to search for.
         * @return The location details if found, or null if not.
         */
        private MockRideData.DemoLocation findLocation(double lat) {
            for (MockRideData.DemoLocation loc : MockRideData.LOCATIONS) {
                if (Math.abs(loc.point.getLatitude() - lat) < 0.0001) return loc;
            }
            return null;
        }
    }
}