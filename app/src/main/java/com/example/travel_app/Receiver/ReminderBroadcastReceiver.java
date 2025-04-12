package com.example.travel_app.Receiver;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.travel_app.R;

public class ReminderBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Log.d("ReminderDebug", "ReminderBroadcastReceiver triggered");

            String message = intent.getStringExtra("message");
            if (message == null) {
                Log.e("ReminderDebug", "Message is null in reminder broadcast");
                return;
            }

            Log.d("ReminderDebug", "Displaying notification with message: " + message);

            // Tạo kênh thông báo
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        "travel_reminder_channel",
                        "Travel Reminder",
                        NotificationManager.IMPORTANCE_HIGH
                );
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

            // Tạo thông báo
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "travel_reminder_channel")
                    .setSmallIcon(R.drawable.notification)
                    .setContentTitle("Nhắc nhở chuyến đi")
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);

            // Gửi thông báo
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Log.e("ReminderDebug", "Missing POST_NOTIFICATIONS permission");
                return;
            }

            int notificationId = (int) System.currentTimeMillis();
            notificationManager.notify(notificationId, builder.build());
            Log.d("ReminderDebug", "Notification displayed with ID: " + notificationId);
        } catch (Exception e) {
            Log.e("ReminderDebug", "Error in onReceive: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
