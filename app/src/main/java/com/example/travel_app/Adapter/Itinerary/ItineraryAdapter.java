package com.example.travel_app.Adapter.Itinerary;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Data.Model.Itinerary.Day;
import com.example.travel_app.Data.Model.Itinerary.Itinerary;
import com.example.travel_app.Data.Model.Itinerary.Location;
import com.example.travel_app.R;
import com.example.travel_app.ViewModel.Itinerary.ImageViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.ItineraryViewHolder> {
    private List<Itinerary> itineraryList;
    private final OnItineraryClickListener listener;
    private final ImageViewModel imageViewModel;

    public interface OnItineraryClickListener {
        void onItineraryClick(Itinerary itinerary);
        void onShareClick(Itinerary itinerary);
    }

    public ItineraryAdapter(List<Itinerary> itineraryList, ImageViewModel imageViewModel, OnItineraryClickListener listener) {
        this.itineraryList = itineraryList != null ? itineraryList : new ArrayList<>();
        this.imageViewModel = imageViewModel;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItineraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itinerary_item, parent, false);
        return new ItineraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItineraryViewHolder holder, int position) {
        Itinerary itinerary = itineraryList.get(position);
        holder.txtTitle.setText(itinerary.getTitle() != null ? itinerary.getTitle() : "Không có tiêu đề");

        List<Day> days = itinerary.getDays();
        int totalLocations = 0;
        int totalDays = 0;
        if (days != null) {
            totalDays = days.size();
            totalLocations = days.stream()
                    .mapToInt(day -> day.getLocations() != null ? day.getLocations().size() : 0)
                    .sum();
        }
        holder.txtItineraryDetail.setText(totalLocations + " địa điểm - " + totalDays + "N" + (totalDays > 0 ? totalDays - 1 : 0) + "D");

        // Tải ảnh của Location đầu tiên trong ngày đầu tiên
        if (days != null && !days.isEmpty() && days.get(0).getLocations() != null && !days.get(0).getLocations().isEmpty()) {
            Location firstLocation = days.get(0).getLocations().get(0);
            int locationId = firstLocation.getLocation_id();
            imageViewModel.loadImageForLocation(locationId);
            imageViewModel.getImageUrlMapLiveData().observe((LifecycleOwner) holder.itemView.getContext(), imageUrlMap -> {
                String imageUrl = imageUrlMap.get(locationId);
//                Log.d("ItineraryAdapter", "Image for location ID: " + locationId + ", URL: " + imageUrl);
                Picasso.get().load(imageUrl).into(holder.imgItinerary);

            });
        } else {
            holder.imgItinerary.setImageResource(R.drawable.ho_hoan_kiem);
            Log.w("ItineraryAdapter", "No locations found for itinerary ID: " + itinerary.getId());
        }

        // Ẩn lnShare dựa trên trạng thái isShare của itinerary
        Log.d("ItineraryAdapter", "Itinerary ID: " + itinerary.getId() + ", isShare: " + itinerary.getIsShare());
        if (itinerary.getIsShare()) {
            holder.lnShare.setVisibility(View.GONE);
        } else {
            holder.lnShare.setVisibility(View.VISIBLE);
            holder.btnShare.setOnClickListener(v -> listener.onShareClick(itinerary));
        }

        // Xử lý sự kiện click vào CardView
        holder.itemView.setOnClickListener(v -> listener.onItineraryClick(itinerary));
    }

    @Override
    public int getItemCount() {
        return itineraryList.size();
    }

    public void updateList(List<Itinerary> newList) {
        this.itineraryList = newList != null ? newList : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class ItineraryViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtItineraryDetail;
        LinearLayout lnShare;
        ImageView imgItinerary;
        AppCompatImageButton btnShare;

        public ItineraryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title_itinerary);
            txtItineraryDetail = itemView.findViewById(R.id.txt_detail_itinerary);
            lnShare = itemView.findViewById(R.id.ln_share);
            btnShare = itemView.findViewById(R.id.btn_share);
            imgItinerary = itemView.findViewById(R.id.img_itinerary);
        }
    }
}