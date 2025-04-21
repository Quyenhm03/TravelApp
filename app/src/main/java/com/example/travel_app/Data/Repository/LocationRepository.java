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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationRepository {
    private static final String TAG = "LocationRepository";

    private final DatabaseReference locationRef;
    private final DatabaseReference userFavoritesRef;
    private final MutableLiveData<Location> locationLiveData;
    private final MutableLiveData<List<Location>> allLocationsLiveData;
    private final MutableLiveData<List<Location>> favoriteLocationsLiveData;

    public LocationRepository() {
        locationRef = FirebaseDatabase.getInstance().getReference("Location");
        userFavoritesRef = FirebaseDatabase.getInstance().getReference("UserFavorites");
        locationLiveData = new MutableLiveData<>();
        allLocationsLiveData = new MutableLiveData<>();
        favoriteLocationsLiveData = new MutableLiveData<>();
    }

    public LiveData<Location> getLocation(int locationId) {
        locationRef.child(String.valueOf(locationId)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()) {
                    Log.w(TAG, "Không tìm thấy địa điểm cho locationId: " + locationId);
                    locationLiveData.setValue(null);
                    return;
                }

                try {
                    Location location = dataSnapshot.getValue(Location.class);
                    if (location != null) {
                        locationLiveData.setValue(location);
                    } else {
                        locationLiveData.setValue(null);
                    }
                } catch (Exception e) {
                    locationLiveData.setValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                locationLiveData.setValue(null);
            }
        });

        return locationLiveData;
    }

    public LiveData<List<Location>> getAllLocations() {
        locationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Location> locations = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        Location location = snapshot.getValue(Location.class);
                        if (location != null) {
                            locations.add(location);
                        }
                    } catch (Exception e) {
                    }
                }

                if (locations.isEmpty()) {
                }

                allLocationsLiveData.setValue(locations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                allLocationsLiveData.setValue(new ArrayList<>());
            }
        });

        return allLocationsLiveData;
    }

//    public void updateLocationFavorite(int locationId, boolean isFavorite) {
//        locationRef.child(String.valueOf(locationId)).child("is_favourite").setValue(isFavorite)
//                .addOnSuccessListener(aVoid -> Log.d(TAG, "Cập nhật is_favourite thành công cho locationId: " + locationId))
//                .addOnFailureListener(e -> Log.e(TAG, "Lỗi khi cập nhật is_favourite cho locationId: " + locationId, e));
//
//    }

    public void updateLocationFavorite(String userId, String locationId, boolean isFavorite) {
        DatabaseReference favRef = FirebaseDatabase.getInstance()
                .getReference("FavoriteLocations").child(userId).child(locationId);

        if (isFavorite) {
            favRef.setValue(true)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Đã yêu thích locationId: " + locationId))
                    .addOnFailureListener(e -> Log.e(TAG, "Lỗi khi yêu thích locationId: " + locationId, e));
        } else {
            favRef.removeValue()
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Đã bỏ yêu thích locationId: " + locationId))
                    .addOnFailureListener(e -> Log.e(TAG, "Lỗi khi bỏ yêu thích locationId: " + locationId, e));
        }
    }



//    public LiveData<List<Location>> getFavoriteLocationsByUserId(String userId) {
//        userFavoritesRef.child(userId).child("locationIds").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                List<Integer> favoriteLocationIds = new ArrayList<>();
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    if (snapshot.getValue(Boolean.class)) {
//                        favoriteLocationIds.add(Integer.parseInt(snapshot.getKey()));
//                    }
//                }
//
//                if (favoriteLocationIds.isEmpty()) {
//                    Log.w(TAG, "Không có địa điểm yêu thích nào cho userId: " + userId);
//                    favoriteLocationsLiveData.setValue(new ArrayList<>());
//                    return;
//                }
//
//                // Lấy thông tin chi tiết của các địa điểm yêu thích
//                List<Location> favoriteLocations = new ArrayList<>();
//                for (int locationId : favoriteLocationIds) {
//                    locationRef.child(String.valueOf(locationId)).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot locationSnapshot) {
//                            try {
//                                Location location = locationSnapshot.getValue(Location.class);
//                                if (location != null) {
//                                    favoriteLocations.add(location);
//                                    Log.d(TAG, "Địa điểm yêu thích: tenDiaDiem=" + location.getTenDiaDiem() + ", locationId=" + location.getLocationId());
//                                }
//
//                                // Cập nhật LiveData khi đã lấy hết tất cả địa điểm
//                                if (favoriteLocations.size() == favoriteLocationIds.size()) {
//                                    favoriteLocationsLiveData.setValue(favoriteLocations);
//                                }
//                            } catch (Exception e) {
//                                Log.e(TAG, "Lỗi ánh xạ địa điểm yêu thích: locationId=" + locationId + " - " + e.getMessage());
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                            Log.e(TAG, "Lỗi khi lấy địa điểm yêu thích: locationId=" + locationId + " - " + databaseError.getMessage());
//                            // Cập nhật LiveData nếu có lỗi
//                            if (favoriteLocations.size() == favoriteLocationIds.size()) {
//                                favoriteLocationsLiveData.setValue(favoriteLocations);
//                            }
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e(TAG, "Lỗi khi lấy danh sách yêu thích cho userId: " + userId + " - " + databaseError.getMessage());
//                favoriteLocationsLiveData.setValue(new ArrayList<>());
//            }
//        });
//
//        return favoriteLocationsLiveData;
//    }

    public MutableLiveData<List<Location>> getFavoriteLocationsByUserId(String userId) {
        MutableLiveData<List<Location>> favoriteLocationsLiveData = new MutableLiveData<>();
        List<Location> favoriteLocations = new ArrayList<>();

        if (userId == null || userId.isEmpty()) {
            Log.e(TAG, "userId không hợp lệ");
            favoriteLocationsLiveData.setValue(favoriteLocations);
            return favoriteLocationsLiveData;
        }

        DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("FavoriteLocations").child(userId);
        favRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot favSnapshot) {
                if (!favSnapshot.exists()) {
                    Log.d(TAG, "Không có địa điểm yêu thích cho userId: " + userId);
                    favoriteLocationsLiveData.setValue(favoriteLocations);
                    return;
                }

                locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot allLocationsSnapshot) {
                        for (DataSnapshot locationSnapshot : allLocationsSnapshot.getChildren()) {
                            String locationId = locationSnapshot.getKey();
                            if (favSnapshot.hasChild(locationId)) {
                                Location location = locationSnapshot.getValue(Location.class);
                                if (location != null) {
                                    favoriteLocations.add(location);
                                }
                            }
                        }
                        favoriteLocationsLiveData.setValue(favoriteLocations);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        favoriteLocationsLiveData.setValue(new ArrayList<>());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                favoriteLocationsLiveData.setValue(new ArrayList<>());
            }
        });

        return favoriteLocationsLiveData;
    }


}