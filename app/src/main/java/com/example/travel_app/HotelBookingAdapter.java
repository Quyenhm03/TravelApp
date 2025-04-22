package com.example.travel_app.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Data.Model.BookingHotel;
import com.example.travel_app.R;

import java.util.List;

public class HotelBookingAdapter extends RecyclerView.Adapter<HotelBookingAdapter.BookingViewHolder> {

    private List<BookingHotel> bookingList;
    private Context context;

    public HotelBookingAdapter(Context context, List<BookingHotel> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        BookingHotel booking = bookingList.get(position);

        Log.d("HotelBookingAdapter", "Binding booking: " + booking.toString());
        // Set hotel name
        holder.tvHotelName.setText(booking.getHotelName());

        // Set dates
        String dates = "Ngày nhận: " + booking.getCheckInDate() + "\nNgày trả: " + booking.getCheckOutDate();
        Log.d("HotelBookingAdapter", "Dates: " + dates);
        holder.tvDates.setText(dates);

        // Set price
        @SuppressLint("DefaultLocale") String price = "Giá: " + String.format("%,d", booking.getTotalAmount()) + " VNĐ";
        holder.tvPrice.setText(price);
        Log.d("HotelBookingAdapter", "Price: " + price);

        // Set status and color
        holder.tvStatus.setText(booking.getStatus());
        if (booking.getStatus().equals("Đã xác nhận")) {
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
        } else if (booking.getStatus().equals("Đã hủy")) {
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
        } else {
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_orange_dark));
        }
    }

    @Override
    public int getItemCount() {
        return bookingList != null ? bookingList.size() : 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateBookings(List<BookingHotel> newBookings) {
        this.bookingList = newBookings;
        notifyDataSetChanged();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvHotelName, tvDates, tvPrice, tvStatus;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHotelName = itemView.findViewById(R.id.tvHotelName);
            tvDates = itemView.findViewById(R.id.tvDates);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}