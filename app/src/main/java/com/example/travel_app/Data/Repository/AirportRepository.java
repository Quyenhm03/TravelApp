package com.example.travel_app.Data.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travel_app.Data.Model.Airport;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AirportRepository {
    private DatabaseReference databaseReference;

    public AirportRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Airport");
    }

    public LiveData<List<Airport>> getAirport() {
        MutableLiveData<List<Airport>> airportLiveData = new MutableLiveData<>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Airport> airportList = new ArrayList<>();
                for (DataSnapshot airportSnapshot : snapshot.getChildren()) {
                    Airport airport = airportSnapshot.getValue(Airport.class);
                    if (airport != null) {
                        airportList.add(airport);
                    }
                }
                airportLiveData.setValue(airportList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi: Đặt giá trị null hoặc thông báo lỗi
                airportLiveData.setValue(null);
                Log.e("AirportRepository", "Failed to load airports: " + error.getMessage());
            }
        });
        return airportLiveData;
    }
}
