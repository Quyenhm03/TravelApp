package com.example.travel_app.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app.Data.Model.Day;
import com.example.travel_app.Data.Model.Item;
import com.example.travel_app.Data.Model.Itinerary;

import java.util.List;

public class ItineraryDetailViewModel extends ViewModel {
    private MutableLiveData<List<Day>> daysLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Item>> itemsLiveData = new MutableLiveData<>();
    private Itinerary itinerary;

    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
        daysLiveData.setValue(itinerary.getDays());
        // Mặc định hiển thị ngày đầu tiên
        if (itinerary.getDays() != null && !itinerary.getDays().isEmpty()) {
            itemsLiveData.setValue(itinerary.getDays().get(0).getItems());
        }
    }

    public LiveData<List<Day>> getDaysLiveData() {
        return daysLiveData;
    }

    public LiveData<List<Item>> getItemsLiveData() {
        return itemsLiveData;
    }

    public void selectDay(int dayIndex) {
        if (itinerary.getDays() != null && dayIndex < itinerary.getDays().size()) {
            itemsLiveData.setValue(itinerary.getDays().get(dayIndex).getItems());
        }
    }

    public Itinerary getItinerary() {
        return itinerary;
    }
}
