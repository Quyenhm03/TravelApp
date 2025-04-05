package com.example.travel_app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Data.Model.Item;
import com.example.travel_app.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.ViewHolder> {
    private List<Item> items;
    private OnItemClickListener listener;

    public SearchItemAdapter(List<Item> items, OnItemClickListener listener) {
        this.items = items;
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
        Item item = items.get(position);
        holder.txtName.setText(item.getTitle());
        holder.txtAddress.setText(item.getAddress());

        // Giả lập hình ảnh
        Picasso.get().load(item.getPic()).into(holder.imgLocation);
        holder.btnDelete.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(List<Item> newItems) {
        this.items.clear();
        this.items.addAll(newItems);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgLocation;
        TextView txtName, txtAddress;
        ImageButton btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            imgLocation = itemView.findViewById(R.id.img_location);
            txtName = itemView.findViewById(R.id.txt_name_location);
            txtAddress = itemView.findViewById(R.id.txt_address_location);
            btnDelete = itemView.findViewById(R.id.btn_delete_item);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }
}