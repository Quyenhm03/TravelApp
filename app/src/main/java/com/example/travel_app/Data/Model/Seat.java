package com.example.travel_app.Data.Model;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;

@IgnoreExtraProperties
public class Seat implements Serializable {
    private String seatNumber;

    @PropertyName("isBooked")
    private boolean isBooked;

    public Seat() {}  // Firebase cần constructor rỗng

    public Seat(String seatNumber, boolean isBooked) {
        this.seatNumber = seatNumber;
        this.isBooked = isBooked;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    @PropertyName("isBooked")
    public boolean getIsBooked() {
        return isBooked;
    }

    @PropertyName("isBooked")
    public void setIsBooked(boolean isBooked) {
        this.isBooked = isBooked;
    }
}
