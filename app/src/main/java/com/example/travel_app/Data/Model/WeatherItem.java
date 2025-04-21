package com.example.travel_app.Data.Model;

public class WeatherItem {
    private String description;
    private String temperature;
    private String windSpeed;

    public WeatherItem(String description, String temperature, String windSpeed) {
        this.description = description;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
    }

    public String getDescription() {
        return description;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getWindSpeed() {
        return windSpeed;
    }
}


