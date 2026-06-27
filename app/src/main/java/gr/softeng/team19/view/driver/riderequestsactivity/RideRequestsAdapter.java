package gr.softeng.team19.view.driver.riderequestsactivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import gr.softeng.team19.R;
import gr.softeng.team19.domain.TaxiRideRequest;
import gr.softeng.team19.memorydao.MockRideData;

import java.util.ArrayList;

/**
 * Adapter that handles the list of ride requests.
 * It takes the data for each request (like name, destination, and distance)
 * and places it into a visual row for the driver to see.
 */
public class RideRequestsAdapter extends RecyclerView.Adapter<RideRequestsAdapter.ViewHolder> {

    private ArrayList<TaxiRideRequest> requests;
    private OnRequestClickListener listener;
    private ArrayList<Double> distances;

    /**
     * Interface to handle what happens when a driver clicks on a specific request.
     */
    public interface OnRequestClickListener {
        /**
         * Triggered when the driver taps an item in the list.
         * @param request The specific ride request that was selected.
         */
        void onClick(TaxiRideRequest request);
    }

    /**
     * Creates the adapter and sets up the data.
     * @param requests  List of available rides.
     * @param listener  Action to perform when a ride is clicked.
     * @param distances How far each pickup point is from the driver.
     */
    public RideRequestsAdapter(ArrayList<TaxiRideRequest> requests, OnRequestClickListener listener, ArrayList<Double> distances) {
        this.requests = requests;
        this.listener = listener;
        this.distances = distances;
    }

    /**
     * Creates a new visual "container" (ViewHolder) for a single list item.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ride_request, parent, false);
        return new ViewHolder(v);
    }

    /**
     * Fills a list item with information from a specific request.
     * @param holder   The visual container for the row.
     * @param position The index of the request in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaxiRideRequest req = requests.get(position);

        // Display customer info
        holder.txtName.setText("Customer: " + req.getCustomer().getName() + " " + req.getCustomer().getSurname());

        // Get the destination name and display it
        String dest = getDestination(req);
        holder.txtDest.setText("To: " + dest);

        // Show distance formatted to 1 decimal point (e.g., 2.5 km)
        holder.txtDist.setText(String.format("%.1f", distances.get(position) / 10) + " km away");

        // Set the click action for this specific row
        holder.itemView.setOnClickListener(v -> listener.onClick(req));
    }

    /**
     * Tells the list how many items to display.
     * @return The number of requests in the list.
     */
    @Override
    public int getItemCount() {
        return requests.size();
    }

    /**
     * Updates the list of requests with new data.
     */
    public void setRequests(ArrayList<TaxiRideRequest> requests) {
        this.requests = new ArrayList<>(requests);
    }

    /**
     * Converts GPS coordinates into a human-readable destination name.
     * @param request The ride request.
     * @return The name of the destination or "Unknown".
     */
    private String getDestination(TaxiRideRequest request) {
        MockRideData.DemoLocation loc = null;
        for (MockRideData.DemoLocation l : MockRideData.LOCATIONS) {
            if (l.point.getLatitude() == request.getDestination().getPoint().getLatitude()) {
                loc = l;
                break;
            }
        }
        if (loc == null) return "Unknown";
        return loc.name;
    }

    /**
     * Class that holds the UI components for a single row in the list.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtDest, txtRating, txtDist;

        /**
         * Finds the UI elements inside the layout file for the item.
         */
        public ViewHolder(View v) {
            super(v);
            txtName = v.findViewById(R.id.txtCustomerName);
            txtDest = v.findViewById(R.id.txtDestination);
            txtRating = v.findViewById(R.id.txtCustomerRating);
            txtDist = v.findViewById(R.id.txtDistance);
        }
    }

    /**
     * Updates the list of distances shown to the driver.
     */
    public void setDistance(ArrayList<Double> distances) {
        this.distances = distances;
    }
}