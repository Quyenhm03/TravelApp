package com.example.travel_app.Data.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travel_app.Data.Model.BookingCoach;
import com.example.travel_app.Data.Model.BookingFlight;
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

public class BookingCoachRepository {
    private DatabaseReference bookingCoachesRef;
    private DatabaseReference coachesRef;

    public BookingCoachRepository() {
        bookingCoachesRef = FirebaseDatabase.getInstance().getReference("BookingCoach");
        coachesRef = FirebaseDatabase.getInstance().getReference("Coach");
    }

    // Lấy danh sách lịch sử đặt vé theo userId
    public LiveData<List<BookingCoach>> getBookingHistory(String userId) {
        MutableLiveData<List<BookingCoach>> bookingLiveData = new MutableLiveData<>();
        bookingCoachesRef.orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<BookingCoach> bookingList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BookingCoach booking = dataSnapshot.getValue(BookingCoach.class);
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

    // Lưu BookingCoach
    public void saveBooking(BookingCoach booking, Callback callback) {
        if (booking.getPayment() == null) {
            callback.onFailure(new Exception("Payment information is missing in BookingCoach"));
            return;
        }

        updateSeats(booking, new Callback() {
            @Override
            public void onSuccess() {
                bookingCoachesRef.child(booking.getId()).setValue(booking, (error, ref) -> {
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

    private void updateSeats(BookingCoach booking, Callback callback) {
        updateCoachSeats(booking.getDepartureCoach().getId(), booking.getSelectedSeatsDeparture(), new Callback() {
            @Override
            public void onSuccess() {
                if (booking.getReturnCoach() != null) {
                    updateCoachSeats(booking.getReturnCoach().getId(), booking.getSelectedSeatsReturn(), callback);
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

    private void updateCoachSeats(int coachId, List<String> seatNumbers, Callback callback) {
        coachesRef.child(String.valueOf(coachId)).child("seats").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Object> updates = new HashMap<>();
                for (DataSnapshot seatSnapshot : snapshot.getChildren()) {
                    Seat seat = seatSnapshot.getValue(Seat.class);
                    if (seat != null && seatNumbers.contains(seat.getSeatNumber())) {
                        updates.put(seatSnapshot.getKey() + "/isBooked", true);
                    }
                }
                coachesRef.child(String.valueOf(coachId)).child("seats").updateChildren(updates, (error, ref) -> {
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

    public LiveData<List<BookingCoach>> getBookingCoachHistory(String userId) {
        MutableLiveData<List<BookingCoach>> bookingLiveData = new MutableLiveData<>();
        bookingCoachesRef.orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<BookingCoach> bookingList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BookingCoach booking = dataSnapshot.getValue(BookingCoach.class);
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

    public interface Callback {
        void onSuccess();
        void onFailure(Exception e);
    }
}
