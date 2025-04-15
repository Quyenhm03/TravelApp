package com.example.travel_app.Adapter;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Data.Model.Itinerary.Location;
import com.example.travel_app.R;
import com.example.travel_app.ViewModel.Itinerary.ImageViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

public class LocationHomeAdapter extends RecyclerView.Adapter<LocationHomeAdapter.LocationViewHolder> {
    private List<Location> locationList;
    private Context context;
    private ImageViewModel imageViewModel;
    private LifecycleOwner lifecycleOwner;
    private OnLocationClickListener onLocationClickListener; // Callback cho sự kiện click

    // Interface để xử lý sự kiện click
    public interface OnLocationClickListener {
        void onLocationClick(int locationId);
    }

    public LocationHomeAdapter(Context context, LifecycleOwner lifecycleOwner, List<Location> locationList, ImageViewModel imageViewModel, OnLocationClickListener listener) {
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
        this.locationList = locationList != null ? new ArrayList<>(locationList) : new ArrayList<>();
        this.imageViewModel = imageViewModel;
        this.onLocationClickListener = listener; // Nhận callback từ Fragment

        // Quan sát LiveData từ ImageViewModel để cập nhật ảnh
        imageViewModel.getImageUrlMapLiveData().observe(lifecycleOwner, urlMap -> {
            notifyDataSetChanged(); // Cập nhật lại toàn bộ danh sách khi có URL mới
        });

        // Tải trước URL ảnh cho tất cả Location
        for (Location location : this.locationList) {
            imageViewModel.loadImageForLocation(location.getLocation_id());
        }
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item_home, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Location location = locationList.get(position);

        // Hiển thị tên thành phố (vitri)
        holder.txtCity.setText(location.getVitri() != null ? location.getVitri() : "Không có địa chỉ");

        // Hiển thị điểm đánh giá
        holder.txtVote.setText(String.format("%.1f/5.0", location.getVote()));

        // Lấy URL ảnh từ ImageViewModel
        Map<Integer, String> urlMap = imageViewModel.getImageUrlMapLiveData().getValue();
        String imageUrl = null;
        if (urlMap != null) {
            imageUrl = urlMap.get(location.getLocation_id());
        }

        // Kiểm tra imageUrl trước khi tải bằng Picasso
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.ho_hoan_kiem)
                    .error(R.drawable.ho_hoan_kiem)
                    .into(holder.imgLocation);
        } else {
            holder.imgLocation.setImageResource(R.drawable.ho_hoan_kiem);
        }

        // Thiết lập sự kiện click cho item
        holder.itemView.setOnClickListener(v -> {
            if (onLocationClickListener != null) {
                onLocationClickListener.onLocationClick(location.getLocation_id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public void setLocationList(List<Location> locations) {
        this.locationList = locations != null ? new ArrayList<>(locations) : new ArrayList<>();
        // Tải trước URL ảnh cho các Location mới
        for (Location location : this.locationList) {
            imageViewModel.loadImageForLocation(location.getLocation_id());
        }
        notifyDataSetChanged();
    }

    static class LocationViewHolder extends RecyclerView.ViewHolder {
        ImageView imgLocation;
        TextView txtVote, txtCity;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLocation = itemView.findViewById(R.id.img_location);
            txtVote = itemView.findViewById(R.id.txt_vote);
            txtCity = itemView.findViewById(R.id.txt_city);
        }
    }
}