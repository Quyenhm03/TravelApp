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

    public LocationViewModel() {
        locationRepository = new LocationRepository();
    }

    public LiveData<Location> getLocation(int locationId) {
        return locationRepository.getLocation(locationId);
    }

    public LiveData<List<Location>> getAllLocations() {
        return locationRepository.getAllLocations();
    }
    public void updateLocationFavorite(int locationId, boolean isFavorite) {
        locationRepository.updateLocationFavorite(locationId, isFavorite);
    }

    public LiveData<List<Location>> getFavoriteLocationsByUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            Log.e(TAG, "userId không hợp lệ: " + userId);
            return new MutableLiveData<>(new ArrayList<>());
        }
        Log.d(TAG, "Lấy danh sách địa điểm yêu thích cho userId: " + userId);
        return locationRepository.getFavoriteLocationsByUserId(userId);
    }

}
















