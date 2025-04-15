package com.example.travel_app.Data.Model.Itinerary;

import java.io.Serializable;

public class Location implements Serializable {
    private String create_at;
    private boolean is_favourite;
    private int location_id;
    private String mota;
    private String tendiadiem;
    private String update_at;
    private String vitri;
    private float vote;

    public Location() {
    }

    // Getters and Setters
    public String getCreate_at() { return create_at; }
    public void setCreate_at(String create_at) { this.create_at = create_at; }
    public boolean isIs_favourite() { return is_favourite; }
    public void setIs_favourite(boolean is_favourite) { this.is_favourite = is_favourite; }
    public int getLocation_id() { return location_id; }
    public void setLocation_id(int location_id) { this.location_id = location_id; }
    public String getMota() { return mota; }
    public void setMota(String mota) { this.mota = mota; }
    public String getTendiadiem() { return tendiadiem; }
    public void setTendiadiem(String tendiadiem) { this.tendiadiem = tendiadiem; }
    public String getUpdate_at() { return update_at; }
    public void setUpdate_at(String update_at) { this.update_at = update_at; }
    public String getVitri() { return vitri; }
    public void setVitri(String vitri) { this.vitri = vitri; }
    public float getVote() { return vote; }
    public void setVote(float vote) { this.vote = vote; }
}