package com.example.travel_app.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app.Data.Model.Hotel;
import com.example.travel_app.Data.Repository.HotelRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HotelViewModel extends ViewModel {
    private static final String TAG = "HotelViewModel";
    private final HotelRepository hotelRepository;
    private final MutableLiveData<List<Hotel>> hotelsLiveData;
    private final MutableLiveData<String> errorLiveData;
    private List<Hotel> hotelListCache;

    public HotelViewModel() {
        hotelRepository = new HotelRepository();
        hotelsLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
        hotelListCache = new ArrayList<>();
        fetchHotels();
    }

    public LiveData<List<Hotel>> getHotels() {
        return hotelsLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    private void fetchHotels() {
        hotelRepository.fetchHotels(new HotelRepository.HotelCallback() {
            @Override
            public void onSuccess(List<Hotel> hotels) {
                hotelListCache.clear();
                hotelListCache.addAll(hotels);
                hotelsLiveData.setValue(hotels);
            }

            @Override
            public void onFailure(String error) {
                Log.e(TAG, "Error from repository: " + error);
                errorLiveData.setValue(error);
            }
        });
    }

    public void sortHotels(String sortType, HotelRepository.HotelCallback callback) {
        if (hotelListCache == null || hotelListCache.isEmpty()) {
            callback.onFailure("No hotels available to sort");
            return;
        }

        List<Hotel> sortedList = new ArrayList<>(hotelListCache);

        switch (sortType) {
            case "price_asc":
               // sortedList.sort((h1, h2) -> Double.compare(h1.getPrice(), h2.getPrice()));
                Log.d(TAG, "Sorted list price_asc: " + sortedList);
                break;
            case "price_desc":
                //sortedList.sort((h1, h2) -> Double.compare(h2.getPrice(), h1.getPrice()));
                Log.d(TAG, "Sorted list price_desc: " + sortedList);
                break;
            case "rating_asc":
                sortedList.sort((h1, h2) -> Float.compare(h1.getRating(), h2.getRating()));
                Log.d(TAG, "Sorted list rating_asc: " + sortedList);
                break;
            case "rating_desc":
                sortedList.sort((h1, h2) -> Float.compare(h2.getRating(), h1.getRating()));
                Log.d(TAG, "Sorted list rating_desc: " + sortedList);
                break;
            default:
                Log.w(TAG, "Unknown sort type: " + sortType);
                break;
        }

        callback.onSuccess(sortedList);
    }
}
