package com.example.travel_app.ViewModel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app.Data.Model.Hotel;
import com.example.travel_app.Data.Repository.HotelRepository;

public class HotelDetailViewModel extends ViewModel {
    private final MutableLiveData<Hotel> selectedHotel = new MutableLiveData<>();
    private final HotelRepository hotelRepository;

    public HotelDetailViewModel() {
        hotelRepository = new HotelRepository();
    }

    public LiveData<Hotel> getSelectedHotel() {
        return selectedHotel;
    }

    public void fetchHotelById(String hotelId) {
        hotelRepository.fetchHotelById(hotelId, new HotelRepository.SingleHotelCallback() {
            @Override
            public void onSuccess(Hotel hotel) {
                selectedHotel.setValue(hotel);
            }

            @Override
            public void onFailure(String error) {
                selectedHotel.setValue(null);
            }
        });
    }
}
