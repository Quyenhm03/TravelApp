package com.example.travel_app.Data.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travel_app.Data.Model.Coach;
import com.example.travel_app.Data.Model.Seat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CoachRepository {
    private DatabaseReference databaseReference;

    public CoachRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Coach");
    }

    public LiveData<List<Coach>> searchCoaches(String departureStationName, String arrivalStationName, String departureDate) {
        MutableLiveData<List<Coach>> coachLiveData = new MutableLiveData<>();

        Query query = databaseReference.orderByChild("departureStationName").equalTo(departureStationName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Coach> coachList = new ArrayList<>();
                for (DataSnapshot coachSnapshot : snapshot.getChildren()) {
                    Coach coach = coachSnapshot.getValue(Coach.class);
                    if (coach != null &&
                            coach.getArrivalStationName().equals(arrivalStationName) &&
                            coach.getDepartureDate().equals(departureDate)) {
                        coachList.add(coach);
                    }
                }
                coachLiveData.setValue(coachList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                coachLiveData.setValue(null);
                Log.e("CoachRepository", "Failed to load coaches: " + error.getMessage());
            }
        });
        return coachLiveData;
    }

    public LiveData<Coach> getCoachSeats(int coachId) {
        MutableLiveData<Coach> coachLiveData = new MutableLiveData<>();
        databaseReference.child(String.valueOf(coachId)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Coach coach = snapshot.getValue(Coach.class);
                if (coach != null) {
                    List<Seat> seats = new ArrayList<>();
                    DataSnapshot seatsSnapshot = snapshot.child("seats");
                    for (DataSnapshot seatSnapshot : seatsSnapshot.getChildren()) {
                        Seat seat = seatSnapshot.getValue(Seat.class);
                        if (seat != null) {
                            seats.add(seat);
                        }
                    }
                    coach.setSeats(seats);
                    coachLiveData.setValue(coach);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                coachLiveData.setValue(null);
            }
        });
        return coachLiveData;
    }

    public LiveData<List<Coach>> getCoachesHome() {
        MutableLiveData<List<Coach>> coachLiveData = new MutableLiveData<>();

        databaseReference.limitToFirst(30).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Coach> coachList = new ArrayList<>();
                for (DataSnapshot coachSnapshot : snapshot.getChildren()) {
                    Coach coach = coachSnapshot.getValue(Coach.class);
                    if (coach != null) {
                        coachList.add(coach);
                    }
                }
                coachLiveData.setValue(coachList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CoachRepository", "Failed to load coaches: " + error.getMessage());
                coachLiveData.setValue(null);
            }
        });
        return coachLiveData;
    }
}