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

import java.util.ArrayList;
import java.util.List;

public class DayItemAdapter extends RecyclerView.Adapter<DayItemAdapter.ViewHolder> {
    private List<Item> items;
    private OnDeleteListener listener;

    public DayItemAdapter(List<Item> items, OnDeleteListener listener) {
        this.items = (items != null) ? new ArrayList<>(items) : new ArrayList<>(); // Khởi tạo an toàn
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
        if (item != null) {
            holder.txtName.setText(item.getTitle() != null ? item.getTitle() : "Không có tiêu đề");
            holder.txtAddress.setText(item.getAddress() != null ? item.getAddress() : "Không có địa chỉ");

            // Tải hình ảnh bằng Picasso, với placeholder và xử lý lỗi
            if (item.getPic() != null && !item.getPic().isEmpty()) {
                Picasso.get()
                        .load(item.getPic())
                        .placeholder(R.drawable.ho_hoan_kiem) // Hình mặc định nếu đang tải
                        .error(R.drawable.ho_hoan_kiem) // Hình mặc định nếu lỗi
                        .into(holder.imgLocation);
            } else {
                holder.imgLocation.setImageResource(R.drawable.ho_hoan_kiem); // Hình mặc định nếu pic null
            }

            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(item);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(List<Item> newItems) {
        this.items.clear();
        this.items.addAll(newItems != null ? newItems : new ArrayList<>());
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

    public interface OnDeleteListener {
        void onDeleteClick(Item item);
    }
}