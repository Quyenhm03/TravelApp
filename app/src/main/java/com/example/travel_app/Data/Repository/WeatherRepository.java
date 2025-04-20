package com.example.travel_app.Data.Repository;

import android.util.Log;

import com.example.travel_app.Api.GeoApiService;
import com.example.travel_app.Api.WeatherApiService;
import com.example.travel_app.Data.Model.GeoResponse;
import com.example.travel_app.ForecastResponse;
import com.example.travel_app.WeatherResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherRepository {
    private WeatherApiService weatherApiService;
    private GeoApiService geoApiService;

    public WeatherRepository() {
        Retrofit weatherRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        weatherApiService = weatherRetrofit.create(WeatherApiService.class);

        Retrofit geoRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/geo/1.0/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        geoApiService = geoRetrofit.create(GeoApiService.class);

        Log.d("WeatherDebug", "WeatherRepository được khởi tạo");
    }

    public void getLocationByName(String cityName, Callback<List<GeoResponse>> callback) {
        Log.d("WeatherDebug", "Gửi request API địa lý cho: " + cityName);
        String apiKey = "79dc5bf71e665fc7d5f3c863dea47b5e";
        Call<List<GeoResponse>> call = geoApiService.getLocationByName(cityName, "1", apiKey);
        call.enqueue(callback);
    }

    public void getCoordinatesByLocationName(String cityName, Callback<List<GeoResponse>> callback) {
        Log.d("WeatherDebug", "Lấy tọa độ cho địa danh: " + cityName);
        String apiKey = "79dc5bf71e665fc7d5f3c863dea47b5e";
        Call<List<GeoResponse>> call = geoApiService.getLocationByName(cityName, "1", apiKey);
        call.enqueue(new Callback<List<GeoResponse>>() {
            @Override
            public void onResponse(Call<List<GeoResponse>> call, Response<List<GeoResponse>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    GeoResponse geoResponse = response.body().get(0);
                    Log.d("WeatherDebug", "Tọa độ tìm thấy: lat=" + geoResponse.getLat() + ", lon=" + geoResponse.getLon());
                    callback.onResponse(call, response);
                } else {
                    String errorMessage = response.message() != null ? response.message() : "Phản hồi rỗng";
                    Log.e("WeatherDebug", "Không tìm thấy tọa độ cho địa danh: " + cityName + ", mã trạng thái: " + response.code() + ", message: " + errorMessage + ", URL: " + call.request().url());
                    callback.onFailure(call, new Throwable("Không tìm thấy vị trí: " + cityName + " (mã: " + response.code() + ", message: " + errorMessage + ")"));
                }
            }

            @Override
            public void onFailure(Call<List<GeoResponse>> call, Throwable t) {
                Log.e("WeatherDebug", "Lỗi khi lấy tọa độ: " + t.getMessage() + ", URL: " + call.request().url());
                callback.onFailure(call, t);
            }
        });
    }

    public void get5DayWeather(double lat, double lon, Callback<ForecastResponse> callback) {
        Log.d("WeatherDebug", "Gửi request API thời tiết cho lat: " + lat + ", lon: " + lon);
        String apiKey = "79dc5bf71e665fc7d5f3c863dea47b5e";
        String units = "metric";
        Call<ForecastResponse> call = weatherApiService.get5DayForecast(lat, lon, units, apiKey);
        call.enqueue(callback);
    }
}


