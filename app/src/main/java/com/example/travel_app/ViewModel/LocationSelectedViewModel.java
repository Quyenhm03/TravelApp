package com.example.travel_app.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app.Data.Model.Location;

public class LocationSelectedViewModel extends ViewModel {
    private MutableLiveData<Location> mutableLiveData;
    public LocationSelectedViewModel() {
        mutableLiveData = new MutableLiveData<>();
    }
    public void setLocation(Location location) {
        mutableLiveData.setValue(location);
    }
    public LiveData<Location> getLocation() {
        return mutableLiveData;
    }
}
