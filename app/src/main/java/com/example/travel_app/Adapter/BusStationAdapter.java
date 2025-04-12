package com.example.travel_app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Data.Model.BusStation;
import com.example.travel_app.R;

import java.util.List;

public class BusStationAdapter extends RecyclerView.Adapter<BusStationAdapter.BusStationViewHolder> {
    private List<BusStation> busStationList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(BusStation busStation);
    }

    public BusStationAdapter(List<BusStation> busStationList, OnItemClickListener listener) {
        this.busStationList = busStationList;
        this.listener = listener;
    }

    public void updateData(List<BusStation> newBusStationList) {
        this.busStationList = newBusStationList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BusStationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_station_item, parent, false);
        return new BusStationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusStationViewHolder holder, int position) {
        BusStation busStation = busStationList.get(position);
        holder.tvBusStationCity.setText(busStation.getCity());
        holder.tvCity.setText("Điểm lên xe ở " + busStation.getName());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(busStation);
            }
        });
    }

    @Override
    public int getItemCount() {
        return busStationList.size();
    }

    static class BusStationViewHolder extends RecyclerView.ViewHolder {
        TextView tvBusStationCity, tvCity;

        public BusStationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBusStationCity = itemView.findViewById(R.id.tv_bus_station_city);
            tvCity = itemView.findViewById(R.id.txt_city);
        }
    }
}
