package com.example.travel_app.Data.Model;

import com.google.firebase.database.PropertyName;

public class Hotel {
    @PropertyName("id")
    private String id;
    @PropertyName("name")
    private String name;
    @PropertyName("location")
    private String location;
    @PropertyName("rating")
    private float rating;
    @PropertyName("website")
    private String website;
    @PropertyName("check_in_time")
    private String checkInTime;
    @PropertyName("check_out_time")
    private String checkOutTime;
    @PropertyName("des")
    private String des;
    @PropertyName("hot_line")
    private String hotLine;
    @PropertyName("hotel_url")
    private String hotelUrl;


    public Hotel() {

    }


    public Hotel(String id, String name, String location, float rating, String website,
                 String checkInTime, String checkOutTime, String des, String hotLine, String hotelUrl) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.website = website;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.des = des;
        this.hotLine = hotLine;
        this.hotelUrl = hotelUrl;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @PropertyName("hotel_url")
    public String getHotelUrl() {
        return hotelUrl;
    }

    @PropertyName("hotel_url")
    public void setHotelUrl(String hotelUrl) {
        this.hotelUrl = hotelUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @PropertyName("check_in_time")
    public String getCheckInTime() {
        return checkInTime;
    }

    @PropertyName("check_in_time")
    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    @PropertyName("check_out_time")
    public String getCheckOutTime() {
        return checkOutTime;
    }

    @PropertyName("check_out_time")
    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    @PropertyName("des")
    public String getDes() {
        return des;
    }

    @PropertyName("des")
    public void setDes(String des) {
        this.des = des;
    }

    @PropertyName("hot_line")
    public String getHotLine() {
        return hotLine;
    }

    @PropertyName("hot_line")
    public void setHotLine(String hotLine) {
        this.hotLine = hotLine;
    }
}
