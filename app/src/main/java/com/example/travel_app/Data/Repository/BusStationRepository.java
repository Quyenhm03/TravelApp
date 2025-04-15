package com.example.travel_app.Data.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travel_app.Data.Model.BusStation;
import com.example.travel_app.UI.Activity.SearchBusStationActivity;
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

    public LiveData<List<BusStation>> searchBusStations(String keyword) {
        MutableLiveData<List<BusStation>> busStationLiveData = new MutableLiveData<>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<BusStation> busStationList = new ArrayList<>();
                String query = SearchBusStationActivity.removeDiacritics(keyword.trim().toLowerCase());
                for (DataSnapshot busStationSnapshot : snapshot.getChildren()) {
                    BusStation busStation = busStationSnapshot.getValue(BusStation.class);
                    if (busStation != null) {
                        String busStationName = SearchBusStationActivity.removeDiacritics(busStation.getName().toLowerCase());
                        String busStationCity = SearchBusStationActivity.removeDiacritics(busStation.getCity().toLowerCase());
                        if (busStationName.contains(query) || busStationCity.contains(query)) {
                            busStationList.add(busStation);
                        }
                    }
                }
                busStationLiveData.setValue(busStationList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                busStationLiveData.setValue(null);
                Log.e("BusStationRepository", "Failed to search bus stations: " + error.getMessage());
            }
        });
        return busStationLiveData;
    }
}
