package com.example.travel_app.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app.Data.Model.BookingFlight;
import com.example.travel_app.Data.Model.Passenger;

import java.util.List;

public class BookingDetailViewModel extends ViewModel {
    private MutableLiveData<BookingFlight> bookingLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Passenger>> passengerListLiveData = new MutableLiveData<>();

    public void setBooking(BookingFlight booking) {
        bookingLiveData.setValue(booking);
        passengerListLiveData.setValue(booking.getPassengerList());
    }

    public LiveData<BookingFlight> getBooking() {
        return bookingLiveData;
    }

    public LiveData<List<Passenger>> getPassengerList() {
        return passengerListLiveData;
    }
}
