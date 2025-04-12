package com.example.travel_app.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.travel_app.Service.ReminderService;

//Đặt lịch thông báo khi thiết bị khởi động
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, ReminderService.class);
            context.startService(serviceIntent);
        }
    }
}
