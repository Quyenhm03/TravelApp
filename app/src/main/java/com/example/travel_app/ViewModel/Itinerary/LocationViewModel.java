package com.example.travel_app.ViewModel.Itinerary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.travel_app.Data.Model.Location;
import com.example.travel_app.Data.Repository.Itinerary.LocationRepository;

import java.util.ArrayList;
import java.util.List;

public class LocationViewModel extends ViewModel {
    private LocationRepository repository;
    private MutableLiveData<List<Location>> locationsLiveData = new MutableLiveData<>();

    public LocationViewModel() {
        repository = new LocationRepository();
        loadLocations();
    }

    public LiveData<List<Location>> getLocationsHome() {
        return locationsLiveData;
    }

    private void loadLocations() {
        repository.getAllLocations(new LocationRepository.SearchCallback() {
            @Override
            public void onSuccess(List<Location> locations) {
                locationsLiveData.setValue(locations);
            }

            @Override
            public void onFailure(Exception e) {
                locationsLiveData.setValue(new ArrayList<>());
            }
        });
    }
}
