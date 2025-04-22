package com.example.travel_app.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.travel_app.Data.Model.Location;
import com.example.travel_app.Data.Repository.LocationRepository;

import java.util.ArrayList;
import java.util.List;

public class LocationViewModel extends ViewModel {
    private final LocationRepository locationRepository;
    private static final String TAG = "LocationViewModel";

    private final MutableLiveData<List<Location>> searchResultsLiveData = new MutableLiveData<>();

    public LocationViewModel() {
        locationRepository = new LocationRepository();
    }

    // Lấy thông tin 1 địa điểm theo ID
    public LiveData<Location> getLocation(int locationId) {
        return locationRepository.getLocation(locationId);
    }

    // Lấy toàn bộ danh sách địa điểm
    public LiveData<List<Location>> getAllLocations() {
        return locationRepository.getAllLocations();
    }

    // Cập nhật yêu thích theo user
    public void updateLocationFavorite(String userId, String locationId, boolean isFavorite) {
        locationRepository.updateLocationFavorite(userId, locationId, isFavorite);
    }

    // Lấy danh sách địa điểm yêu thích theo user
    public LiveData<List<Location>> getFavoriteLocationsByUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            Log.e(TAG, "userId không hợp lệ: " + userId);
            return new MutableLiveData<>(new ArrayList<>());
        }
        Log.d(TAG, "Lấy danh sách địa điểm yêu thích cho userId: " + userId);
        return locationRepository.getFavoriteLocationsByUserId(userId);
    }


    public LiveData<List<Location>> getSearchResults() {
        return searchResultsLiveData;
    }

    public void searchLocations(String query) {
        locationRepository.searchLocations(query, new LocationRepository.SearchCallback() {
            @Override
            public void onSuccess(List<Location> locations) {
                searchResultsLiveData.setValue(locations);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Lỗi khi tìm kiếm địa điểm: " + e.getMessage());
                searchResultsLiveData.setValue(new ArrayList<>());
            }
        });
    }

    public void loadAllLocationsWithCallback() {
        locationRepository.getAllLocations(new LocationRepository.SearchCallback() {
            @Override
            public void onSuccess(List<Location> locations) {
                searchResultsLiveData.setValue(locations);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Lỗi khi lấy tất cả địa điểm: " + e.getMessage());
                searchResultsLiveData.setValue(new ArrayList<>());
            }
        });
    }

    public void getLocationById(int locationId, LocationRepository.LocationCallback callback) {
        locationRepository.getLocationById(locationId, callback);
    }
}
