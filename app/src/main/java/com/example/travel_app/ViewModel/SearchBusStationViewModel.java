package com.example.travel_app.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app.Data.Model.BusStation;
import com.example.travel_app.Data.Repository.BusStationRepository;

import java.util.List;

public class SearchBusStationViewModel extends ViewModel {
    private BusStationRepository busStationRepository;
    private LiveData<List<BusStation>> busStations;

    public SearchBusStationViewModel() {
        busStationRepository = new BusStationRepository();
        busStations = busStationRepository.getBusStations();
    }

    public LiveData<List<BusStation>> getBusStations() {
        return busStations;
    }
}
