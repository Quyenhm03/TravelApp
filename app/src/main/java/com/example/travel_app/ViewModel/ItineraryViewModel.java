package com.example.travel_app.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app.Data.Model.Day;
import com.example.travel_app.Data.Model.Item;
import com.example.travel_app.Data.Model.Itinerary;
import com.example.travel_app.Data.Repository.ItineraryRepository;
import com.example.travel_app.Data.Repository.ItemRepository;

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
    private ItemRepository itemRepository;
    private MutableLiveData<Itinerary> itineraryLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Item>> searchResultsLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> selectedDayIndex = new MutableLiveData<>(0);

    public ItineraryViewModel() {
        itineraryRepository = new ItineraryRepository();
        itemRepository = new ItemRepository();
        itineraryLiveData.setValue(new Itinerary());
    }

    public LiveData<Itinerary> getItineraryLiveData() {
        return itineraryLiveData;
    }

    public LiveData<List<Item>> getSearchResultsLiveData() {
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

        Log.d("ItineraryViewModel", "Title: " + title + ", StartDate: " + startDate + ", EndDate: " + endDate);
        Log.d("ItineraryViewModel", "Days generated: " + (days != null ? days.size() : "null"));

        itineraryLiveData.setValue(itinerary);
        return itinerary;
    }

    public void searchItems(String query) {
        itemRepository.searchItems(query, new ItemRepository.SearchCallback() {
            @Override
            public void onSuccess(List<Item> items) {
                searchResultsLiveData.setValue(items);
            }

            @Override
            public void onFailure(Exception e) {
                searchResultsLiveData.setValue(new ArrayList<>());
            }
        });
    }

    public void addItemToDay(int dayIndex, Item item) {
        Itinerary itinerary = itineraryLiveData.getValue();
        if (itinerary != null && dayIndex >= 0 && dayIndex < itinerary.getDays().size()) {
            List<Item> items = itinerary.getDays().get(dayIndex).getItems();
            if (items == null) {
                items = new ArrayList<>();
                itinerary.getDays().get(dayIndex).setItems(items);
            }
            items.add(item);
            itineraryLiveData.setValue(itinerary);
            Log.d("ItineraryViewModel", "Item added to day " + dayIndex);
        }
    }

    public void removeItemFromDay(int dayIndex, Item item) {
        Itinerary itinerary = itineraryLiveData.getValue();
        if (itinerary != null && dayIndex >= 0 && dayIndex < itinerary.getDays().size()) {
            List<Item> items = itinerary.getDays().get(dayIndex).getItems();
            if (items != null && items.remove(item)) {
                itineraryLiveData.setValue(itinerary);
                Log.d("ItineraryViewModel", "Item removed from day " + dayIndex);
            }
        }
    }

    public void setSelectedDay(int dayIndex) {
        selectedDayIndex.setValue(dayIndex);
    }

    public void saveItinerary(Callback callback) {
        Itinerary itinerary = itineraryLiveData.getValue();
        if (itinerary != null) {
            Log.d("ItineraryViewModel", "Saving itinerary: " + itinerary.getTitle() + ", ID: " + itinerary.getId());
            itineraryRepository.saveItinerary(itinerary, new ItineraryRepository.Callback() {
                @Override
                public void onSuccess() {
                    Log.d("ItineraryViewModel", "Itinerary saved successfully with ID: " + itinerary.getId());
                    callback.onSuccess();
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e("ItineraryViewModel", "Failed to save itinerary: " + e.getMessage(), e);
                    callback.onFailure(e);
                }
            });
        } else {
            Log.e("ItineraryViewModel", "Itinerary is null, cannot save");
            callback.onFailure(new IllegalStateException("Itinerary data is null"));
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

            Log.d("ItineraryViewModel", "Start: " + start + ", End: " + end + ", NumDays: " + numDays);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start);
            for (int i = 0; i < numDays; i++) {
                Day day = new Day();
                day.setDate(sdf.format(calendar.getTime()));
                day.setItems(new ArrayList<>());
                days.add(day);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (ParseException e) {
            Log.e("ItineraryViewModel", "ParseException: " + e.getMessage());
            e.printStackTrace();
        }
        return days;
    }

    public interface Callback {
        void onSuccess();
        void onFailure(Exception e);
    }
}