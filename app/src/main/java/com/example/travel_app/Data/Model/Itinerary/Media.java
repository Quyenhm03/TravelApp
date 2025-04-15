package com.example.travel_app.Data.Model.Itinerary;

import java.io.Serializable;

public class Media implements Serializable {
    private int location_id;
    private int media_id;
    private String media_type;
    private String media_url;
    private String upload_at;
    private int video_id;

    public Media() {
    }

    // Getters and Setters
    public int getLocation_id() { return location_id; }
    public void setLocation_id(int location_id) { this.location_id = location_id; }
    public int getMedia_id() { return media_id; }
    public void setMedia_id(int media_id) { this.media_id = media_id; }
    public String getMedia_type() { return media_type; }
    public void setMedia_type(String media_type) { this.media_type = media_type; }
    public String getMedia_url() { return media_url; }
    public void setMedia_url(String media_url) { this.media_url = media_url; }
    public String getUpload_at() { return upload_at; }
    public void setUpload_at(String upload_at) { this.upload_at = upload_at; }
    public int getVideo_id() { return video_id; }
    public void setVideo_id(int video_id) { this.video_id = video_id; }
}
