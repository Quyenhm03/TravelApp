package com.example.travel_app.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.travel_app.Data.Model.Location;
import com.example.travel_app.R;
import com.example.travel_app.UI.Activity.Location.LocationActivity;
import com.example.travel_app.ViewModel.Itinerary.ImageViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavoriteLocationAdapter extends RecyclerView.Adapter<FavoriteLocationAdapter.LocationViewHolder> {

    private List<Location> locationList = new ArrayList<>();
    private final Context context;
    private final LifecycleOwner lifecycleOwner;

    private final SparseArray<String> imageCache = new SparseArray<>();
    private final ImageViewModel imageViewModel;

    public FavoriteLocationAdapter(Context context, LifecycleOwner lifecycleOwner) {
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
        this.imageViewModel = new ViewModelProvider((ViewModelStoreOwner) lifecycleOwner).get(ImageViewModel.class);

    }

    public void setLocationList(List<Location> locations) {
        this.locationList = locations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_location_favorite, parent, false);
        return new LocationViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Location location = locationList.get(position);

        holder.tvLocationName.setText(location.getTenDiaDiem());
        holder.tvSeeDetails.setText("Xem thông tin chi tiết");

        int locationId = location.getLocationId();


        if (imageCache.get(locationId) != null) {
            Glide.with(context)
                    .load(imageCache.get(locationId))
                    .placeholder(R.drawable.ic_location)
                    .into(holder.imageLocation);
        } else {

            imageViewModel.getImageUrlMapLiveData().observe(lifecycleOwner, imageMap -> {
                String imageUrl = imageMap.get(locationId);
                if (imageUrl != null) {
                    imageCache.put(locationId, imageUrl);
                    Glide.with(context)
                            .load(imageUrl)
                            .placeholder(R.drawable.ic_location)
                            .into(holder.imageLocation);
                }
            });

            imageViewModel.loadImageForLocation(locationId);
        }


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, LocationActivity.class);
            intent.putExtra("location_id", location.getLocationId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return locationList != null ? locationList.size() : 0;
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        ImageView imageLocation;
        TextView tvLocationName, tvSeeDetails;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            imageLocation = itemView.findViewById(R.id.imageLocation);
            tvLocationName = itemView.findViewById(R.id.tvLocationName);
            tvSeeDetails = itemView.findViewById(R.id.tvSeeDetails);
        }
    }
}
