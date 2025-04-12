package com.example.travel_app.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app.Data.Model.Flight;
import com.example.travel_app.Data.Repository.FlightRepository;

public class SeatFlightViewModel extends ViewModel {
    private FlightRepository repository;
    private LiveData<Flight> flightLiveData;
    private MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public SeatFlightViewModel() {
        repository = new FlightRepository();
    }

    public void loadFlightSeats(int flightId) {
        flightLiveData = repository.getFlightSeats(flightId);
    }

    public LiveData<Flight> getFlightLiveData() { return flightLiveData; }
}
