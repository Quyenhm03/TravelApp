package com.example.travel_app.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app.Data.Model.Bookings;
import com.example.travel_app.Data.Repository.BookingRepository;

public class BookingHotelViewModel extends ViewModel {
    private BookingRepository bookingRepository = BookingRepository.getInstance();
    private MutableLiveData<Boolean> saveSuccess = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<Boolean> getSaveSuccess() {
        return saveSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public Bookings getBookingById(int bookingId) {
        return bookingRepository.getBookingById(bookingId);
    }

    public void saveBooking(Bookings booking) {
        try {
            bookingRepository.saveBooking(booking);
            saveSuccess.postValue(true);
        } catch (Exception e) {
            errorMessage.postValue(e.getMessage());
        }
    }
}

