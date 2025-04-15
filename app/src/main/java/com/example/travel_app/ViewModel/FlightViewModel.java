package com.example.travel_app.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app.Data.Model.Flight;
import com.example.travel_app.Data.Repository.FlightRepository;

import java.util.List;

public class FlightViewModel extends ViewModel {
    private FlightRepository flightRepository;

    public FlightViewModel() {
        flightRepository = new FlightRepository();
    }

    public LiveData<List<Flight>> getFlightsHome() {
        return flightRepository.getFlightsHome();
    }
}