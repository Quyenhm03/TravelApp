package com.example.travel_app.Data.Repository;

import com.example.travel_app.Data.Model.Bookings;

import java.util.ArrayList;
import java.util.List;

public class BookingRepository {

    private static BookingRepository instance;
    private List<Bookings> bookingsList = new ArrayList<>();

    public static BookingRepository getInstance() {
        if (instance == null) {
            instance = new BookingRepository();
        }
        return instance;
    }

    private BookingRepository() {

    }

    public Bookings getBookingById(int bookingId) {
        for (Bookings booking : bookingsList) {
            if (booking.getId() == bookingId) {
                return booking;
            }
        }
        return null;
    }

    public void saveBooking(Bookings booking) {
        for (int i = 0; i < bookingsList.size(); i++) {
            if (bookingsList.get(i).getId() == booking.getId()) {
                bookingsList.set(i, booking);
                return;
            }
        }
        bookingsList.add(booking);
    }
}

