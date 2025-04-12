package com.example.travel_app.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Data.Model.BookingFlight;
import com.example.travel_app.Data.Model.BookingItem;
import com.example.travel_app.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookingAdapter extends ListAdapter<BookingItem, BookingAdapter.BookingViewHolder> {
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(BookingItem booking);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public BookingAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<BookingItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<BookingItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull BookingItem oldItem, @NonNull BookingItem newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull BookingItem oldItem, @NonNull BookingItem newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        BookingItem booking = getItem(position);

        holder.txtName.setText(booking.getName());
        holder.txtRoute.setText(booking.getRoute());
        holder.txtDate.setText(booking.getDate());
        holder.txtPrice.setText(String.format("%,.0f VND", booking.getTotalAmount()));

        String imageUrl = booking.getImageUrl();
        Picasso.get().load(imageUrl).into(holder.imgBooking);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(booking);
            }
        });
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBooking;
        TextView txtName, txtRoute, txtDate, txtPrice;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBooking = itemView.findViewById(R.id.img_booking);
            txtName = itemView.findViewById(R.id.txt_name);
            txtRoute = itemView.findViewById(R.id.txt_route);
            txtDate = itemView.findViewById(R.id.txt_date);
            txtPrice = itemView.findViewById(R.id.txt_price);
        }
    }
}