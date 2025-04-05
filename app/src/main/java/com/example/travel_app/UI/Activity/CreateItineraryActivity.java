package com.example.travel_app.UI.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Adapter.ItineraryAdapter;
import com.example.travel_app.Data.Model.Itinerary;
import com.example.travel_app.R;
import com.example.travel_app.ViewModel.CreateItineraryViewModel;

import java.util.ArrayList;

public class CreateItineraryActivity extends AppCompatActivity {
    private CreateItineraryViewModel viewModel;
    private ItineraryAdapter adapter;
    private RecyclerView rcvItinerary;
    private AppCompatButton btnMyItinerary, btnSharedItinerary;
    private CardView cvCreateItinerary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_itinerary);

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(CreateItineraryViewModel.class);

        // Khởi tạo các view
        rcvItinerary = findViewById(R.id.rcv_itinerary);
        btnMyItinerary = findViewById(R.id.btn_my_itinerary);
        btnSharedItinerary = findViewById(R.id.btn_shared_itinerary);
        cvCreateItinerary = findViewById(R.id.cv_create_itinerary);

        // Thiết lập RecyclerView
        rcvItinerary.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItineraryAdapter(new ArrayList<>(), new ItineraryAdapter.OnItineraryClickListener() {
            @Override
            public void onItineraryClick(Itinerary itinerary) {
                Intent intent = new Intent(CreateItineraryActivity.this, ItineraryDetailActivity.class);
                intent.putExtra("itinerary", itinerary);
                startActivity(intent);
            }

            @Override
            public void onShareClick(Itinerary itinerary) {
                viewModel.shareItinerary(itinerary);
            }
        });
        rcvItinerary.setAdapter(adapter);

        // Xử lý sự kiện click vào các nút
        btnMyItinerary.setOnClickListener(v -> {
            btnMyItinerary.setBackgroundResource(R.drawable.button_selected);
            btnMyItinerary.setTextColor(getResources().getColor(R.color.white));
            btnSharedItinerary.setBackgroundResource(android.R.color.white);
            btnSharedItinerary.setTextColor(Color.parseColor("#B8B8B8"));
            viewModel.loadMyItineraries();
        });

        btnSharedItinerary.setOnClickListener(v -> {
            btnSharedItinerary.setBackgroundResource(R.drawable.button_selected);
            btnSharedItinerary.setTextColor(getResources().getColor(R.color.white));
            btnMyItinerary.setBackgroundResource(android.R.color.white);
            btnMyItinerary.setTextColor(Color.parseColor("#B8B8B8"));
            viewModel.loadSharedItineraries();
        });

        // Quan sát dữ liệu từ ViewModel
        viewModel.getItineraryListLiveData().observe(this, itineraries -> {
            if (itineraries != null) {
//                boolean isSharedList = btnSharedItinerary.getCurrentTextColor() == getResources().getColor(R.color.white);
                adapter.updateList(itineraries);
            }
        });

        // Mặc định hiển thị "Lộ trình của bạn"
        btnMyItinerary.performClick();

        cvCreateItinerary.setOnClickListener(v -> {
            Intent intent = new Intent(CreateItineraryActivity.this, SetUpInfoActivity.class);
            startActivity(intent);
        });
    }
}
