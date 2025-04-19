package com.example.travel_app.Adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Adapter.listener.OnViewMoreClickListener;
import com.example.travel_app.Data.Model.ReviewWithUser;
import com.example.travel_app.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<ReviewWithUser> reviews;
    private boolean isLimited = true;
    private final int maxInitialDisplay = 3;
    private final OnViewMoreClickListener onViewMoreClickListener;

    public ReviewAdapter(List<ReviewWithUser> reviews, OnViewMoreClickListener onViewMoreClickListener) {
        this.reviews = reviews;
        this.onViewMoreClickListener = onViewMoreClickListener;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        public TextView tvReviewerName;
        public RatingBar rbReviewRating;
        public TextView tvReviewContent;
        public TextView tvReviewDate;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReviewerName = itemView.findViewById(R.id.tvReviewerName);
            rbReviewRating = itemView.findViewById(R.id.rbReviewRating);
            tvReviewContent = itemView.findViewById(R.id.tvReviewContent);
            tvReviewDate = itemView.findViewById(R.id.tvReviewDate);
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
        ReviewWithUser review = reviews.get(position);
        holder.tvReviewerName.setText("Người dùng: " + review.getFullName());
        holder.rbReviewRating.setRating(review.getRating());
        holder.tvReviewContent.setText(review.getComment());

        LayerDrawable stars = (LayerDrawable) holder.rbReviewRating.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#FFD700"), PorterDuff.Mode.SRC_ATOP); // full
        stars.getDrawable(1).setColorFilter(Color.parseColor("#FFD700"), PorterDuff.Mode.SRC_ATOP); // half
        stars.getDrawable(0).setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP); // empty


        String createAt = review.getCreateAt();
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            Date date = inputFormat.parse(createAt);
            assert date != null;
            holder.tvReviewDate.setText(outputFormat.format(date));
        } catch (Exception e) {
            holder.tvReviewDate.setText(createAt);
        }
    }

    @Override
    public int getItemCount() {
        if (isLimited) {
            return Math.min(reviews.size(), maxInitialDisplay);
        } else {
            return reviews.size();
        }
    }

    public void updateReviews(List<ReviewWithUser> newReviews) {
        this.reviews = newReviews;
        notifyDataSetChanged();
    }

    public void showAllReviews() {
        isLimited = false;
        notifyDataSetChanged();
        if (onViewMoreClickListener != null) {
            onViewMoreClickListener.onViewMoreClick();
        }
    }
}