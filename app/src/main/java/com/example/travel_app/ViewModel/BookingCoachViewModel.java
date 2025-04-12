package com.example.travel_app.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app.Data.Model.BookingCoach;
import com.example.travel_app.Data.Repository.BookingCoachRepository;

public class BookingCoachViewModel extends ViewModel {
    private BookingCoachRepository repository;
    private MutableLiveData<Boolean> saveSuccess = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public BookingCoachViewModel() {
        repository = new BookingCoachRepository();
    }

    public void saveBooking(BookingCoach booking) {
        repository.saveBooking(booking, new BookingCoachRepository.Callback() {
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