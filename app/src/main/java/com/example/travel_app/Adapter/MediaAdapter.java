package com.example.travel_app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travel_app.R;

import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {

    private List<String> mediaList;

    public MediaAdapter(List<String> mediaList) {
        this.mediaList = mediaList;
    }

    public static class MediaViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public VideoView videoView;

        public MediaViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivMedia);
            videoView = itemView.findViewById(R.id.vvMedia);
        }
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_media, parent, false);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        String media = mediaList.get(position);
        if (media.endsWith(".mp4")) { // Kiểm tra nếu là video
            holder.imageView.setVisibility(View.GONE);
            holder.videoView.setVisibility(View.VISIBLE);
            holder.videoView.setVideoPath(media);
            holder.videoView.start();
        } else { // Là ảnh
            holder.videoView.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
            // Sử dụng Glide để load ảnh
            Glide.with(holder.itemView.getContext()).load(media).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }
}
