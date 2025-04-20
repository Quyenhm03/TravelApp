package com.example.travel_app;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherResponse {

    @SerializedName("daily")
    private List<Daily> daily;

    @SerializedName("timezone")
    private String timezone;

    public List<Daily> getDaily() {
        return daily;
    }

    public String getTimezone() {
        return timezone;
    }

    // Lớp Daily chứa thông tin dự báo thời tiết hàng ngày
    public static class Daily {

        @SerializedName("dt")
        private long date;

        @SerializedName("temp")
        private Temp temp;

        @SerializedName("weather")
        private List<Weather> weather;

        @SerializedName("wind_speed")
        private float windSpeed;

        public long getDate() {
            return date;
        }

        public Temp getTemp() {
            return temp;
        }

        public List<Weather> getWeather() {
            return weather;
        }

        public float getWindSpeed() {
            return windSpeed;
        }

        public static class Temp {
            @SerializedName("day")
            private float day;

            public float getDay() {
                return day;
            }
        }

        public static class Weather {
            @SerializedName("description")
            private String description;

            public String getDescription() {
                return description;
            }
        }
    }
}
