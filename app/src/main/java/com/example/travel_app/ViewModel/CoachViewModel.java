package com.example.travel_app.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app.Data.Model.Coach;
import com.example.travel_app.Data.Repository.CoachRepository;

import java.util.List;

public class CoachViewModel extends ViewModel {
    private CoachRepository coachRepository;

    public CoachViewModel() {
        coachRepository = new CoachRepository();
    }

    public LiveData<List<Coach>> getCoachesHome() {
        return coachRepository.getCoachesHome();
    }
}