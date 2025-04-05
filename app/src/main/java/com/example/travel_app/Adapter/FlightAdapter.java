package com.example.travel_app.Adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Data.Model.Flight;
import com.example.travel_app.R;
import com.squareup.picasso.Picasso;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {
    private List<Flight> flightList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Flight flight);
        void onViewDetailClick(Flight flight);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public FlightAdapter(List<Flight> flightList) {
        this.flightList = flightList;
    }

    public void setFlightList(List<Flight> flightList) {
        this.flightList = flightList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_flight, parent, false);
        return new FlightViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flightList.get(position);
        holder.txtFlightNumber.setText(flight.getFlightNumber());
        holder.txtDepartureAirportCode.setText(flight.getDepartureAirportCode());
        holder.txtArrivalAirportCode.setText(flight.getArrivalAirportCode());
        holder.txtDepartureTime.setText(flight.getDepartureTime());
        holder.txtArrivalTime.setText(flight.getArrivalTime());
        holder.txtPrice.setText((int)flight.getPrice() + " VND");
        holder.txtFlightTime.setText(flight.getFlightTime());

        Picasso.get().load(flight.getAirlineImgUrl()).into(holder.imgAirline);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(flight);
            }
        });

        holder.btnViewDetail.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onViewDetailClick(flight);
            }
        });
    }

    @Override
    public int getItemCount() {
        return flightList != null ? flightList.size() : 0;
    }

    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        TextView txtFlightNumber, txtFlightTime, txtDepartureTime, txtDepartureAirportCode, txtArrivalTime, txtArrivalAirportCode, txtPrice;
        ImageView imgAirline;
        Button btnViewDetail;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);

            txtFlightNumber = itemView.findViewById(R.id.txt_flight_number);
            txtFlightTime = itemView.findViewById(R.id.txt_flight_time);
            txtDepartureTime = itemView.findViewById(R.id.txt_departure_time);
            txtArrivalTime = itemView.findViewById(R.id.txt_arrival_time);
            txtDepartureAirportCode = itemView.findViewById(R.id.txt_departure_airport_code_ticket);
            txtArrivalAirportCode = itemView.findViewById(R.id.txt_arrival_airport_code_ticket);
            txtPrice = itemView.findViewById(R.id.txt_ticket_flight_price);
            imgAirline = itemView.findViewById(R.id.img_airline);
            btnViewDetail = itemView.findViewById(R.id.btn_view_detail_ticket);

        }
    }
}
