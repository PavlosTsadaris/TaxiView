package gr.softeng.team19.view.customer.bookride.searchride;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gr.softeng.team19.R;
import gr.softeng.team19.domain.TaxiDriver;

/**
 * Adapter that creates the list of available drivers for the customer to see.
 * It shows each driver's name, car, rating, and how many minutes away they are.
 */
public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.DriverViewHolder> {

    private ArrayList<TaxiDriver> driverList;
    private OnDriverClickListener listener;
    private gr.softeng.team19.domain.GPSLocation pickupLocation;

    /**
     * Interface to handle what happens when a customer taps on a specific driver.
     */
    public interface OnDriverClickListener {
        /**
         * Triggered when the customer selects a driver from the list.
         * @param driver The taxi driver that was picked.
         */
        void onDriverClick(TaxiDriver driver);
    }

    /**
     * Constructor that sets up the list of drivers and the customer's pickup spot.
     * @param driverList     List of taxi drivers found nearby.
     * @param listener       The action to perform when a driver is clicked.
     * @param pickupLocation The spot where the customer is waiting (to calculate travel time).
     */
    public DriverAdapter(ArrayList<TaxiDriver> driverList, OnDriverClickListener listener, gr.softeng.team19.domain.GPSLocation pickupLocation) {
        this.driverList = driverList;
        this.listener = listener;
        this.pickupLocation = pickupLocation;
    }

    /**
     * Creates a new visual row for a driver in the list.
     */
    @NonNull
    @Override
    public DriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_driver, parent, false);
        return new DriverViewHolder(view);
    }

    /**
     * Fills the visual row with the driver's real information.
     * It also calculates the "Estimated Arrival Time" based on distance.
     */
    @Override
    public void onBindViewHolder(@NonNull DriverViewHolder holder, int position) {
        TaxiDriver driver = driverList.get(position);

        holder.txtName.setText(driver.getName());
        holder.txtCar.setText(driver.getVehicle().getModel());

        // Show rating with 2 decimal points
        double rating = Double.parseDouble(String.format("%.2f", driver.getAverageRating()));
        holder.txtRating.setText(String.valueOf(rating));

        // Calculate travel time: distance * 2.1 minutes per km
        if (pickupLocation != null && driver.getUserLocation() != null) {
            double distanceKm = driver.getUserLocation().distanceTo(pickupLocation);
            int minutesAway = (int) Math.max(1, Math.round(distanceKm * 2.1));
            holder.txtArrivalEstimate.setText(minutesAway + " min away");
        }

        // Set the click action for this specific row
        holder.itemView.setOnClickListener(v -> listener.onDriverClick(driver));
    }

    /**
     * Tells the app how many drivers are in the list.
     */
    @Override
    public int getItemCount() {
        return driverList.size();
    }

    /**
     * Helper class that holds the UI components (text views) for each row.
     */
    static class DriverViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtCar, txtRating, txtArrivalEstimate;

        public DriverViewHolder(@NonNull View itemView) {
            super(itemView);
            txtArrivalEstimate = itemView.findViewById(R.id.txtArrivalEstimate);
            txtName = itemView.findViewById(R.id.txtDriverName);
            txtCar = itemView.findViewById(R.id.txtCarModel);
            txtRating = itemView.findViewById(R.id.txtRating);
        }
    }
}