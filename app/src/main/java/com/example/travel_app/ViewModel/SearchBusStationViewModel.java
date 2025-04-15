package com.example.travel_app.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app.Data.Model.BusStation;
import com.example.travel_app.Data.Repository.BusStationRepository;

import java.util.List;

public class SearchBusStationViewModel extends ViewModel {
    private BusStationRepository busStationRepository;
    private LiveData<List<BusStation>> busStations;
    private MutableLiveData<List<BusStation>> searchResults;

    public SearchBusStationViewModel() {
        busStationRepository = new BusStationRepository();
        busStations = busStationRepository.getBusStations();
        searchResults = new MutableLiveData<>();
    }

    public LiveData<List<BusStation>> getBusStations() {
        return busStations;
    }

    public LiveData<List<BusStation>> getSearchResults() {
        return searchResults;
    }

    public void searchBusStations(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            busStations.observeForever(busStationList -> {
                searchResults.setValue(busStationList);
            });
        } else {
            busStationRepository.searchBusStations(keyword).observeForever(busStationList -> {
                searchResults.setValue(busStationList);
            });
        }
    }
}