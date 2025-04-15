package com.example.travel_app.Data.Model;

import com.google.firebase.database.PropertyName;

public class Image {
    @PropertyName("caption")
    private String caption;
    @PropertyName("image_id")
    private int imageId;
    @PropertyName("location_id")
    private int locationId;
    @PropertyName("position")
    private int position;
    @PropertyName("url")
    private String url;

    public Image() {
    }

    public Image(String caption, int imageId, int locationId, int position, String url) {
        this.caption = caption;
        this.imageId = imageId;
        this.locationId = locationId;
        this.position = position;
        this.url = url;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}