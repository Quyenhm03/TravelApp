package com.example.travel_app.Data.Model;

import com.google.firebase.database.PropertyName;

import java.util.List;

public class Room {
    @PropertyName("hotel_id")
    private String hotelId;
    @PropertyName("availability")
    private int availability;
    @PropertyName("price")
    private long price;
    @PropertyName("room_type")
    private String roomType;


    public Room() {}

    public Room(String hotelId, int availability, long price, String roomType) {
        this.hotelId = hotelId;
        this.availability = availability;
        this.price = price;
        this.roomType = roomType;
    }

    @PropertyName("hotel_id")
    public String getHotelId() {
        return hotelId;
    }

    @PropertyName("hotel_id")
    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    @PropertyName("availability")
    public int getAvailability() {
        return availability;
    }

    @PropertyName("availability")
    public void setAvailability(int availability) {
        this.availability = availability;
    }

    @PropertyName("price")
    public long getPrice() {
        return price;
    }

    @PropertyName("price")
    public void setPrice(long price) {
        this.price = price;
    }


    @PropertyName("room_type")
    public String getRoomType() {
        return roomType;
    }

    @PropertyName("room_type")
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
}