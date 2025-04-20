package com.example.travel_app.Api;

import com.example.travel_app.Data.Model.GeoResponse;
import com.example.travel_app.WeatherResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    @GET("onecall")
    Call<WeatherResponse> get7DayForecast(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("exclude") String exclude,
            @Query("units") String units,
            @Query("appid") String apiKey
    );

    @GET("geo/1.0/direct")
    Call<List<GeoResponse>> getLocationByName(
            @Query("q") String cityName,
            @Query("limit") String limit,
            @Query("appid") String apiKey
    );

}



