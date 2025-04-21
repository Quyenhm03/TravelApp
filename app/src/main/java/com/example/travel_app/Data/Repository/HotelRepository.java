package com.example.travel_app.Data.Repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.travel_app.Data.Model.Hotel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HotelRepository {
    private static final String TAG = "HotelRepository";
    private final DatabaseReference hotelsRef;

    public interface HotelCallback {
        void onSuccess(List<Hotel> hotels);
        void onFailure(String error);
    }

    public interface SingleHotelCallback {
        void onSuccess(Hotel hotel);
        void onFailure(String error);
    }

    public HotelRepository() {

        hotelsRef = FirebaseDatabase.getInstance().getReference().child("Hotels");
    }

    // Hàm lấy danh sách khách sạn từ Firebase
    public void fetchHotels(HotelCallback callback) {
        hotelsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Hotel> hotelList = new ArrayList<>();

                for (DataSnapshot hotelSnapshot : dataSnapshot.getChildren()) {
                    try {
                        Hotel hotel = hotelSnapshot.getValue(Hotel.class);
                        if (hotel != null) {
                            hotelList.add(hotel);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing hotel data: " + e.getMessage());
                    }
                }

                // Gọi callback với danh sách khách sạn
                callback.onSuccess(hotelList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error fetching hotels: " + databaseError.getMessage());
                callback.onFailure(databaseError.getMessage());
            }
        });
    }

    // Hàm lấy khách sạn theo ID
    public void fetchHotelById(String hotelId, SingleHotelCallback callback) {
        hotelsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean found = false;
                for (DataSnapshot hotelSnapshot : dataSnapshot.getChildren()) {
                    try {
                        Hotel hotel = hotelSnapshot.getValue(Hotel.class);
                        if (hotel != null && hotel.getId().equals(hotelId)) {
                            callback.onSuccess(hotel);
                            found = true;
                            break;
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing hotel data: " + e.getMessage());
                    }
                }
                if (!found) {
                    callback.onFailure("Hotel not found with ID: " + hotelId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error fetching hotel: " + databaseError.getMessage());
                callback.onFailure(databaseError.getMessage());
            }
        });
    }

}
