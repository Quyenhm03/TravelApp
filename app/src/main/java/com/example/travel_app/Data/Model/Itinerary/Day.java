package com.example.travel_app.Data.Model.Itinerary;

import java.io.Serializable;
import java.util.List;

public class Day implements Serializable {
    private String date;
    private List<Location> locations; // Thay Item thành Location

    public Day() {
    }

    // Getters and Setters
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public List<Location> getLocations() { return locations; }
    public void setLocations(List<Location> locations) { this.locations = locations; }
}
