package com.example.travel_app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Data.Model.Airport;
import com.example.travel_app.R;

import java.util.List;

public class AirportAdapter extends RecyclerView.Adapter<AirportAdapter.AirportViewHolder> {
    private List<Airport> airportList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Airport airport);
    }

    public AirportAdapter(List<Airport> airportList, OnItemClickListener listener) {
        this.airportList = airportList;
        this.listener = listener;
    }

    public void updateData(List<Airport> newAirportList) {
        this.airportList = newAirportList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AirportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.airport_item, parent, false);
        return new AirportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AirportViewHolder holder, int position) {
        Airport airport = airportList.get(position);
        holder.tvAirportName.setText(airport.getName());
        holder.tvAirportCode.setText(airport.getAirportCode());
        holder.tvAirportAddress.setText(airport.getAirportAddress());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(airport);
            }
        });
    }

    @Override
    public int getItemCount() {
        return airportList.size();
    }

    static class AirportViewHolder extends RecyclerView.ViewHolder {
        TextView tvAirportName, tvAirportCode, tvAirportAddress;

        public AirportViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAirportName = itemView.findViewById(R.id.tv_airport_name);
            tvAirportCode = itemView.findViewById(R.id.tv_airport_code);
            tvAirportAddress = itemView.findViewById(R.id.tv_airport_address);
        }
    }
}
