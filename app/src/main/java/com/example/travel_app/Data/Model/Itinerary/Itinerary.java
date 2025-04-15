package com.example.travel_app.Data.Model.Itinerary;

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

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getUserImg() { return userImg; }
    public void setUserImg(String userImg) { this.userImg = userImg; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public boolean getIsShare() { return isShare; }
    public void setIsShare(boolean isShare) { this.isShare = isShare; }
    public List<Day> getDays() { return days; }
    public void setDays(List<Day> days) { this.days = days; }
    public String getCreateDate() { return createDate; }
    public void setCreateDate(String createDate) { this.createDate = createDate; }
}