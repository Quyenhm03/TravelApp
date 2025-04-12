package com.example.travel_app.Data.Model;

import java.util.ArrayList;
import java.util.List;

public class FilterCriteria {
    private List<String> airlines;
    private double minPrice;
    private double maxPrice;

    public FilterCriteria() {
        airlines = new ArrayList<>();
        minPrice = 0;
        maxPrice = Double.MAX_VALUE;
    }

    public List<String> getAirlines() {
        return airlines;
    }

    public void setAirlines(List<String> airlines) {
        this.airlines = airlines;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }
}