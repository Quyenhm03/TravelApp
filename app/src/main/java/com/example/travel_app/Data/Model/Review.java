package com.example.travel_app.Data.Model;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

@IgnoreExtraProperties
public class Review {
    @PropertyName("review_id")
    private int reviewId;

    @PropertyName("location_id")
    private int locationId;

    @PropertyName("user_id")
    private String userId;

    @PropertyName("comment")
    private String comment;

    @PropertyName("create_at")
    private String createAt;

    @PropertyName("rating")
    private float rating;

    public Review() {
    }

    @PropertyName("review_id")
    public int getReviewId() {
        return reviewId;
    }

    @PropertyName("review_id")
    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    @PropertyName("location_id")
    public int getLocationId() {
        return locationId;
    }

    @PropertyName("location_id")
    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    @PropertyName("user_id")
    public String getUserId() {
        return userId;
    }


    @PropertyName("user_id")
    public void setUserId(Long userId) {
        this.userId = userId != null ? String.valueOf(userId) : null;
    }

    @PropertyName("comment")
    public String getComment() {
        return comment;
    }

    @PropertyName("comment")
    public void setComment(String comment) {
        this.comment = comment;
    }

    @PropertyName("create_at")
    public String getCreateAt() {
        return createAt;
    }

    @PropertyName("create_at")
    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    @PropertyName("rating")
    public float getRating() {
        return rating;
    }

    @PropertyName("rating")
    public void setRating(float rating) {
        this.rating = rating;
    }
}