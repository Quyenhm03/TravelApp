package com.example.travel_app.Data.Repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.travel_app.Data.Model.Room;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RoomRepository {
    private static final String TAG = "RoomRepository";
    private final DatabaseReference roomsRef;

    public interface RoomListCallback {
        void onSuccess(List<Room> rooms);
        void onFailure(String error);
    }

    public RoomRepository() {
        roomsRef = FirebaseDatabase.getInstance().getReference().child("Rooms");
    }

    // Hàm lấy danh sách phòng theo hotelId
    public void fetchRoomsByHotelId(String hotelId, RoomListCallback callback) {
        roomsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Room> roomList = new ArrayList<>();

                for (DataSnapshot roomSnapshot : dataSnapshot.getChildren()) {
                    try {
                        // Lấy dữ liệu phòng từ snapshot
                        String hotelsId = roomSnapshot.child("Hotelsid").getValue(String.class);

                        // Lọc các phòng theo hotelId
                        if (hotelsId != null && hotelsId.equals(hotelId)) {
                            Room room = new Room();
                            room.setRoomType(roomSnapshot.child("room_type").getValue(String.class));
                            room.setPrice(roomSnapshot.child("price").getValue(Integer.class));
                            room.setAvailability(roomSnapshot.child("availability").getValue(Integer.class));
                            roomList.add(room);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing room data: " + e.getMessage());
                    }
                }

                // Gọi callback với danh sách phòng
                callback.onSuccess(roomList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error fetching rooms: " + databaseError.getMessage());
                callback.onFailure(databaseError.getMessage());
            }
        });
    }
}