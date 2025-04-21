package com.example.travel_app.Api;

import com.example.travel_app.Data.Model.GeoResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeoApiService {
    @GET("direct")
    Call<List<GeoResponse>> getLocationByName(
            @Query("q") String cityName,
            @Query("limit") String limit,
            @Query("appid") String apiKey
    );
}
