package com.example.travel_app.Data.Model.Itinerary;

import java.io.Serializable;

public class Image implements Serializable {
    private String caption;
    private int image_id;
    private int media_id;
    private int position;
    private String url;

    public Image() {
    }

    // Getters and Setters
    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }
    public int getImage_id() { return image_id; }
    public void setImage_id(int image_id) { this.image_id = image_id; }
    public int getMedia_id() { return media_id; }
    public void setMedia_id(int media_id) { this.media_id = media_id; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}