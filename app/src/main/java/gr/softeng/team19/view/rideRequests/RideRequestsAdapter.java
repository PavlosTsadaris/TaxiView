package gr.softeng.team19.view.rideRequests;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;
import gr.softeng.team19.R;
import gr.softeng.team19.domain.TaxiRideRequest;
import gr.softeng.team19.memorydao.MockRideData;

/**
 * Adapter class that connects the list of ride requests with the RecyclerView UI.
 * It handles how each request item looks and what data it shows.
 */
public class RideRequestsAdapter extends RecyclerView.Adapter<RideRequestsAdapter.RequestViewHolder> {

    private List<TaxiRideRequest> requestList;

    /**
     * Constructor that initializes the adapter with a list of requests.
     * @param requestList The list of ride requests to display.
     */
    public RideRequestsAdapter(List<TaxiRideRequest> requestList) {
        this.requestList = requestList;
    }

    /**
     * Creates a new visual row (ViewHolder) for the list.
     * @param parent The ViewGroup into which the new view will be added.
     * @param viewType The view type of the new View.
     * @return A new RequestViewHolder that holds the view for each list item.
     */
    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ride_history, parent, false);
        return new RequestViewHolder(view);
    }

    /**
     * Connects the data of a specific request to a specific row in the list.
     * @param holder The ViewHolder which should be updated.
     * @param position The position of the item within the list.
     */
    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        holder.bind(requestList.get(position));
    }

    /**
     * Returns the total number of items in the request list.
     * @return The size of the request list.
     */
    @Override
    public int getItemCount() {
        return requestList.size();
    }

    /**
     * Class that holds the UI elements for a single request item in the list.
     */
    static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtPrice, txtPickup, txtDestination, txtStatusBadge;

        /**
         * Constructor that finds the UI components by their ID.
         * @param itemView The view of a single list item.
         */
        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtPickup = itemView.findViewById(R.id.txtPickup);
            txtDestination = itemView.findViewById(R.id.txtDestination);
            txtStatusBadge = itemView.findViewById(R.id.txtStatusBadge);
        }

        /**
         * Takes a request object and fills the UI text fields and colors.
         * @param request The TaxiRideRequest object containing the data to show.
         */
        public void bind(TaxiRideRequest request) {
            // Find location names from coordinates
            for (MockRideData.DemoLocation loc : MockRideData.LOCATIONS) {
                if (loc.point.getLatitude() == request.getPickupPoint().getLatitude()) {
                    txtPickup.setText(loc.name);
                    break;
                }
            }
            for (MockRideData.DemoLocation loc : MockRideData.LOCATIONS) {
                if (loc.point.getLatitude() == request.getDestination().getPoint().getLatitude()) {
                    txtDestination.setText(loc.name);
                    break;
                }
            }

            // Set the status text and hide price
            String status = request.getStatus() != null ? request.getStatus().toUpperCase().trim() : "PENDING";
            txtStatusBadge.setText(status);
            txtPrice.setVisibility(View.GONE);

            // Set colors based on the status
            switch (status) {
                case "ACCEPTED":
                    txtStatusBadge.setTextColor(android.graphics.Color.parseColor("#1B664D"));
                    txtStatusBadge.setBackgroundResource(R.drawable.badge_completed_completed);
                    break;
                case "DENIED":
                case "REJECTED":
                    txtStatusBadge.setTextColor(android.graphics.Color.parseColor("#D32F2F"));
                    txtStatusBadge.setBackgroundResource(R.drawable.badge_completed_canceled);
                    break;
                case "PENDING":
                default:
                    txtStatusBadge.setTextColor(android.graphics.Color.parseColor("#FFA000"));
                    txtStatusBadge.setBackgroundResource(R.drawable.badge_completed_pending);
                    break;
            }

            // Format and set the date
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                java.time.format.DateTimeFormatter formatter =
                        java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.US);
                txtDate.setText(request.getDate().format(formatter));
            }
        }
    }
}