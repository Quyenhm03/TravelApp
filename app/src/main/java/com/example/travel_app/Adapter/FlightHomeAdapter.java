package com.example.travel_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Data.Model.SearchFlightInfo;
import com.example.travel_app.R;
import com.example.travel_app.Data.Model.Flight;
import com.example.travel_app.UI.Activity.SearchFlightActivity;
import com.example.travel_app.UI.Activity.SearchFlightResultActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FlightHomeAdapter extends RecyclerView.Adapter<FlightHomeAdapter.FlightViewHolder> {
    private Context context;
    private List<Flight> flightList;

    public FlightHomeAdapter(Context context, List<Flight> flightList) {
        this.context = context;
        this.flightList = flightList;
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ticket_item, parent, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flightList.get(position);

        holder.txtRoute.setText(flight.getDepartureCity() + " - " + flight.getArrivalCity());
        holder.txtDate.setText(flight.getDepartureDate());
        holder.txtPrice.setText(String.format("%,.0f VND", flight.getPrice()));


        Picasso.get().load(flight.getFlightImg()).into(holder.imgItem);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SearchFlightResultActivity.class);
            SearchFlightInfo searchFlightInfo = new SearchFlightInfo(
                    flight.getDepartureAirportCode(),
                    flight.getArrivalAirportCode(),
                    flight.getDepartureDate(),
                    flight.getDepartureCity(),
                    flight.getArrivalCity(),
                    flight.getDepartureAirportName(),
                    flight.getArrivalAirportName(),
                    "Economy",
                    1, 0, 0
            );
            intent.putExtra("searchFlightInfo", searchFlightInfo);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return flightList != null ? flightList.size() : 0;
    }

    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItem;
        TextView txtRoute, txtDate, txtPrice;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.img_item);
            txtRoute = itemView.findViewById(R.id.txt_route);
            txtDate = itemView.findViewById(R.id.txt_date);
            txtPrice = itemView.findViewById(R.id.txt_price);
        }
    }

    public void setFlightList(List<Flight> flightList) {
        this.flightList = flightList;
        notifyDataSetChanged();
    }
}