package com.example.travel_app.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Adapter.listener.OnViewMoreClickListener;
import com.example.travel_app.Data.Model.Review;
import com.example.travel_app.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviews;
    private boolean isLimited = true; // Biến kiểm soát hiển thị giới hạn 3 đánh giá
    private final int maxInitialDisplay = 3;
    private final OnViewMoreClickListener onViewMoreClickListener;

    // Constructor
    public ReviewAdapter(List<Review> reviews, OnViewMoreClickListener onViewMoreClickListener) {
        this.reviews = reviews;
        this.onViewMoreClickListener = onViewMoreClickListener;
    }

    // ViewHolder class
    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        public TextView tvReviewerName;
        public RatingBar rbReviewRating;
        public TextView tvReviewContent;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReviewerName = itemView.findViewById(R.id.tvReviewerName);
            rbReviewRating = itemView.findViewById(R.id.rbReviewRating);
            tvReviewContent = itemView.findViewById(R.id.tvReviewContent);
        }
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.tvReviewerName.setText(
                holder.itemView.getContext().getString(R.string.userReview) + "Duong"
        );

        holder.rbReviewRating.setRating(review.getRating());
        holder.tvReviewContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        if (isLimited) {
            return Math.min(reviews.size(), maxInitialDisplay);
        } else {
            return reviews.size();
        }
    }

    // Hàm để mở rộng danh sách khi nhấn "Xem thêm"
    public void showAllReviews() {
        isLimited = false;
        notifyDataSetChanged();
        if (onViewMoreClickListener != null) {
            onViewMoreClickListener.onViewMoreClick();
        }
    }
}
