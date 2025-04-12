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

import com.example.travel_app.Data.Model.SearchCoachInfo;
import com.example.travel_app.R;
import com.example.travel_app.Data.Model.Coach;
import com.example.travel_app.UI.Activity.SearchCoachActivity;
import com.example.travel_app.UI.Activity.SearchCoachResultActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CoachHomeAdapter extends RecyclerView.Adapter<CoachHomeAdapter.CoachViewHolder> {
    private Context context;
    private List<Coach> coachList;

    public CoachHomeAdapter(Context context, List<Coach> coachList) {
        this.context = context;
        this.coachList = coachList;
    }

    @NonNull
    @Override
    public CoachViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ticket_item, parent, false);
        return new CoachViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoachViewHolder holder, int position) {
        Coach coach = coachList.get(position);

        holder.txtRoute.setText(coach.getDepartureCity() + " - " + coach.getArrivalCity());
        holder.txtDate.setText(coach.getDepartureDate());
        holder.txtPrice.setText(String.format("%,.0f VND", coach.getPrice()));

        Picasso.get().load(coach.getCoachImg()).into(holder.imgItem);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SearchCoachResultActivity.class);
            SearchCoachInfo searchCoachInfo = new SearchCoachInfo(
                    coach.getDepartureDate(),
                    coach.getDepartureCity(),
                    coach.getArrivalCity(),
                    coach.getDepartureStationName(),
                    coach.getArrivalStationName(),
                    1
            );
            intent.putExtra("searchCoachInfo", searchCoachInfo);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return coachList != null ? coachList.size() : 0;
    }

    public static class CoachViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItem;
        TextView txtRoute, txtDate, txtPrice;

        public CoachViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.img_item);
            txtRoute = itemView.findViewById(R.id.txt_route);
            txtDate = itemView.findViewById(R.id.txt_date);
            txtPrice = itemView.findViewById(R.id.txt_price);
        }
    }

    public void setCoachList(List<Coach> coachList) {
        this.coachList = coachList;
        notifyDataSetChanged();
    }
}