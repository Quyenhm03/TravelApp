package com.example.travel_app.Data.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travel_app.Data.Model.Location;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LocationRepository {
    private static final String TAG = "LocationRepository";

    private final DatabaseReference locationRef;
    private final MutableLiveData<Location> locationLiveData;
    private final MutableLiveData<List<Location>> allLocationsLiveData;

    public LocationRepository() {
        locationRef = FirebaseDatabase.getInstance().getReference("Location");
        locationLiveData = new MutableLiveData<>();
        allLocationsLiveData = new MutableLiveData<>();
    }

    public LiveData<Location> getLocation(int locationId) {
        locationRef.child(String.valueOf(locationId)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "Dữ liệu JSON thô cho locationId " + locationId + ": " + dataSnapshot.getValue());
                if (!dataSnapshot.exists()) {
                    Log.w(TAG, "Không tìm thấy địa điểm cho locationId: " + locationId);
                    locationLiveData.setValue(null);
                    return;
                }

                try {
                    Location location = dataSnapshot.getValue(Location.class);
                    if (location != null) {
                        locationLiveData.setValue(location);
                        Log.d(TAG, "Địa điểm được ánh xạ: tenDiaDiem=" + location.getTenDiaDiem() + ", moTa=" + location.getMoTa());
                    } else {
                        Log.e(TAG, "Không thể ánh xạ dữ liệu cho locationId: " + locationId);
                        locationLiveData.setValue(null);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Lỗi ánh xạ dữ liệu cho locationId: " + locationId + " - " + e.getMessage());
                    locationLiveData.setValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Lỗi khi lấy địa điểm cho locationId: " + locationId + " - " + databaseError.getMessage());
                locationLiveData.setValue(null);
            }
        });

        return locationLiveData;
    }

    public LiveData<List<Location>> getAllLocations() {
        locationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "Dữ liệu JSON thô cho tất cả địa điểm: " + dataSnapshot.getValue());
                List<Location> locations = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        Location location = snapshot.getValue(Location.class);
                        if (location != null) {
                            locations.add(location);
                            Log.d(TAG, "Địa điểm: tenDiaDiem=" + location.getTenDiaDiem() + ", locationId=" + location.getLocationId());
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Lỗi ánh xạ một địa điểm: " + e.getMessage());
                    }
                }

                if (locations.isEmpty()) {
                    Log.w(TAG, "Không tìm thấy địa điểm nào");
                }

                allLocationsLiveData.setValue(locations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Lỗi khi lấy tất cả địa điểm: " + databaseError.getMessage());
                allLocationsLiveData.setValue(new ArrayList<>());
            }
        });

        return allLocationsLiveData;
    }

    public void updateLocationFavorite(int locationId, boolean isFavorite) {
        locationRef.child(String.valueOf(locationId)).child("is_favourite").setValue(isFavorite)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Cập nhật is_favourite thành công cho locationId: " + locationId))
                .addOnFailureListener(e -> Log.e(TAG, "Lỗi khi cập nhật is_favourite cho locationId: " + locationId, e));
    }

}
