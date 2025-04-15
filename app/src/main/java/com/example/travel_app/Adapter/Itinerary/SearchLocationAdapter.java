package com.example.travel_app.Adapter.Itinerary;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Data.Model.Itinerary.Location;
import com.example.travel_app.R;
import com.example.travel_app.UI.Activity.Location.LocationActivity;
import com.example.travel_app.ViewModel.Itinerary.ImageViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchLocationAdapter extends RecyclerView.Adapter<SearchLocationAdapter.ViewHolder> {
    private List<Location> locations;
    private final ImageViewModel imageViewModel;
    private final OnItemClickListener listener;

    public SearchLocationAdapter(List<Location> locations, ImageViewModel imageViewModel, OnItemClickListener listener) {
        this.locations = (locations != null) ? new ArrayList<>(locations) : new ArrayList<>();
        this.imageViewModel = imageViewModel;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Location location = locations.get(position);
        holder.txtName.setText(location.getTendiadiem() != null ? location.getTendiadiem() : "Không có tên");
        holder.txtAddress.setText(location.getVitri() != null ? location.getVitri() : "Không có địa chỉ");

        imageViewModel.loadImageForLocation(location.getLocation_id());
        imageViewModel.getImageUrlMapLiveData().observe((LifecycleOwner) holder.itemView.getContext(), imageUrlMap -> {
            String imageUrl = imageUrlMap.get(location.getLocation_id());
            Log.d("SearchLocationAdapter", "Location ID: " + location.getLocation_id() + ", Image URL: " + imageUrl);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Picasso.get().load(imageUrl).into(holder.imgLocation);
            }
        });

        holder.btnDelete.setVisibility(View.GONE);
        holder.btnSelect.setVisibility(View.VISIBLE); // Hiển thị nút Chọn

        // Xử lý click vào nút Chọn (trả về kết quả)
        holder.btnSelect.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(location);
            }
        });

        // Xử lý click vào item (chuyển sang LocationActivity)
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), LocationActivity.class);
            intent.putExtra("location_id", location.getLocation_id());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public void updateLocations(List<Location> newLocations) {
        this.locations.clear();
        this.locations.addAll(newLocations != null ? newLocations : new ArrayList<>());
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgLocation;
        TextView txtName, txtAddress;
        ImageButton btnDelete;
        AppCompatButton btnSelect;

        ViewHolder(View itemView) {
            super(itemView);
            imgLocation = itemView.findViewById(R.id.img_location);
            txtName = itemView.findViewById(R.id.txt_name_location);
            txtAddress = itemView.findViewById(R.id.txt_address_location);
            btnDelete = itemView.findViewById(R.id.btn_delete_item);
            btnSelect = itemView.findViewById(R.id.btn_select); // Khởi tạo nút Chọn
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Location location);
    }
}