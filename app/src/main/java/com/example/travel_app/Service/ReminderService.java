package com.example.travel_app.Service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.travel_app.Data.Model.BookingCoach;
import com.example.travel_app.Data.Model.BookingFlight;
import com.example.travel_app.Receiver.ReminderBroadcastReceiver;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReminderService extends Service {
    private FirebaseDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 1. Xử lý vé xe khách (BookingCoach)
        DatabaseReference bookingCoachRef = database.getReference("bookingCoach");
        bookingCoachRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot bookingSnapshot : snapshot.getChildren()) {
                    BookingCoach booking = bookingSnapshot.getValue(BookingCoach.class);
                    if (booking != null && booking.getStatus().equals("Đã thanh toán")) {
                        long currentTime = System.currentTimeMillis();

                        // Chuyến đi
                        long departureTime = booking.getDepartureTimestamp();
                        if (departureTime > currentTime) { // Chưa khởi hành
                            long departureReminderTime = departureTime - 30 * 60 * 1000; // 30 phút trước
                            if (departureReminderTime > currentTime) { // Chưa đến thời gian thông báo
                                scheduleReminder(
                                        ReminderService.this,
                                        "Chuyến xe đi từ " + booking.getDepartureCity() + " đến " +
                                                booking.getArrivalCity() + " sẽ khởi hành lúc " +
                                                booking.getDepartureCoach().getDepartureTime(),
                                        departureReminderTime
                                );
                            }
                        }

                        // Chuyến về (nếu có)
                        if (booking.getReturnCoach() != null) {
                            long returnTime = booking.getReturnTimestamp();
                            if (returnTime > currentTime) { // Chưa khởi hành
                                long returnReminderTime = returnTime - 30 * 60 * 1000;
                                if (returnReminderTime > currentTime) { // Chưa đến thời gian thông báo
                                    scheduleReminder(
                                            ReminderService.this,
                                            "Chuyến xe về từ " + booking.getArrivalCity() + " đến " +
                                                    booking.getDepartureCity() + " sẽ khởi hành lúc " +
                                                    booking.getReturnCoach().getDepartureTime(),
                                            returnReminderTime
                                    );
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ReminderService", "Failed to load coach bookings: " + error.getMessage());
            }
        });

        // 2. Xử lý vé máy bay (BookingFlight)
        DatabaseReference bookingFlightRef = database.getReference("bookingFlight");
        bookingFlightRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot bookingSnapshot : snapshot.getChildren()) {
                    BookingFlight booking = bookingSnapshot.getValue(BookingFlight.class);
                    if (booking != null && booking.getStatus().equals("Đã thanh toán")) {
                        long currentTime = System.currentTimeMillis();

                        // Chuyến đi
                        long departureTime = booking.getDepartureTimestamp();
                        if (departureTime > currentTime) {
                            long departureReminderTime = departureTime - 30 * 60 * 1000;
                            if (departureReminderTime > currentTime) { // Chưa đến thời gian thông báo
                                scheduleReminder(
                                        ReminderService.this,
                                        "Chuyến bay đi từ " + booking.getDepartureCity() + " đến " +
                                                booking.getArrivalCity() + " sẽ khởi hành lúc " +
                                                booking.getDepartureFlight().getDepartureTime(),
                                        departureReminderTime
                                );
                            }
                        }

                        // Chuyến về (nếu có)
                        if (booking.getReturnFlight() != null) {
                            long returnTime = booking.getReturnTimestamp();
                            if (returnTime > currentTime) { // Chưa khởi hành
                                long returnReminderTime = returnTime - 30 * 60 * 1000;
                                if (returnReminderTime > currentTime) { // Chưa đến thời gian thông báo
                                    scheduleReminder(
                                            ReminderService.this,
                                            "Chuyến bay về từ " + booking.getArrivalCity() + " đến " +
                                                    booking.getDepartureCity() + " sẽ khởi hành lúc " +
                                                    booking.getReturnFlight().getDepartureTime(),
                                            returnReminderTime
                                    );
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ReminderService", "Failed to load flight bookings: " + error.getMessage());
            }
        });

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("ScheduleExactAlarm")
    private void scheduleReminder(Context context, String message, long triggerTime) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Log.e("ReminderDebug", "Cannot schedule exact alarm due to missing SCHEDULE_EXACT_ALARM permission");
                return;
            }
        }

        Intent intent = new Intent(context, ReminderBroadcastReceiver.class);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                message.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        }
    }
}