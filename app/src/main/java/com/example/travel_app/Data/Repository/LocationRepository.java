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
    private final DatabaseReference favoriteRef;

    private final MutableLiveData<Location> locationLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Location>> allLocationsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Location>> favoriteLocationsLiveData = new MutableLiveData<>();
    private Location location = new Location();

    public LocationRepository() {
        locationRef = FirebaseDatabase.getInstance().getReference("Location");
        favoriteRef = FirebaseDatabase.getInstance().getReference("FavoriteLocations");
    }

    // ------------------- Lấy 1 địa điểm theo ID --------------------
    public LiveData<Location> getLocation(int locationId) {
        locationRef.child(String.valueOf(locationId - 1)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                locationLiveData.setValue(snapshot.getValue(Location.class));

            }
            @Override public void onCancelled(@NonNull DatabaseError error) {
                locationLiveData.setValue(null);
            }
        });
        return locationLiveData;
    }

    public void getLocationById(int locationId, LocationCallback callback) {
        locationRef.child(String.valueOf(locationId)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                Location location = snapshot.getValue(Location.class);
                callback.onLocationLoaded(location);
            }

            @Override public void onCancelled(@NonNull DatabaseError error) {
                callback.onLocationLoaded(null);
            }
        });
    }


    // ------------------- Lấy toàn bộ địa điểm --------------------
    public LiveData<List<Location>> getAllLocations() {
        locationRef.addValueEventListener(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Location> result = new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Location location = snap.getValue(Location.class);
                    if (location != null) result.add(location);
                }
                allLocationsLiveData.setValue(result);
            }

            @Override public void onCancelled(@NonNull DatabaseError error) {
                allLocationsLiveData.setValue(new ArrayList<>());
            }
        });
        return allLocationsLiveData;
    }

    // ------------------- Cập nhật yêu thích --------------------
    public void updateLocationFavorite(String userId, String locationId, boolean isFavorite) {
        DatabaseReference favRef = favoriteRef.child(userId).child(locationId);
        if (isFavorite) {
            favRef.setValue(true)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Yêu thích: " + locationId))
                    .addOnFailureListener(e -> Log.e(TAG, "Lỗi yêu thích: " + locationId, e));
        } else {
            favRef.removeValue()
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Bỏ yêu thích: " + locationId))
                    .addOnFailureListener(e -> Log.e(TAG, "Lỗi bỏ yêu thích: " + locationId, e));
        }
    }

     // ------------------- Lấy danh sách yêu thích theo user --------------------
//    public MutableLiveData<List<Location>> getFavoriteLocationsByUserId(String userId) {
//        List<Location> favoriteLocations = new ArrayList<>();
//        MutableLiveData<List<Location>> liveData = new MutableLiveData<>();
//
//        if (userId == null || userId.isEmpty()) {
//            liveData.setValue(favoriteLocations);
//            return liveData;
//        }
//
//        favoriteRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override public void onDataChange(@NonNull DataSnapshot favSnapshot) {
//                if (!favSnapshot.exists()) {
//                    liveData.setValue(favoriteLocations);
//                    return;
//                }
//
//                locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override public void onDataChange(@NonNull DataSnapshot allSnapshot) {
//                        for (DataSnapshot snap : allSnapshot.getChildren()) {
//                            String locId = snap.getKey();
//                            if (favSnapshot.hasChild(locId)) {
//                                Location loc = snap.getValue(Location.class);
//                                if (loc != null) favoriteLocations.add(loc);
//                            }
//                        }
//                        liveData.setValue(favoriteLocations);
//                    }
//
//                    @Override public void onCancelled(@NonNull DatabaseError error) {
//                        liveData.setValue(new ArrayList<>());
//                    }
//                });
//            }
//
//            @Override public void onCancelled(@NonNull DatabaseError error) {
//                liveData.setValue(new ArrayList<>());
//            }
//        });
//
//        return liveData;
//    }

    public MutableLiveData<List<Location>> getFavoriteLocationsByUserId(String userId) {
        final String TAG = "FavoriteLocations";
        MutableLiveData<List<Location>> liveData = new MutableLiveData<>();
        List<Location> favoriteLocations = new ArrayList<>();

        if (userId == null || userId.isEmpty()) {
            Log.w(TAG, "userId is null or empty, returning empty list");
            liveData.setValue(favoriteLocations);
            return liveData;
        }

        DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("FavoriteLocations").child(userId);
        DatabaseReference locRef = FirebaseDatabase.getInstance().getReference("Location");

        favRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot favSnapshot) {
                List<String> favIds = new ArrayList<>();

                for (DataSnapshot snap : favSnapshot.getChildren()) {
                    Boolean isFav = snap.getValue(Boolean.class);
                    if (Boolean.TRUE.equals(isFav)) {
                        favIds.add(snap.getKey());  // locationId
                    }
                }

                if (favIds.isEmpty()) {
                    liveData.setValue(favoriteLocations);
                    return;
                }

                locRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot locSnapshot) {
                        for (DataSnapshot snap : locSnapshot.getChildren()) {
                            if (favIds.contains(snap.getKey())) {
                                Location loc = snap.getValue(Location.class);
                                if (loc != null) favoriteLocations.add(loc);
                            }
                        }
                        liveData.setValue(favoriteLocations);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Location query cancelled: " + error.getMessage());
                        liveData.setValue(new ArrayList<>());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "FavoriteLocations query cancelled: " + error.getMessage());
                liveData.setValue(new ArrayList<>());
            }
        });

        return liveData;
    }

    // ------------------- Kiểm tra một địa điểm có đang được yêu thích hay không --------------------
    public LiveData<Boolean> isLocationFavorite(String userId, String locationId) {
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();

        if (userId == null || userId.isEmpty() || locationId == null || locationId.isEmpty()) {
            liveData.setValue(false);
            return liveData;
        }

        favoriteRef.child(userId).child(locationId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        liveData.setValue(snapshot.exists());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "isLocationFavorite cancelled: " + error.getMessage());
                        liveData.setValue(false);
                    }
                });

        return liveData;
    }





    // ------------------- Tìm kiếm (callback cho Itinerary) --------------------
    public void searchLocations(String query, SearchCallback callback) {
        locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Location> result = new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Location loc = snap.getValue(Location.class);
                    if (loc != null && (
                            loc.getTenDiaDiem().toLowerCase().contains(query.toLowerCase()) ||
                                    loc.getViTri().toLowerCase().contains(query.toLowerCase()))) {
                        result.add(loc);
                    }
                }
                callback.onSuccess(result);
            }

            @Override public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure(error.toException());
            }
        });
    }

    public void getAllLocations(SearchCallback callback) {
        locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Location> result = new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Location loc = snap.getValue(Location.class);
                    if (loc != null) result.add(loc);
                }
                callback.onSuccess(result);
            }

            @Override public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure(error.toException());
            }
        });
    }

    // Interface callback cho tìm kiếm
    public interface SearchCallback {
        void onSuccess(List<Location> locations);
        void onFailure(Exception e);
    }

    public interface LocationCallback {
        void onLocationLoaded(Location location);
    }

}
