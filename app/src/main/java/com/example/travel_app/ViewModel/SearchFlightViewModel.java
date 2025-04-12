package com.example.travel_app.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app.Data.Model.FilterCriteria;
import com.example.travel_app.Data.Model.Flight;
import com.example.travel_app.Data.Model.SortOption;
import com.example.travel_app.Data.Repository.FlightRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SearchFlightViewModel extends ViewModel {

    private FlightRepository flightRepository;
    private MutableLiveData<List<Flight>> filteredFlightsLiveData = new MutableLiveData<>();
    private List<Flight> allFlights = new ArrayList<>();
    private FilterCriteria filterCriteria = new FilterCriteria();
    private SortOption sortOption;

    public SearchFlightViewModel() {
        flightRepository = new FlightRepository();
    }

    public LiveData<List<Flight>> searchFlights(String departureAirportCode, String arrivalAirportCode, String departureDate, String seatType) {
        MutableLiveData<List<Flight>> flightLiveData = new MutableLiveData<>();
        flightRepository.searchFlights(departureAirportCode, arrivalAirportCode, departureDate, seatType).observeForever(flights -> {
            if (flights != null) {
                allFlights = new ArrayList<>(flights);
                applyFilterAndSort();
                flightLiveData.setValue(flights);
            } else {
                flightLiveData.setValue(null);
            }
        });
        return flightLiveData;
    }

    public LiveData<List<Flight>> getFilteredFlightsLiveData() {
        return filteredFlightsLiveData;
    }

    public void setFilterCriteria(FilterCriteria criteria) {
        this.filterCriteria = criteria;
        applyFilterAndSort();
    }

    public void setSortOption(SortOption option) {
        this.sortOption = option;
        applyFilterAndSort();
    }

    private void applyFilterAndSort() {
        List<Flight> filteredFlights = new ArrayList<>(allFlights);

        // Lọc
        filteredFlights = filteredFlights.stream()
                .filter(flight -> {
                    boolean matchesAirline = filterCriteria.getAirlines().isEmpty() || filterCriteria.getAirlines().contains(flight.getAirline());
                    boolean matchesPrice = flight.getPrice() >= filterCriteria.getMinPrice() && flight.getPrice() <= filterCriteria.getMaxPrice();
                    return matchesAirline && matchesPrice;
                })
                .collect(Collectors.toList());

        // Sắp xếp
        if (sortOption != null) {
            switch (sortOption) {
                case PRICE_ASC:
                    filteredFlights.sort(Comparator.comparingDouble(Flight::getPrice));
                    break;
                case PRICE_DESC:
                    filteredFlights.sort((f1, f2) -> Double.compare(f2.getPrice(), f1.getPrice()));
                    break;
                case DEPARTURE_EARLY:
                    filteredFlights.sort(Comparator.comparing(Flight::getDepartureTime));
                    break;
                case DEPARTURE_LATE:
                    filteredFlights.sort((f1, f2) -> f2.getDepartureTime().compareTo(f1.getDepartureTime()));
                    break;
            }
        }

        filteredFlightsLiveData.setValue(filteredFlights);
    }

}
