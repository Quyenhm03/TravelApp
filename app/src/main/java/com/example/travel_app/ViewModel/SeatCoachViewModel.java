package com.example.travel_app.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app.Data.Model.Coach;
import com.example.travel_app.Data.Repository.CoachRepository;

public class SeatCoachViewModel extends ViewModel {
    private CoachRepository repository;
    private LiveData<Coach> coachLiveData;
    private MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public SeatCoachViewModel() {
        repository = new CoachRepository();
    }

    public void loadCoachSeats(int coachId) {
        coachLiveData = repository.getCoachSeats(coachId);
    }

    public LiveData<Coach> getCoachLiveData() {
        return coachLiveData;
    }
}