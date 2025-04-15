package com.example.travel_app.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app.Data.Model.Airport;
import com.example.travel_app.Data.Repository.AirportRepository;

import java.util.List;

public class SearchAirportViewModel extends ViewModel {
    private AirportRepository airportRepository;
    private LiveData<List<Airport>> airports;
    private MutableLiveData<List<Airport>> searchResults;

    public SearchAirportViewModel() {
        airportRepository = new AirportRepository();
        airports = airportRepository.getAirport();
        searchResults = new MutableLiveData<>();
    }

    public LiveData<List<Airport>> getAirports() {
        return airports;
    }

    public LiveData<List<Airport>> getSearchResults() {
        return searchResults;
    }

    public void searchAirports(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            airports.observeForever(airportList -> {
                searchResults.setValue(airportList);
            });
        } else {
            airportRepository.searchAirports(keyword).observeForever(airportList -> {
                searchResults.setValue(airportList);
            });
        }
    }
}
