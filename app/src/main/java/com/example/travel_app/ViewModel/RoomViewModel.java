package com.example.travel_app.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app.Data.Model.Room;
import com.example.travel_app.Data.Repository.RoomRepository;

import java.util.List;

public class RoomViewModel extends ViewModel {
    private final MutableLiveData<List<Room>> rooms = new MutableLiveData<>();
    private final RoomRepository roomRepository;

    public RoomViewModel() {
        roomRepository = new RoomRepository();
    }

    public LiveData<List<Room>> getRooms() {
        return rooms;
    }

    public void fetchRoomsByHotelId(String hotelId) {
        roomRepository.fetchRoomsByHotelId(hotelId, new RoomRepository.RoomListCallback() {
            @Override
            public void onSuccess(List<Room> roomList) {
                rooms.setValue(roomList);
            }

            @Override
            public void onFailure(String error) {
                rooms.setValue(null);
            }
        });
    }
}
