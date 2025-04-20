package com.example.travel_app.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide; // Thêm Glide để tải ảnh
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.travel_app.Data.Model.Hotel;
import com.example.travel_app.R;
import com.example.travel_app.UI.Activity.DetailHotelActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder> {
    private List<Hotel> hotelList;
    private List<Hotel> hotelListFull;

    public HotelAdapter(List<Hotel> hotelList) {
        this.hotelList = new ArrayList<>(hotelList);
        this.hotelListFull = new ArrayList<>(hotelList);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setHotelList(List<Hotel> newList) {
        this.hotelList = new ArrayList<>(newList);
        this.hotelListFull = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hotel, parent, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        Hotel hotel = hotelList.get(position);
        holder.textHotelName.setText(hotel.getName());
        holder.textHotelLocation.setText(hotel.getLocation());
        holder.textHotelRating.setText(String.valueOf(hotel.getRating()));
        holder.textHotelPrice.setText("635.000 VNĐ/đêm");

        holder.buttonBook.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailHotelActivity.class);
            intent.putExtra("hotel_id", hotel.getId());
            holder.itemView.getContext().startActivity(intent);
        });
        holder.bindData(holder, hotel);
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    // Lọc danh sách khách sạn theo thành phố
    @SuppressLint("NotifyDataSetChanged")
    public void filter(String city) {
        hotelList.clear();
        if (city.isEmpty()) {
            hotelList.addAll(hotelListFull);
        } else {
            String normalizedCity = removeDiacritics(city.toLowerCase());
            for (Hotel hotel : hotelListFull) {
                String normalizedLocation = removeDiacritics(hotel.getLocation().toLowerCase());
                if (normalizedLocation.contains(normalizedCity)) {
                    hotelList.add(hotel);
                }
            }
        }
        notifyDataSetChanged();
    }

    // Hàm loại bỏ dấu tiếng Việt
    private String removeDiacritics(String text) {
        text = text.replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a");
        text = text.replaceAll("[èéẹẻẽêềếệểễ]", "e");
        text = text.replaceAll("[ìíịỉĩ]", "i");
        text = text.replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o");
        text = text.replaceAll("[ùúụủũưừứựửữ]", "u");
        text = text.replaceAll("[ỳýỷỹỵ]", "y");
        text = text.replaceAll("đ", "d");
        return text;
    }

    static class HotelViewHolder extends RecyclerView.ViewHolder {
        ImageView imageHotel;
        TextView textHotelName;
        TextView textHotelLocation;
        TextView textHotelRating;
        TextView textHotelPrice;
        Button buttonBook;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            imageHotel = itemView.findViewById(R.id.imageHotel);
            textHotelName = itemView.findViewById(R.id.textHotelName);
            textHotelLocation = itemView.findViewById(R.id.textHotelLocation);
            textHotelRating = itemView.findViewById(R.id.textHotelRating);
            textHotelPrice = itemView.findViewById(R.id.textHotelPrice);
            buttonBook = itemView.findViewById(R.id.buttonBook);
        }

        public void bindData(HotelViewHolder holder, Hotel hotel) {
            String hotelUrl = hotel.getHotelUrl() != null ? hotel.getHotelUrl() : "null";
            Log.d("HotelAdapter", "Hotel: " + hotel.getName() + ", URL: [" + hotelUrl + "]");

            if (hotel.getHotelUrl() != null && !hotel.getHotelUrl().isEmpty()) {
                Picasso.get()
                        .load(hotel.getHotelUrl())
                        .placeholder(R.drawable.placeholder_image) // Ảnh hiển thị khi đang tải
                        .error(R.drawable.img_hotel_default) // Ảnh hiển thị khi lỗi
                        .into(holder.imageHotel, new Callback() {
                            @Override
                            public void onSuccess() {
                                Log.d("Picasso", "Load successful for " + hotel.getHotelUrl());
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e("Picasso", "Load failed for " + hotel.getHotelUrl() + ": " + e.getMessage());
                            }
                        });
            } else {
                Log.w("HotelAdapter", "Empty or null URL for hotel: " + hotel.getName());
                holder.imageHotel.setImageResource(R.drawable.img_hotel_default);
            }

            holder.textHotelName.setText(hotel.getName());
            holder.textHotelLocation.setText(hotel.getLocation());
            holder.textHotelRating.setText(String.valueOf(hotel.getRating()));
        }
    }
}