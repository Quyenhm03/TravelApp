package com.example.travel_app.Data.Repository.Itinerary;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travel_app.Data.Model.Itinerary.Day;
import com.example.travel_app.Data.Model.Itinerary.Itinerary;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ItineraryRepository {
    private DatabaseReference itinerariesRef;
    private DatabaseReference locationsRef;

    public ItineraryRepository() {
        itinerariesRef = FirebaseDatabase.getInstance().getReference("Itinerary");
        locationsRef = FirebaseDatabase.getInstance().getReference("Location");
    }

    private String generateItineraryId() {
        return "itinerary_" + System.currentTimeMillis();
    }

    public LiveData<List<Itinerary>> getItinerariesByUser(String userId) {
        MutableLiveData<List<Itinerary>> itinerariesLiveData = new MutableLiveData<>();
        Query query = itinerariesRef.orderByChild("userId").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Itinerary> itineraryList = new ArrayList<>();
                Log.d("ItineraryRepository", "Snapshot cho userId " + userId + ": tồn tại=" + snapshot.exists() + ", số lượng=" + snapshot.getChildrenCount());
                for (DataSnapshot itinerarySnapshot : snapshot.getChildren()) {
                    Itinerary itinerary = itinerarySnapshot.getValue(Itinerary.class);
                    if (itinerary != null) {
                        itinerary.setId(itinerarySnapshot.getKey());
                        if (itinerary.getDays() == null) {
                            itinerary.setDays(new ArrayList<>());
                        }
                        for (Day day : itinerary.getDays()) {
                            if (day.getLocations() == null) {
                                day.setLocations(new ArrayList<>());
                            }
                        }
                        itineraryList.add(itinerary);
                        Log.d("ItineraryRepository", "Hành trình: " + itinerary.getTitle());
                    }
                }
                itinerariesLiveData.setValue(itineraryList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ItineraryRepository", "Lấy lộ trình bị hủy: " + error.getMessage());
                itinerariesLiveData.setValue(new ArrayList<>());
            }
        });
        return itinerariesLiveData;
    }

    public LiveData<List<Itinerary>> getSharedItineraries() {
        MutableLiveData<List<Itinerary>> sharedItinerariesLiveData = new MutableLiveData<>();
        Query query = itinerariesRef.orderByChild("isShare").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Itinerary> itineraryList = new ArrayList<>();
                Log.d("ItineraryRepository", "Snapshot cho lộ trình chia sẻ: tồn tại=" + snapshot.exists() + ", số lượng=" + snapshot.getChildrenCount());
                for (DataSnapshot itinerarySnapshot : snapshot.getChildren()) {
                    Itinerary itinerary = itinerarySnapshot.getValue(Itinerary.class);
                    if (itinerary != null) {
                        itinerary.setId(itinerarySnapshot.getKey());
                        if (itinerary.getDays() == null) {
                            itinerary.setDays(new ArrayList<>());
                        }
                        for (Day day : itinerary.getDays()) {
                            if (day.getLocations() == null) {
                                day.setLocations(new ArrayList<>());
                            }
                        }
                        itineraryList.add(itinerary);
                        Log.d("ItineraryRepository", "Hành trình chia sẻ: " + itinerary.getTitle());
                    }
                }
                sharedItinerariesLiveData.setValue(itineraryList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ItineraryRepository", "Lấy lộ trình chia sẻ bị hủy: " + error.getMessage());
                sharedItinerariesLiveData.setValue(new ArrayList<>());
            }
        });
        return sharedItinerariesLiveData;
    }

    public void saveItinerary(Itinerary itinerary, Callback callback) {
        if (itinerary == null) {
            callback.onFailure(new IllegalArgumentException("Hành trình không được null"));
            return;
        }

        String id = itinerary.getId();
        if (id == null) {
            id = generateItineraryId();
            itinerary.setId(id);
        }

        itinerariesRef.child(id).setValue(itinerary, (error, ref) -> {
            if (error != null) {
                callback.onFailure(error.toException());
            } else {
                callback.onSuccess();
            }
        });
    }

    public void updateIsShare(String itineraryId, boolean isShare, Callback callback) {
        itinerariesRef.child(itineraryId).child("isShare").setValue(isShare, (error, ref) -> {
            if (error != null) {
                Log.e("ItineraryRepository", "Cập nhật isShare thất bại: " + error.getMessage());
                callback.onFailure(error.toException());
            } else {
                Log.d("ItineraryRepository", "Cập nhật isShare cho ID: " + itineraryId);
                callback.onSuccess();
            }
        });
    }

    public LiveData<List<Itinerary>> searchItinerariesByTitle(String query) {
        MutableLiveData<List<Itinerary>> searchResultsLiveData = new MutableLiveData<>();
        itinerariesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Itinerary> itineraryList = new ArrayList<>();
                String lowerQuery = query.toLowerCase();
                Log.d("ItineraryRepository", "Tìm kiếm với truy vấn: " + query);
                for (DataSnapshot itinerarySnapshot : snapshot.getChildren()) {
                    Itinerary itinerary = itinerarySnapshot.getValue(Itinerary.class);
                    if (itinerary != null && itinerary.getTitle() != null && itinerary.getTitle().toLowerCase().contains(lowerQuery)) {
                        itinerary.setId(itinerarySnapshot.getKey());
                        if (itinerary.getDays() == null) {
                            itinerary.setDays(new ArrayList<>());
                        }
                        for (Day day : itinerary.getDays()) {
                            if (day.getLocations() == null) {
                                day.setLocations(new ArrayList<>());
                            }
                        }
                        itineraryList.add(itinerary);
                        Log.d("ItineraryRepository", "Hành trình tìm thấy: " + itinerary.getTitle());
                    }
                }
                searchResultsLiveData.setValue(itineraryList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ItineraryRepository", "Tìm kiếm lộ trình bị hủy: " + error.getMessage());
                searchResultsLiveData.setValue(new ArrayList<>());
            }
        });
        return searchResultsLiveData;
    }

    public interface Callback {
        void onSuccess();
        void onFailure(Exception e);
    }
}