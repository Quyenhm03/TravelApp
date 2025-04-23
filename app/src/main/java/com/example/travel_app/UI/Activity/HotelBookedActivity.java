package com.example.travel_app.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Adapter.HotelBookingAdapter;
import com.example.travel_app.Data.Model.BookingHotel;
import com.example.travel_app.R;
import com.example.travel_app.UI.Login.LoginActivity;
import com.example.travel_app.ViewModel.Itinerary.BookingHotelViewModel;
import com.example.travel_app.ViewModel.UserCurrentViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class HotelBookedActivity extends AppCompatActivity {

    private RecyclerView rvHotelBookings;
    private HotelBookingAdapter adapter;
    private BookingHotelViewModel bookingHotelViewModel;
    private UserCurrentViewModel userCurrentViewModel;
    private Button btnBookNew, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_booked);


        rvHotelBookings = findViewById(R.id.rvHotelBookings);
        btnBookNew = findViewById(R.id.btnBookNew);
        btnBack = findViewById(R.id.btnBack);


        rvHotelBookings.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HotelBookingAdapter(this, new ArrayList<>());
        rvHotelBookings.setAdapter(adapter);
        rvHotelBookings.setHasFixedSize(true);

        // Initialize ViewModels
        bookingHotelViewModel = new ViewModelProvider(this).get(BookingHotelViewModel.class);
        userCurrentViewModel = new ViewModelProvider(this).get(UserCurrentViewModel.class);

        // Get userId and fetch bookings
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Bạn cần đăng nhập để xem lịch sử đặt phòng", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        userCurrentViewModel.user.observe(this, user -> {
            if (user != null) {
                Log.d("HotelBookedActivity", "UserId hiện tại: " + user.getUserId());
                bookingHotelViewModel.loadBookingsForUser("10");

                // Observe danh sách Booking sau khi tải xong
                bookingHotelViewModel.getBookingsByUserIdLiveData().observe(this, bookings -> {
                    if (bookings != null) {
                        adapter.updateBookings(bookings);
                    }
                });

            } else {
                Toast.makeText(this, "Bạn cần đăng nhập để xem lịch sử đặt phòng", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });



        // Observe errors
        bookingHotelViewModel.getErrorMessage().observe(this, error -> {
            Toast.makeText(this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
        });

        // Button listeners
        btnBookNew.setOnClickListener(v -> {
            // Navigate to activity for booking a new hotel (e.g., HotelSearchActivity)
            Intent intent = new Intent(this, FindHotelActivity.class);
            startActivity(intent);
        });

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}