package com.example.travel_app.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Data.Model.Day;
import com.example.travel_app.Data.Model.Itinerary;
import com.example.travel_app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.ItineraryViewHolder> {
    private List<Itinerary> itineraryList;
    private OnItineraryClickListener listener;
//    private boolean isSharedList;

    public interface OnItineraryClickListener {
        void onItineraryClick(Itinerary itinerary);
        void onShareClick(Itinerary itinerary);
    }

    public ItineraryAdapter(List<Itinerary> itineraryList, OnItineraryClickListener listener) {
        this.itineraryList = itineraryList != null ? itineraryList : new ArrayList<>();
//        this.isSharedList = isSharedList;
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
                    .mapToInt(day -> day.getItems() != null ? day.getItems().size() : 0)
                    .sum();
        }
        holder.txtItineraryDetail.setText(totalLocations + " địa điểm - " + totalDays + "N" + (totalDays > 0 ? totalDays - 1 : 0) + "D");

        if (days != null && !days.isEmpty() && days.get(0).getItems() != null && !days.get(0).getItems().isEmpty()) {
            String picUrl = days.get(0).getItems().get(0).getPic();
            if (picUrl != null && !picUrl.isEmpty()) {
                Picasso.get().load(picUrl).into(holder.imgItinerary);
            } else {
                holder.imgItinerary.setImageResource(R.drawable.ho_hoan_kiem); // Ảnh mặc định
            }
        } else {
            holder.imgItinerary.setImageResource(R.drawable.ho_hoan_kiem); // Ảnh mặc định nếu không có dữ liệu
        }

        // Ẩn lnShare dựa trên isSharedList và trạng thái isShare của itinerary
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
//        this.isSharedList = isSharedList;
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