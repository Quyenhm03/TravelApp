package com.example.travel_app.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app.Data.Model.BookingFlight;
import com.example.travel_app.Data.Repository.BookingFlightRepository;

public class BookingFlightViewModel extends ViewModel {
    private BookingFlightRepository repository;
    private MutableLiveData<Boolean> saveSuccess = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public BookingFlightViewModel() {
        repository = new BookingFlightRepository();
    }

    public void saveBooking(BookingFlight booking) {
        repository.saveBooking(booking, new BookingFlightRepository.Callback() {
            @Override
            public void onSuccess() {
                saveSuccess.postValue(true);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.postValue(e.getMessage());
            }
        });
    }

    public LiveData<Boolean> getSaveSuccess() {
        return saveSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}
