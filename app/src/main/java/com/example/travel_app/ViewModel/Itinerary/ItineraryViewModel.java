package com.example.travel_app.ViewModel.Itinerary;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app.Data.Model.Itinerary.Day;
import com.example.travel_app.Data.Model.Itinerary.Itinerary;

import com.example.travel_app.Data.Model.Location;
import com.example.travel_app.Data.Repository.Itinerary.ItineraryRepository;
import com.example.travel_app.Data.Repository.Itinerary.LocationRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ItineraryViewModel extends ViewModel {
    private ItineraryRepository itineraryRepository;
    private LocationRepository locationRepository;
    private MutableLiveData<Itinerary> itineraryLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Location>> searchResultsLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> selectedDayIndex = new MutableLiveData<>(0);

    public ItineraryViewModel() {
        itineraryRepository = new ItineraryRepository();
        locationRepository = new LocationRepository();
        itineraryLiveData.setValue(new Itinerary());
    }

    public LiveData<Itinerary> getItineraryLiveData() {
        return itineraryLiveData;
    }

    public LiveData<List<Location>> getSearchResultsLiveData() {
        return searchResultsLiveData;
    }

    public LiveData<Integer> getSelectedDayIndex() {
        return selectedDayIndex;
    }

    public void setItinerary(Itinerary itinerary) {
        itineraryLiveData.setValue(itinerary);
    }

    public Itinerary setItineraryInfo(String title, String startDate, String endDate, String userId, String userName) {
        Itinerary itinerary = new Itinerary();
        itinerary.setTitle(title);
        itinerary.setUserId(userId);
        itinerary.setUserName(userName);
        itinerary.setIsShare(false);
        itinerary.setCreateDate(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));

        List<Day> days = generateDays(startDate, endDate);
        itinerary.setDays(days);

        itineraryLiveData.setValue(itinerary);
        return itinerary;
    }

    public void searchLocations(String query) {
        locationRepository.searchLocations(query, new LocationRepository.SearchCallback() {
            @Override
            public void onSuccess(List<Location> locations) {
                searchResultsLiveData.setValue(locations);
            }

            @Override
            public void onFailure(Exception e) {
                searchResultsLiveData.setValue(new ArrayList<>());
                Log.e("ItineraryViewModel", "Tìm kiếm địa điểm thất bại: " + e.getMessage());
            }
        });
    }

    public void getAllLocations() {
        locationRepository.getAllLocations(new LocationRepository.SearchCallback() {
            @Override
            public void onSuccess(List<Location> locations) {
                searchResultsLiveData.setValue(locations);
            }

            @Override
            public void onFailure(Exception e) {
                searchResultsLiveData.setValue(new ArrayList<>());
                Log.e("ItineraryViewModel", "Lấy tất cả địa điểm thất bại: " + e.getMessage());
            }
        });
    }

    public void addLocationToDay(int dayIndex, Location location) {
        Itinerary itinerary = itineraryLiveData.getValue();
        if (itinerary != null && dayIndex >= 0 && dayIndex < itinerary.getDays().size()) {
            List<Location> locations = itinerary.getDays().get(dayIndex).getLocations();
            if (locations == null) {
                locations = new ArrayList<>();
                itinerary.getDays().get(dayIndex).setLocations(locations);
            }
            locations.add(location);
            itineraryLiveData.setValue(itinerary);
        }
    }

    public void removeLocationFromDay(int dayIndex, Location location) {
        Itinerary itinerary = itineraryLiveData.getValue();
        if (itinerary != null && dayIndex >= 0 && dayIndex < itinerary.getDays().size()) {
            List<Location> locations = itinerary.getDays().get(dayIndex).getLocations();
            if (locations != null && locations.remove(location)) {
                itineraryLiveData.setValue(itinerary);
            }
        }
    }

    public void setSelectedDay(int dayIndex) {
        selectedDayIndex.setValue(dayIndex);
    }

    public void saveItinerary(Callback callback) {
        Itinerary itinerary = itineraryLiveData.getValue();
        if (itinerary != null) {
            itineraryRepository.saveItinerary(itinerary, new ItineraryRepository.Callback() {
                @Override
                public void onSuccess() {
                    callback.onSuccess();
                }

                @Override
                public void onFailure(Exception e) {
                    callback.onFailure(e);
                }
            });
        } else {
            callback.onFailure(new IllegalStateException("Dữ liệu hành trình rỗng"));
        }
    }

    private List<Day> generateDays(String startDate, String endDate) {
        List<Day> days = new ArrayList<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            long diff = end.getTime() - start.getTime();
            int numDays = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start);
            for (int i = 0; i < numDays; i++) {
                Day day = new Day();
                day.setDate(sdf.format(calendar.getTime()));
                day.setLocations(new ArrayList<>());
                days.add(day);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    public interface Callback {
        void onSuccess();
        void onFailure(Exception e);
    }
}