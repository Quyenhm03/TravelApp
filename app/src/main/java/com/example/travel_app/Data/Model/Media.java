package com.example.travel_app.Data.Model;

public class Media {
    private int mediaId;
    private int locationId;
    private String mediaType;
    private String mediaUrl;
    private String uploadAt;
    private int videoId;

    public Media() {
    }

    public Media(int mediaId, int locationId, String mediaType, String mediaUrl, String uploadAt, int videoId) {
        this.mediaId = mediaId;
        this.locationId = locationId;
        this.mediaType = mediaType;
        this.mediaUrl = mediaUrl;
        this.uploadAt = uploadAt;
        this.videoId = videoId;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getUploadAt() {
        return uploadAt;
    }

    public void setUploadAt(String uploadAt) {
        this.uploadAt = uploadAt;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }
}