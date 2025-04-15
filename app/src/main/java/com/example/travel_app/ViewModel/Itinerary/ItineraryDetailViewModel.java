package com.example.travel_app.ViewModel.Itinerary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app.Data.Model.Itinerary.Day;
import com.example.travel_app.Data.Model.Itinerary.Itinerary;
import com.example.travel_app.Data.Model.Itinerary.Location;

import java.util.List;

public class ItineraryDetailViewModel extends ViewModel {
    private MutableLiveData<List<Day>> daysLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Location>> locationsLiveData = new MutableLiveData<>();
    private Itinerary itinerary;

    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
        daysLiveData.setValue(itinerary.getDays());
        if (itinerary.getDays() != null && !itinerary.getDays().isEmpty()) {
            locationsLiveData.setValue(itinerary.getDays().get(0).getLocations());
        }
    }

    public LiveData<List<Day>> getDaysLiveData() {
        return daysLiveData;
    }

    public LiveData<List<Location>> getLocationsLiveData() {
        return locationsLiveData;
    }

    public void selectDay(int dayIndex) {
        if (itinerary.getDays() != null && dayIndex < itinerary.getDays().size()) {
            locationsLiveData.setValue(itinerary.getDays().get(dayIndex).getLocations());
        }
    }

    public Itinerary getItinerary() {
        return itinerary;
    }
}
