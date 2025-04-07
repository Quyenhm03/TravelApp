package com.example.travel_app.Data.Model;

public class Review {
    private String id;
    private float rating;
    private String content;
    private String locationId;
    private String userId;

    public Review() {
    }

    public Review(String id, float rating, String content) {
        this.id = id;
        this.rating = rating;
        this.content = content;
    }

    public Review(String id, float rating, String content, String locationId, String userId) {
        this.id = id;
        this.rating = rating;
        this.content = content;
        this.locationId = locationId;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
