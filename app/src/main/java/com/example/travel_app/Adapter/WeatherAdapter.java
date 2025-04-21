package com.example.travel_app.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Data.Model.WeatherItem;
import com.example.travel_app.R;

import java.util.List;


public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private List<WeatherItem> weatherList;


    public WeatherAdapter(List<WeatherItem> weatherList) {
        this.weatherList = weatherList;
    }

    // Cập nhật danh sách thời tiết
    public void setWeatherList(List<WeatherItem> weatherList) {
        this.weatherList = weatherList;
        notifyDataSetChanged();  // Notify adapter to update the UI
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (weatherList != null && weatherList.size() > 0) {
            WeatherItem item = weatherList.get(position);
            holder.textDescription.setText(item.getDescription());
            holder.textTemperature.setText(item.getTemperature() + "°C");
            holder.textWind.setText("Gió: " + item.getWindSpeed() + " m/s");

            // Set icon theo description
            String desc = item.getDescription().toLowerCase();
            if (desc.contains("sun") || desc.contains("clear")) {
                holder.imageWeatherIcon.setImageResource(R.drawable.ic_sunny);
            } else if (desc.contains("rain")) {
                holder.imageWeatherIcon.setImageResource(R.drawable.ic_rainy);
            } else if (desc.contains("cloud") || desc.contains("overcast")) {
                holder.imageWeatherIcon.setImageResource(R.drawable.ic_cloudy);
            } else if (desc.contains("storm") || desc.contains("thunder")) {
                holder.imageWeatherIcon.setImageResource(R.drawable.ic_storm);
            } else if (desc.contains("snow")) {
                holder.imageWeatherIcon.setImageResource(R.drawable.ic_snow);
            } else {
                holder.imageWeatherIcon.setImageResource(R.drawable.ic_cloudy); // mặc định
            }
        }
    }

    @Override
    public int getItemCount() {
        return (weatherList != null) ? weatherList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textDescription, textTemperature, textWind;
        ImageView imageWeatherIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textDescription = itemView.findViewById(R.id.textDescription);
            textTemperature = itemView.findViewById(R.id.textTemperature);
            textWind = itemView.findViewById(R.id.textWind);
            imageWeatherIcon = itemView.findViewById(R.id.imageWeatherIcon);
        }
    }
}




