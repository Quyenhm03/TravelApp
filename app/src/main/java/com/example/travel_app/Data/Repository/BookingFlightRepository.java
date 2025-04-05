package com.example.travel_app.Data.Repository;

import androidx.annotation.NonNull;

import com.example.travel_app.Data.Model.BookingFlight;
import com.example.travel_app.Data.Model.Flight;
import com.example.travel_app.Data.Model.Payment;
import com.example.travel_app.Data.Model.Seat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingFlightRepository {
    private DatabaseReference bookingFlightsRef;
    private DatabaseReference flightsRef;

    public BookingFlightRepository() {
        bookingFlightsRef = FirebaseDatabase.getInstance().getReference("bookingFlight");
        flightsRef = FirebaseDatabase.getInstance().getReference("flight");
    }

    // Lưu BookingFlight (bao gồm Payment bên trong)
    public void saveBooking(BookingFlight booking, Callback callback) {
        // Kiểm tra xem Payment đã được thiết lập trong BookingFlight chưa
        if (booking.getPayment() == null) {
            callback.onFailure(new Exception("Payment information is missing in BookingFlight"));
            return;
        }

        // Cập nhật trạng thái ghế trước khi lưu BookingFlight
        updateSeats(booking, new Callback() {
            @Override
            public void onSuccess() {
                // Sau khi cập nhật ghế thành công, lưu BookingFlight
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

    // Cập nhật trạng thái ghế cho các chuyến bay
    private void updateSeats(BookingFlight booking, Callback callback) {
        // Cập nhật ghế chuyến đi
        updateFlightSeats(booking.getDepartureFlight().getId(), booking.getSelectedSeatsDeparture(), new Callback() {
            @Override
            public void onSuccess() {
                // Nếu có chuyến về, cập nhật ghế chuyến về
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

    // Cập nhật trạng thái ghế cho một chuyến bay
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
                // Cập nhật tất cả ghế cùng lúc
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

    // Callback để thông báo kết quả
    public interface Callback {
        void onSuccess();
        void onFailure(Exception e);
    }
}