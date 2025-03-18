package com.example.travel_app.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app.Data.Model.Airport;
import com.example.travel_app.Data.Repository.AirportRepository;

import java.util.List;

public class SearchAirportViewModel extends ViewModel {
    private AirportRepository airportRepository;
    private LiveData<List<Airport>> airports;

    public SearchAirportViewModel() {
        airportRepository = new AirportRepository();
        airports = airportRepository.getAirport();
    }

    public LiveData<List<Airport>> getAirports() {
        return airports;
    }
}
