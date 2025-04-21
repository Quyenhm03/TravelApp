package com.example.travel_app.Data.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.travel_app.Data.Model.BookingHotel;
import com.example.travel_app.Data.Model.Payment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookingHotelRepository {
    private static final String TAG = "BookingHotelRepository";
    private DatabaseReference bookingHotelRef;
    private DatabaseReference paymentRef;
    private DatabaseReference roomsRef;

    public BookingHotelRepository() {
        bookingHotelRef = FirebaseDatabase.getInstance().getReference("BookingHotel");
        paymentRef = FirebaseDatabase.getInstance().getReference("Payment");
        roomsRef = FirebaseDatabase.getInstance().getReference("Rooms");
    }

    private MutableLiveData<Boolean> saveSuccess = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<List<BookingHotel>> bookingsByUserId = new MutableLiveData<>();

    public MutableLiveData<Boolean> getSaveSuccess() {
        return saveSuccess;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    // Hàm lưu BookingHotel và Payment vào Firebase
    public void saveBooking(BookingHotel bookingHotel, Payment payment, String hotelId, String roomType) {
        // 1. Lưu Payment trước
        paymentRef.child(payment.getId()).setValue(payment, (error, ref) -> {
            if (error != null) {
                Log.e(TAG, "Lỗi khi lưu Payment: " + error.getMessage());
                errorMessage.setValue(error.getMessage());
                saveSuccess.setValue(false);
                return;
            }

            // 2. Lưu BookingHotel
            bookingHotelRef.child(bookingHotel.getId()).setValue(bookingHotel, (error2, ref2) -> {
                if (error2 != null) {
                    Log.e(TAG, "Lỗi khi lưu BookingHotel: " + error2.getMessage());
                    errorMessage.setValue(error2.getMessage());
                    saveSuccess.setValue(false);
                    return;
                }

                // 3. Cập nhật availability trong Rooms
                updateRoomAvailability(hotelId, roomType);
            });
        });
    }

    // Hàm cập nhật availability trong node Rooms
    private void updateRoomAvailability(String hotelId, String roomType) {
        roomsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot roomSnapshot : dataSnapshot.getChildren()) {
                    String currentHotelId = roomSnapshot.child("Hotelsid").getValue(String.class);
                    String currentRoomType = roomSnapshot.child("room_type").getValue(String.class);
                    Integer availability = roomSnapshot.child("availability").getValue(Integer.class);

                    if (currentHotelId != null && currentRoomType != null && availability != null &&
                            currentHotelId.equals(hotelId) && currentRoomType.equals(roomType)) {
                        // Giảm availability đi 1
                        int newAvailability = availability - 1;
                        if (newAvailability >= 0) {
                            roomSnapshot.getRef().child("availability").setValue(newAvailability, (error, ref) -> {
                                if (error != null) {
                                    Log.e(TAG, "Lỗi khi cập nhật availability: " + error.getMessage());
                                    errorMessage.setValue(error.getMessage());
                                    saveSuccess.setValue(false);
                                } else {
                                    Log.d(TAG, "Cập nhật availability thành công: " + newAvailability);
                                    saveSuccess.setValue(true);
                                }
                            });
                        } else {
                            Log.w(TAG, "Không còn phòng trống để đặt!");
                            errorMessage.setValue("Không còn phòng trống để đặt!");
                            saveSuccess.setValue(false);
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Lỗi khi truy vấn Rooms: " + databaseError.getMessage());
                errorMessage.setValue(databaseError.getMessage());
                saveSuccess.setValue(false);
            }
        });
    }

    // Hàm tìm kiếm danh sách đặt phòng theo userId

//    public MutableLiveData<List<BookingHotel>> getBookingsByUserId(String userId) {
//        Log.d(TAG, "Bắt đầu truy vấn BookingHotel với userId: " + userId);
//        bookingHotelRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.d(TAG, "Số lượng booking trong database: " + dataSnapshot.getChildrenCount());
//                List<BookingHotel> bookings = new ArrayList<>();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    BookingHotel booking = snapshot.getValue(BookingHotel.class);
//                    if (booking != null && booking.getUserId().equals(userId)) {
//                        bookings.add(booking);
//                    }
//                }
//                bookingsByUserId.setValue(bookings);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e(TAG, "Lỗi khi truy vấn BookingHotel: " + databaseError.getMessage());
//                errorMessage.setValue(databaseError.getMessage());
//            }
//        });
//        return bookingsByUserId;
//    }

    public void fetchBookingsByUserId(String userId) {
        bookingHotelRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<BookingHotel> bookings = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BookingHotel booking = snapshot.getValue(BookingHotel.class);
                    if (booking != null && booking.getPayment() != null && booking.getUserId().equals(userId)) {
                        bookings.add(booking);
                    }
                }
                bookingsByUserId.setValue(bookings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                errorMessage.setValue(databaseError.getMessage());
            }
        });
    }

    public MutableLiveData<List<BookingHotel>> getBookingsByUserIdLiveData() {
        return bookingsByUserId;
    }


}
