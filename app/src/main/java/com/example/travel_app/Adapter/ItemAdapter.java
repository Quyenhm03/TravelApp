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

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<Item> itemList;
    private boolean isDeleteButtonVisible = true; // Biến kiểm soát hiển thị nút Delete

    public ItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    // Phương thức để cập nhật trạng thái hiển thị nút Delete
    public void setDeleteButtonVisible(boolean visible) {
        this.isDeleteButtonVisible = visible;
        notifyDataSetChanged(); // Cập nhật lại giao diện
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.txtTitle.setText(item.getTitle());
        holder.txtAddress.setText(item.getAddress());

        Picasso.get().load(item.getPic()).into(holder.imgLocation);

        // Ẩn hoặc hiển thị nút Delete dựa trên biến isDeleteButtonVisible
        holder.btnDelete.setVisibility(isDeleteButtonVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }

    public void updateList(List<Item> newList) {
        this.itemList = newList;
        notifyDataSetChanged();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtAddress;
        ImageView imgLocation;
        ImageButton btnDelete; // Thêm ánh xạ cho nút Delete

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_name_location);
            txtAddress = itemView.findViewById(R.id.txt_address_location);
            imgLocation = itemView.findViewById(R.id.img_location);
            btnDelete = itemView.findViewById(R.id.btn_delete_item); // Ánh xạ nút Delete
        }
    }
}