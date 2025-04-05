package com.example.travel_app.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app.Data.Model.Flight;
import com.example.travel_app.Data.Repository.FlightRepository;

import java.util.List;

public class SearchFlightViewModel extends ViewModel {
    private FlightRepository flightRepository;

    public SearchFlightViewModel() {
        flightRepository = new FlightRepository();
    }

    public LiveData<List<Flight>> searchFlights(String departureAirportCode, String arrivalAirportCode, String departureDate, String seatType) {
        return flightRepository.searchFlights(departureAirportCode, arrivalAirportCode, departureDate, seatType);
    }
}
