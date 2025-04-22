package com.example.travel_app.ViewModel.Itinerary;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travel_app.Data.Model.BookingHotel;
import com.example.travel_app.Data.Model.Payment;
import com.example.travel_app.Data.Repository.BookingHotelRepository;

import java.util.List;

public class BookingHotelViewModel extends AndroidViewModel {
    private BookingHotelRepository repository;

    public BookingHotelViewModel(@NonNull Application application) {
        super(application);
        repository = new BookingHotelRepository();
    }

    public LiveData<Boolean> getSaveSuccess() {
        return repository.getSaveSuccess();
    }

    public LiveData<String> getErrorMessage() {
        return repository.getErrorMessage();
    }

    public void saveBooking(BookingHotel bookingHotel, Payment payment, String hotelId, String roomType) {
        repository.saveBooking(bookingHotel, payment, hotelId, roomType);
    }

//    public LiveData<List<BookingHotel>> getBookingsByUserId(String userId) {
//        return repository.getBookingsByUserId(userId);
//    }

    public void loadBookingsForUser(String userId) {
        repository.fetchBookingsByUserId(userId);
    }

    public MutableLiveData<List<BookingHotel>> getBookingsByUserIdLiveData() {
        return repository.getBookingsByUserIdLiveData();
    }

}