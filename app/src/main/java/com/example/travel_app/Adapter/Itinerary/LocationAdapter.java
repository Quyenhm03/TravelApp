package com.example.travel_app.Adapter.Itinerary;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Data.Model.Itinerary.Location;
import com.example.travel_app.R;
import com.example.travel_app.ViewModel.Itinerary.ImageViewModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
    private List<Location> locationList;
    private final ImageViewModel imageViewModel;
    private boolean isDeleteButtonVisible = true;
    private final OnItemClickListener itemClickListener; // Callback mới cho sự kiện click

    // Interface cho sự kiện click vào item
    public interface OnItemClickListener {
        void onItemClick(Location location);
    }

    public LocationAdapter(List<Location> locationList, ImageViewModel imageViewModel, OnItemClickListener itemClickListener) {
        this.locationList = locationList != null ? new ArrayList<>(locationList) : new ArrayList<>();
        this.imageViewModel = imageViewModel;
        this.itemClickListener = itemClickListener; // Nhận callback từ Activity
    }

    public void setDeleteButtonVisible(boolean visible) {
        this.isDeleteButtonVisible = visible;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Location location = locationList.get(position);
        holder.txtTitle.setText(location.getTendiadiem() != null ? location.getTendiadiem() : "Không có tên");
        holder.txtAddress.setText(location.getVitri() != null ? location.getVitri() : "Không có địa chỉ");

        imageViewModel.loadImageForLocation(location.getLocation_id());
        imageViewModel.getImageUrlMapLiveData().observe((LifecycleOwner) holder.itemView.getContext(), imageUrlMap -> {
            String imageUrl = imageUrlMap.get(location.getLocation_id());
            Log.d("LocationAdapter", "Location ID: " + location.getLocation_id() + ", Image URL: " + imageUrl);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Picasso.get().load(imageUrl).into(holder.imgLocation);
            }
        });

        holder.btnDelete.setVisibility(isDeleteButtonVisible ? View.VISIBLE : View.GONE);

        // Xử lý sự kiện click vào item (logic mới)
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(location);
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public void updateList(List<Location> newList) {
        this.locationList.clear();
        this.locationList.addAll(newList != null ? newList : new ArrayList<>());
        notifyDataSetChanged();
    }

    static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtAddress;
        ImageView imgLocation;
        ImageButton btnDelete;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_name_location);
            txtAddress = itemView.findViewById(R.id.txt_address_location);
            imgLocation = itemView.findViewById(R.id.img_location);
            btnDelete = itemView.findViewById(R.id.btn_delete_item);
        }
    }
}
