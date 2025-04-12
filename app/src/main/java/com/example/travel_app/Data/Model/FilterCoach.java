package com.example.travel_app.Data.Model;

public class FilterCoach {
    private double minPrice;
    private double maxPrice;

    public FilterCoach() {
        minPrice = 0;
        maxPrice = Double.MAX_VALUE;
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
