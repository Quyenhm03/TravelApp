package com.example.travel_app.Data.Model;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

public class Location implements Serializable {

    @PropertyName("location_id")
    private int locationId;

    @PropertyName("tendiadiem")
    private String tenDiaDiem;

    @PropertyName("mota")
    private String moTa;

    @PropertyName("vitri")
    private String viTri;

    @PropertyName("create_at")
    private String createAt;

    @PropertyName("update_at")
    private String updateAt;

    @PropertyName("vote")
    private float vote;

    @PropertyName("is_favourite")
    private boolean isFavorite;


    public Location() {}


    public Location(int locationId, String tenDiaDiem, String moTa, String viTri, float vote) {
        this.locationId = locationId;
        this.tenDiaDiem = tenDiaDiem;
        this.moTa = moTa;
        this.viTri = viTri;
        this.vote = vote;
    }

    public Location(int locationId, String tenDiaDiem, String moTa, String viTri, float vote,
                    String createAt, String updateAt, boolean isFavorite) {
        this.locationId = locationId;
        this.tenDiaDiem = tenDiaDiem;
        this.moTa = moTa;
        this.viTri = viTri;
        this.vote = vote;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.isFavorite = isFavorite;
    }

    @PropertyName("location_id")
    public int getLocationId() {
        return locationId;
    }

    @PropertyName("location_id")
    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    @PropertyName("tendiadiem")
    public String getTenDiaDiem() {
        return tenDiaDiem;
    }

    @PropertyName("tendiadiem")
    public void setTenDiaDiem(String tenDiaDiem) {
        this.tenDiaDiem = tenDiaDiem;
    }

    @PropertyName("mota")
    public String getMoTa() {
        return moTa;
    }

    @PropertyName("mota")
    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    @PropertyName("vitri")
    public String getViTri() {
        return viTri;
    }

    @PropertyName("vitri")
    public void setViTri(String viTri) {
        this.viTri = viTri;
    }

    @PropertyName("create_at")
    public String getCreateAt() {
        return createAt;
    }

    @PropertyName("create_at")
    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    @PropertyName("update_at")
    public String getUpdateAt() {
        return updateAt;
    }

    @PropertyName("update_at")
    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    @PropertyName("vote")
    public float getVote() {
        return vote;
    }

    @PropertyName("vote")
    public void setVote(float vote) {
        this.vote = vote;
    }

    @PropertyName("is_favourite")
    public boolean isFavorite() {
        return isFavorite;
    }

    @PropertyName("is_favourite")
    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
}
