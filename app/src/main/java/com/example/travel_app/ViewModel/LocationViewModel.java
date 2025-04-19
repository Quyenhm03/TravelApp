package com.example.travel_app.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.travel_app.Data.Model.Location;
import com.example.travel_app.Data.Repository.LocationRepository;

import java.util.List;

public class LocationViewModel extends ViewModel {
    private final LocationRepository locationRepository;

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

}
















