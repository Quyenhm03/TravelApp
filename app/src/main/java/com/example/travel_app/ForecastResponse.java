package com.example.travel_app;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastResponse {
    @SerializedName("cod")
    private String cod;
    @SerializedName("message")
    private int message;
    @SerializedName("cnt")
    private int cnt;
    @SerializedName("list")
    private List<ForecastItem> list;
    @SerializedName("city")
    private City city;

    public List<ForecastItem> getList() {
        return list;
    }

    public static class ForecastItem {
        @SerializedName("dt")
        private long dt;
        @SerializedName("main")
        private Main main;
        @SerializedName("weather")
        private List<Weather> weather;
        @SerializedName("clouds")
        private Clouds clouds;
        @SerializedName("wind")
        private Wind wind;
        @SerializedName("visibility")
        private int visibility;
        @SerializedName("pop")
        private float pop;
        @SerializedName("rain")
        private Rain rain;
        @SerializedName("sys")
        private Sys sys;
        @SerializedName("dt_txt")
        private String dtTxt;

        public long getDt() {
            return dt;
        }

        public Main getMain() {
            return main;
        }

        public List<Weather> getWeather() {
            return weather;
        }

        public Wind getWind() {
            return wind;
        }

        public String getDtTxt() {
            return dtTxt;
        }
    }

    public static class Main {
        @SerializedName("temp")
        private float temp;
        @SerializedName("feels_like")
        private float feelsLike;
        @SerializedName("temp_min")
        private float tempMin;
        @SerializedName("temp_max")
        private float tempMax;
        @SerializedName("pressure")
        private int pressure;
        @SerializedName("humidity")
        private int humidity;

        public float getTemp() {
            return temp;
        }

        public int getHumidity() {
            return humidity;
        }
    }

    public static class Weather {
        @SerializedName("id")
        private int id;
        @SerializedName("main")
        private String main;
        @SerializedName("description")
        private String description;
        @SerializedName("icon")
        private String icon;

        public String getDescription() {
            return description;
        }
    }

    public static class Clouds {
        @SerializedName("all")
        private int all;
    }

    public static class Wind {
        @SerializedName("speed")
        private float speed;
        @SerializedName("deg")
        private int deg;
        @SerializedName("gust")
        private float gust;

        public float getSpeed() {
            return speed;
        }
    }

    public static class Rain {
        @SerializedName("3h")
        private float rain3h;

        public float getRain3h() {
            return rain3h;
        }
    }

    public static class Sys {
        @SerializedName("pod")
        private String pod;
    }

    public static class City {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
        @SerializedName("coord")
        private Coord coord;
        @SerializedName("country")
        private String country;
        @SerializedName("population")
        private int population;
        @SerializedName("timezone")
        private int timezone;
        @SerializedName("sunrise")
        private long sunrise;
        @SerializedName("sunset")
        private long sunset;
    }

    public static class Coord {
        @SerializedName("lat")
        private double lat;
        @SerializedName("lon")
        private double lon;
    }
}