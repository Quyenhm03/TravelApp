package com.example.travel_app.Data.Model;

import java.io.Serializable;
import java.util.List;

public class Itinerary implements Serializable {
    private String id;
    private String userId;
    private String userName;
    private String userImg;
    private String title;
    private boolean isShare;
    private List<Day> days;
    private String createDate;

    public Itinerary() {
    }

    public Itinerary(String userId, String userName, String id, String userImg, String title, boolean isShare, List<Day> days, String createDate) {
        this.userId = userId;
        this.userName = userName;
        this.id = id;
        this.userImg = userImg;
        this.title = title;
        this.isShare = isShare;
        this.days = days;
        this.createDate = createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public boolean getIsShare() {
        return isShare;
    }

    public void setIsShare(boolean share) {
        isShare = share;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }
}
