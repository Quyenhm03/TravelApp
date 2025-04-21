package com.example.travel_app.ViewModel;

import android.util.Log;

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
        Log.d("HomeFragmentTAG_LocationSelectedViewModel", "setLocation: " + location.getTenDiaDiem());
        mutableLiveData.setValue(location);
    }
    public LiveData<Location> getLocation() {
        return mutableLiveData;
    }
}
