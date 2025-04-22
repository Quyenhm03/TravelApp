package com.example.travel_app.UI.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.travel_app.Data.Model.Hotel;
import com.example.travel_app.Data.Model.Room;
import com.example.travel_app.R;
import com.example.travel_app.ViewModel.HotelDetailViewModel;
import com.example.travel_app.ViewModel.RoomViewModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class DetailHotelActivity extends AppCompatActivity {

    private ImageView imageHotel, btnBack;
    private TextView textHotelName, textHotelRating, textHotelLocation, textHotelWebsite, textHotelPhone;
    private TextView textCheckInTime, textCheckOutTime;
    private Button buttonConfirmBooking;
    private HotelDetailViewModel hotelDetailViewModel;


    @SuppressLint({"SetTextI18n", "MissingInflatedId", "ObsoleteSdkInt"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_hotel);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.orange));
        }
        imageHotel = findViewById(R.id.imageHotel);
        textHotelName = findViewById(R.id.textHotelName);
        textHotelRating = findViewById(R.id.textHotelRating);
        textHotelLocation = findViewById(R.id.textHotelLocation);
        textHotelWebsite = findViewById(R.id.textHotelWebsite);
        textHotelPhone = findViewById(R.id.textHotelPhone);
        textCheckInTime = findViewById(R.id.textCheckInTime);
        textCheckOutTime = findViewById(R.id.textCheckOutTime);
        buttonConfirmBooking = findViewById(R.id.buttonConfirmBooking);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });

        hotelDetailViewModel = new ViewModelProvider(this).get(HotelDetailViewModel.class);

        Intent intent = getIntent();
        String hotelId = intent.getStringExtra("hotel_id");

        hotelDetailViewModel.fetchHotelById(hotelId);

        hotelDetailViewModel.getSelectedHotel().observe(this, hotel -> {
            if (hotel != null) {
                displayHotelDetails(hotel);
            } else {
                Toast.makeText(this, "Hotel not found!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        buttonConfirmBooking.setOnClickListener(v -> {

            Intent intent1 = new Intent(DetailHotelActivity.this, DetailBookingRoomActivity.class);
            intent1.putExtra("hotel_id", hotelId);
            startActivity(intent1);
        });

    }

    @SuppressLint("SetTextI18n")
    private void displayHotelDetails(Hotel hotel) {
        textHotelName.setText(hotel.getName());
        textHotelRating.setText("Rating: " + hotel.getRating());
        textHotelLocation.setText("Location: " + hotel.getLocation());
        textHotelWebsite.setText("Website: " + hotel.getWebsite());

        if(hotel.getHotLine() == null){
            hotel.setHotLine("0123456789");
        }
        else {
            textHotelPhone.setText("Phone: " + hotel.getHotLine());
        }

        textCheckInTime.setText("Check-in: " + hotel.getCheckInTime());
        textCheckOutTime.setText("Check-out: " + hotel.getCheckOutTime());


        if (hotel.getHotelUrl() != null && !hotel.getHotelUrl().isEmpty()) {
            Picasso.get()
                    .load(hotel.getHotelUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.img_hotel_default)
                    .into(imageHotel, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
        } else {
            imageHotel.setImageResource(R.drawable.img_hotel_default);
        }
    }


}