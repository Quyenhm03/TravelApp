package com.example.travel_app.Data.Model;

import java.io.Serializable;

public class Item implements Serializable {
    private int id;
    private String address;
    private int bed;
    private String dateTour;
    private String description;
    private String distance;
    private String duration;
    private String pic;
    private double price;
    private float score;
    private String timeTour;
    private String title;
    private String tourGuideName;
    private String tourGuidePhone;
    private String tourGuidePic;

    public Item() {
    }

    public Item(int id, String address, int bed, String dateTour, String description, String distance, String duration, String pic, double price, float score, String timeTour, String title, String tourGuideName, String tourGuidePhone, String tourGuidePic) {
        this.id = id;
        this.address = address;
        this.bed = bed;
        this.dateTour = dateTour;
        this.description = description;
        this.distance = distance;
        this.duration = duration;
        this.pic = pic;
        this.price = price;
        this.score = score;
        this.timeTour = timeTour;
        this.title = title;
        this.tourGuideName = tourGuideName;
        this.tourGuidePhone = tourGuidePhone;
        this.tourGuidePic = tourGuidePic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getBed() {
        return bed;
    }

    public void setBed(int bed) {
        this.bed = bed;
    }

    public String getDateTour() {
        return dateTour;
    }

    public void setDateTour(String dateTour) {
        this.dateTour = dateTour;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getTimeTour() {
        return timeTour;
    }

    public void setTimeTour(String timeTour) {
        this.timeTour = timeTour;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTourGuideName() {
        return tourGuideName;
    }

    public void setTourGuideName(String tourGuideName) {
        this.tourGuideName = tourGuideName;
    }

    public String getTourGuidePhone() {
        return tourGuidePhone;
    }

    public void setTourGuidePhone(String tourGuidePhone) {
        this.tourGuidePhone = tourGuidePhone;
    }

    public String getTourGuidePic() {
        return tourGuidePic;
    }

    public void setTourGuidePic(String tourGuidePic) {
        this.tourGuidePic = tourGuidePic;
    }
}
