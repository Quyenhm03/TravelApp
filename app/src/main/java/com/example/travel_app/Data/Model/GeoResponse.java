package com.example.travel_app.Data.Model;

public class GeoResponse {
    private double lat;  // Vĩ độ
    private double lon;  // Kinh độ
    private String name; // Tên thành phố

    // Getter và Setter cho lat và lon
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
