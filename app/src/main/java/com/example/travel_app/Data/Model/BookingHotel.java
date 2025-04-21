package com.example.travel_app.Data.Model;

import java.io.Serializable;

public class BookingHotel implements Serializable {
    private String id;
    private String hotelId;
    private String hotelName;
    private String checkInDate;
    private String checkOutDate;
    private String roomType;
    private long totalAmount;
    private String userId;
    private String status;
    private Payment payment;
    private long checkInTimestamp;

    public BookingHotel() {}

    public BookingHotel(String id, String hotelId, String hotelName, String checkInDate, String checkOutDate,
                        String roomType, long totalAmount, String userId, String status, Payment payment,
                        long checkInTimestamp) {
        this.id = id;
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomType = roomType;
        this.totalAmount = totalAmount;
        this.userId = userId;
        this.status = status;
        this.payment = payment;
        this.checkInTimestamp = checkInTimestamp;
    }

    // Getter và Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getHotelId() { return hotelId; }
    public void setHotelId(String hotelId) { this.hotelId = hotelId; }
    public String getHotelName() { return hotelName; }
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }
    public String getCheckInDate() { return checkInDate; }
    public void setCheckInDate(String checkInDate) { this.checkInDate = checkInDate; }
    public String getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(String checkOutDate) { this.checkOutDate = checkOutDate; }
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public long getTotalAmount() { return totalAmount; }
    public void setTotalAmount(long totalAmount) { this.totalAmount = totalAmount; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }
    public long getCheckInTimestamp() { return checkInTimestamp; }
    public void setCheckInTimestamp(long checkInTimestamp) { this.checkInTimestamp = checkInTimestamp; }
}