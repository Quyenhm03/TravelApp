package com.example.travel_app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Data.Model.Coach;
import com.example.travel_app.R;

import java.util.ArrayList;
import java.util.List;

public class CoachAdapter extends RecyclerView.Adapter<CoachAdapter.CoachViewHolder> {
    private List<Coach> coachList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Coach coach);
        void onViewDetailClick(Coach coach);
    }

    public CoachAdapter(List<Coach> coachList) {
        this.coachList = coachList != null ? coachList : new ArrayList<>();
    }

    public void setCoachList(List<Coach> coachList) {
        this.coachList = coachList != null ? coachList : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CoachViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_coach, parent, false);
        return new CoachViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoachViewHolder holder, int position) {
        Coach coach = coachList.get(position);
        holder.tvCoachName.setText(coach.getCompanyName());
        holder.tvTimeStart.setText(coach.getDepartureTime());
        holder.tvTimeEnd.setText(coach.getArrivalTime());
        holder.tvDepartureStation.setText(coach.getDepartureStationName());
        holder.tvArrivalStation.setText(coach.getArrivalStationName());
        holder.tvPrice.setText(String.format("%,.0f VNĐ", coach.getPrice()));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(coach);
            }
        });

        holder.btnViewDetail.setOnClickListener(v -> {
            if (listener != null) {
                listener.onViewDetailClick(coach);
            }
        });
    }

    @Override
    public int getItemCount() {
        return coachList != null ? coachList.size() : 0;
    }

    static class CoachViewHolder extends RecyclerView.ViewHolder {
        TextView tvCoachName, tvTimeStart, tvTimeEnd, tvDepartureStation, tvArrivalStation, tvPrice;
        AppCompatButton btnViewDetail;

        public CoachViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCoachName = itemView.findViewById(R.id.txt_coach_name);
            tvTimeStart = itemView.findViewById(R.id.txt_time_start);
            tvTimeEnd = itemView.findViewById(R.id.txt_time_end);
            tvDepartureStation = itemView.findViewById(R.id.txt_station_start);
            tvArrivalStation = itemView.findViewById(R.id.txt_station_end);
            tvPrice = itemView.findViewById(R.id.txt_ticket_coach_price);
            btnViewDetail = itemView.findViewById(R.id.btn_coach_detail);
        }
    }
}
