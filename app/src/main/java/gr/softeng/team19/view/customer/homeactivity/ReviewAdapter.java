package gr.softeng.team19.view.customer.homeactivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gr.softeng.team19.R;
import gr.softeng.team19.domain.TaxiBooking;

/**
 * RecyclerView Adapter for displaying pending reviews.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<TaxiBooking> bookings;
    private OnBookingClickListener listener;

    /**
     * Interface for handling booking click events.
     */
    public interface OnBookingClickListener {
        /**
         * @param booking The specific booking selected by the user.
         */
        void onBookingClick(TaxiBooking booking);
    }

    /**
     * @param bookings List of bookings to display.
     * @param listener Callback for item clicks.
     */
    public ReviewAdapter(List<TaxiBooking> bookings, OnBookingClickListener listener) {
        this.bookings = bookings;
        this.listener = listener;
    }

    /**
     * Inflates the item layout and creates the ViewHolder.
     * @param parent The parent view group.
     * @param viewType The view type integer.
     * @return A new ViewHolder instance.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_evaluation, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds booking data to the UI components.
     * @param holder The ViewHolder to update.
     * @param position The index of the item in the list.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TaxiBooking booking = bookings.get(position);
        holder.name.setText(booking.getTaxiDriver().getName() + " " + booking.getTaxiDriver().getSurname());
        holder.date.setText(booking.getDate().toString());

        holder.itemView.setOnClickListener(v -> listener.onBookingClick(booking));
    }

    /**
     * @return Total number of bookings in the list.
     */
    @Override
    public int getItemCount() { return bookings.size(); }

    /**
     * Cache for view references to improve performance.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, date;

        /**
         * @param itemView The root view of the list item.
         */
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtDriverNameItem);
            date = itemView.findViewById(R.id.txtBookingDateItem);
        }
    }
}