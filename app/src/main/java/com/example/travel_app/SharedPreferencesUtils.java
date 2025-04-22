package com.example.travel_app;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {
    private static final String PREF_NAME = "hotel_booking_data";

    public static void saveBookingData(Context context, String hotelName, String roomType, long totalAmount, String checkInDate, String checkOutDate) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("hotel_name", hotelName);
        editor.putString("room_type", roomType);
        editor.putLong("total_amount", totalAmount);
        editor.putString("check_in_date", checkInDate);
        editor.putString("check_out_date", checkOutDate);

        editor.apply();
    }

    public static void setBookingId(Context context, String hotelId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("hotel_id", hotelId);
        editor.apply();
    }

    public static SharedPreferences getBookingData(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void clearBookingData(Context context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().clear().apply();
    }
}
