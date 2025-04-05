package com.example.travel_app.Data.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travel_app.Data.Model.Day;
import com.example.travel_app.Data.Model.Itinerary;
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
    private DatabaseReference itemsRef;

    public ItineraryRepository() {
        itinerariesRef = FirebaseDatabase.getInstance().getReference("Itinerary");
        itemsRef = FirebaseDatabase.getInstance().getReference("Item_tmp");
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
                for (DataSnapshot itinerarySnapshot : snapshot.getChildren()) {
                    Itinerary itinerary = itinerarySnapshot.getValue(Itinerary.class);
                    if (itinerary != null) {
                        if (itinerary.getDays() == null) {
                            itinerary.setDays(new ArrayList<>());
                        }
                        for (Day day : itinerary.getDays()) {
                            if (day.getItems() == null) {
                                day.setItems(new ArrayList<>());
                            }
                        }
                        itineraryList.add(itinerary);
                    }
                }
                itinerariesLiveData.setValue(itineraryList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ItineraryRepository", "Get itineraries cancelled: " + error.getMessage());
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
                for (DataSnapshot itinerarySnapshot : snapshot.getChildren()) {
                    Itinerary itinerary = itinerarySnapshot.getValue(Itinerary.class);
                    if (itinerary != null) {
                        if (itinerary.getDays() == null) {
                            itinerary.setDays(new ArrayList<>());
                        }
                        for (Day day : itinerary.getDays()) {
                            if (day.getItems() == null) {
                                day.setItems(new ArrayList<>());
                            }
                        }
                        itineraryList.add(itinerary);
                    }
                }
                sharedItinerariesLiveData.setValue(itineraryList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ItineraryRepository", "Get shared itineraries cancelled: " + error.getMessage());
                sharedItinerariesLiveData.setValue(new ArrayList<>());
            }
        });
        return sharedItinerariesLiveData;
    }

    public void saveItinerary(Itinerary itinerary, Callback callback) {
        if (itinerary == null) {
            callback.onFailure(new IllegalArgumentException("Itinerary cannot be null"));
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
                Log.e("ItineraryRepository", "Failed to update isShare: " + error.getMessage());
                callback.onFailure(error.toException());
            } else {
                Log.d("ItineraryRepository", "Updated isShare for ID: " + itineraryId);
                callback.onSuccess();
            }
        });
    }

    public interface Callback {
        void onSuccess();
        void onFailure(Exception e);
    }
}