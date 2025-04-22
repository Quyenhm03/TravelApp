package com.example.travel_app;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Firebase sign out mỗi khi app khởi động
        FirebaseAuth.getInstance().signOut();
    }
}

