package com.example.travel_app.Data.Repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travel_app.Data.Model.BusStation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BusStationRepository {
    private DatabaseReference databaseReference;

    public BusStationRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference("BusStation");
    }

    public LiveData<List<BusStation>> getBusStations() {
        MutableLiveData<List<BusStation>> busStationLiveData = new MutableLiveData<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<BusStation> busStations = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BusStation busStation = dataSnapshot.getValue(BusStation.class);
                    if (busStation != null) {
                        busStations.add(busStation);
                    }
                }
                busStationLiveData.setValue(busStations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                busStationLiveData.setValue(null);
            }
        });
        return busStationLiveData;
    }
}
