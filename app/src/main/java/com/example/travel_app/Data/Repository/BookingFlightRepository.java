package com.example.travel_app.Data.Repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travel_app.Data.Model.BookingFlight;
import com.example.travel_app.Data.Model.Flight;
import com.example.travel_app.Data.Model.Payment;
import com.example.travel_app.Data.Model.Seat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingFlightRepository {
    private DatabaseReference bookingFlightsRef;
    private DatabaseReference flightsRef;

    public BookingFlightRepository() {
        bookingFlightsRef = FirebaseDatabase.getInstance().getReference("BookingFlight");
        flightsRef = FirebaseDatabase.getInstance().getReference("Flight");
    }

    public LiveData<List<BookingFlight>> getBookingFlightHistory(String userId) {
        MutableLiveData<List<BookingFlight>> bookingLiveData = new MutableLiveData<>();
        bookingFlightsRef.orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<BookingFlight> bookingList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BookingFlight booking = dataSnapshot.getValue(BookingFlight.class);
                    if (booking != null) {
                        bookingList.add(booking);
                    }
                }
                bookingLiveData.setValue(bookingList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                bookingLiveData.setValue(null);
            }
        });
        return bookingLiveData;
    }

    public void saveBooking(BookingFlight booking, Callback callback) {
        if (booking.getPayment() == null) {
            callback.onFailure(new Exception("Payment information is missing in BookingFlight"));
            return;
        }

        updateSeats(booking, new Callback() {
            @Override
            public void onSuccess() {
                bookingFlightsRef.child(booking.getId()).setValue(booking, (error, ref) -> {
                    if (error != null) {
                        callback.onFailure(error.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    private void updateSeats(BookingFlight booking, Callback callback) {
        updateFlightSeats(booking.getDepartureFlight().getId(), booking.getSelectedSeatsDeparture(), new Callback() {
            @Override
            public void onSuccess() {
                if (booking.getReturnFlight() != null) {
                    updateFlightSeats(booking.getReturnFlight().getId(), booking.getSelectedSeatsReturn(), callback);
                } else {
                    callback.onSuccess();
                }
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    private void updateFlightSeats(int flightId, List<String> seatNumbers, Callback callback) {
        flightsRef.child(String.valueOf(flightId)).child("seats").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Object> updates = new HashMap<>();
                for (DataSnapshot seatSnapshot : snapshot.getChildren()) {
                    Seat seat = seatSnapshot.getValue(Seat.class);
                    if (seat != null && seatNumbers.contains(seat.getSeatNumber())) {
                        updates.put(seatSnapshot.getKey() + "/isBooked", true);
                    }
                }
                flightsRef.child(String.valueOf(flightId)).child("seats").updateChildren(updates, (error, ref) -> {
                    if (error != null) {
                        callback.onFailure(error.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure(error.toException());
            }
        });
    }

    public interface Callback {
        void onSuccess();
        void onFailure(Exception e);
    }
}