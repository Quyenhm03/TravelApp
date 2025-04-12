package com.example.travel_app.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app.Data.Model.Coach;
import com.example.travel_app.Data.Model.FilterCoach;
import com.example.travel_app.Data.Model.SortOption;
import com.example.travel_app.Data.Repository.CoachRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SearchCoachViewModel extends ViewModel {
    private CoachRepository coachRepository;
    private MutableLiveData<List<Coach>> filteredCoachesLiveData = new MutableLiveData<>();
    private List<Coach> allCoaches = new ArrayList<>();
    private FilterCoach filterCriteria = new FilterCoach();
    private SortOption sortOption;

    public SearchCoachViewModel() {
        coachRepository = new CoachRepository();
    }

    public LiveData<List<Coach>> searchCoaches(String departureStationName, String arrivalStationName, String departureDate) {
        MutableLiveData<List<Coach>> coachLiveData = new MutableLiveData<>();
        coachRepository.searchCoaches(departureStationName, arrivalStationName, departureDate).observeForever(coaches -> {
            if (coaches != null) {
                allCoaches = new ArrayList<>(coaches);
                applyFilterAndSort();
                coachLiveData.setValue(coaches);
            } else {
                coachLiveData.setValue(null);
            }
        });
        return coachLiveData;
    }

    public LiveData<List<Coach>> getFilteredCoachesLiveData() {
        return filteredCoachesLiveData;
    }

    public void setFilterCriteria(FilterCoach criteria) {
        this.filterCriteria = criteria;
        applyFilterAndSort();
    }

    public void setSortOption(SortOption option) {
        this.sortOption = option;
        applyFilterAndSort();
    }

    private void applyFilterAndSort() {
        List<Coach> filteredCoaches = new ArrayList<>(allCoaches);

        // Lọc theo giá
        filteredCoaches = filteredCoaches.stream()
                .filter(coach -> {
                    boolean matchesPrice = coach.getPrice() >= filterCriteria.getMinPrice() && coach.getPrice() <= filterCriteria.getMaxPrice();
                    return matchesPrice;
                })
                .collect(Collectors.toList());

        // Sắp xếp
        if (sortOption != null) {
            switch (sortOption) {
                case PRICE_ASC:
                    filteredCoaches.sort(Comparator.comparingDouble(Coach::getPrice));
                    break;
                case PRICE_DESC:
                    filteredCoaches.sort((c1, c2) -> Double.compare(c2.getPrice(), c1.getPrice()));
                    break;
                case DEPARTURE_EARLY:
                    filteredCoaches.sort(Comparator.comparing(Coach::getDepartureTime));
                    break;
                case DEPARTURE_LATE:
                    filteredCoaches.sort((c1, c2) -> c2.getDepartureTime().compareTo(c1.getDepartureTime()));
                    break;
            }
        }

        filteredCoachesLiveData.setValue(filteredCoaches);
    }
}