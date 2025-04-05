package com.example.travel_app.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Data.Model.Seat;
import com.example.travel_app.R;

import java.util.ArrayList;
import java.util.List;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {
    private List<Seat> seatList;
    private OnSeatClickListener listener;
    private List<String> selectedSeats = new ArrayList<>();
    private int seatCount;

    public interface OnSeatClickListener {
        void onSeatClick(String seatNumber, boolean isSelecting);
    }

    public SeatAdapter(List<Seat> seatList, int seatCount, OnSeatClickListener listener) {
        this.seatList = seatList;
        this.seatCount = seatCount;
        this.listener = listener;
    }

    public void updateSeats(List<Seat> newSeats) {
        this.seatList = newSeats != null ? newSeats : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seat_item, parent, false);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        Seat seat = seatList.get(position);
        holder.btnSeat.setText(seat.getSeatNumber());

        // Đặt màu nền dựa vào trạng thái đặt ghế
        if (seat.getIsBooked()) {
            holder.btnSeat.setBackgroundColor(Color.parseColor("#7C7270")); // Màu xám nếu đã đặt
            holder.btnSeat.setTextColor(Color.parseColor("#FFFFFF"));
            holder.btnSeat.setEnabled(false);
        } else if (selectedSeats.contains(seat.getSeatNumber())) { // Kiểm tra Seat thay vì seatNumber
            holder.btnSeat.setBackgroundColor(Color.parseColor("#EA6300")); // Màu cam nếu đã chọn
            holder.btnSeat.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.btnSeat.setBackgroundColor(Color.parseColor("#F4EFEF")); // Màu trắng nếu còn trống
            holder.btnSeat.setTextColor(Color.parseColor("#575757"));
        }

        holder.btnSeat.setOnClickListener(v -> {
            if (seat.getIsBooked()) return;

            if (selectedSeats.contains(seat.getSeatNumber())) {
                selectedSeats.remove(seat.getSeatNumber());
                listener.onSeatClick(seat.getSeatNumber(), false);
            } else {
                if (selectedSeats.size() < seatCount) {
                    selectedSeats.add(seat.getSeatNumber());
                    listener.onSeatClick(seat.getSeatNumber(), true);
                }
            }
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return seatList.size();
    }

    static class SeatViewHolder extends RecyclerView.ViewHolder {
        Button btnSeat;

        public SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            btnSeat = itemView.findViewById(R.id.btn_seat);
        }
    }
}

