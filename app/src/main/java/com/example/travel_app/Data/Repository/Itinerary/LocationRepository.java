package com.example.travel_app.Data.Repository.Itinerary;

import android.util.Log;

import androidx.annotation.NonNull;


import com.example.travel_app.Data.Model.Location;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LocationRepository {
    private DatabaseReference locationsRef;

    public LocationRepository() {
        locationsRef = FirebaseDatabase.getInstance().getReference("Location");
    }

    public void searchLocations(String query, SearchCallback callback) {
        locationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Location> result = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Location location = snapshot.getValue(Location.class);
                    if (location != null && (location.getTenDiaDiem().toLowerCase().contains(query.toLowerCase())
                            || location.getViTri().toLowerCase().contains(query.toLowerCase()))) {
                        result.add(location);
                    }
                }
                Log.d("LocationRepository", "Found " + result.size() + " locations for query: " + query);
                callback.onSuccess(result);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("LocationRepository", "Search locations failed: " + databaseError.getMessage());
                callback.onFailure(databaseError.toException());
            }
        });
    }

    public void getAllLocations(SearchCallback callback) {
        locationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Location> result = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Location location = snapshot.getValue(Location.class);
                    if (location != null) {
                        result.add(location);
                    }
                }
                Log.d("LocationRepository", "Lấy được " + result.size() + " địa điểm");
                callback.onSuccess(result);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("LocationRepository", "Lấy tất cả địa điểm thất bại: " + databaseError.getMessage());
                callback.onFailure(databaseError.toException());
            }
        });
    }

    public interface SearchCallback {
        void onSuccess(List<Location> locations);
        void onFailure(Exception e);
    }
}