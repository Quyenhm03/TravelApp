package com.example.travel_app.Data.Model;

public class ReviewWithUser {
    private Long reviewId;
    private String userId;
    private int locationId;
    private String comment;
    private float rating;
    private String createAt;
    private String fullName;

    public ReviewWithUser() {
    }

    public ReviewWithUser(Review review, String fullName) {
        this.reviewId = review.getReviewId();
        this.userId = review.getUserId();
        this.locationId = review.getLocationId();
        this.comment = review.getComment();
        this.rating = review.getRating();
        this.createAt = review.getCreateAt();
        this.fullName = fullName;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public String getUserId() { // Sửa từ Integer thành String
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}