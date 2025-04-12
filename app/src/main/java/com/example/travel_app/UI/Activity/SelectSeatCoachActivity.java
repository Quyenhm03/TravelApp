package com.example.travel_app.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Adapter.SeatAdapter;
import com.example.travel_app.Data.Model.BookingCoach;
import com.example.travel_app.Data.Model.SearchCoachInfo;
import com.example.travel_app.R;
import com.example.travel_app.ViewModel.SeatCoachViewModel;
import com.example.travel_app.ViewModel.SeatFlightViewModel;

import java.util.ArrayList;
import java.util.List;

public class SelectSeatCoachActivity extends AppCompatActivity {
    private SearchCoachInfo searchCoachInfo;
    private BookingCoach bookingCoach;
    private List<String> selectedSeatsDeparture = new ArrayList<>();
    private List<String> selectedSeatsReturn = new ArrayList<>();
    private SeatCoachViewModel seatViewModelDeparture, seatViewModelReturn;
    private SeatAdapter seatDepartureAdapter, seatReturnAdapter;
    private RecyclerView rcvSeatDeparture, rcvSeatReturn;
    private LinearLayout lnSeatReturn;
    private Button btnConfirm;

    private int seatCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_seat_coach);

        // Lấy dữ liệu từ Intent
        searchCoachInfo = (SearchCoachInfo) getIntent().getSerializableExtra("searchCoachInfo");
        bookingCoach = (BookingCoach) getIntent().getSerializableExtra("bookingCoach");

        seatCount = searchCoachInfo.getSeatCount();

        // Ánh xạ các view
        rcvSeatDeparture = findViewById(R.id.rcv_seat_departure);
        btnConfirm = findViewById(R.id.btn_confirm_seat);
        lnSeatReturn = findViewById(R.id.ln_seat_return);

        // Setup RecyclerView cho chuyến đi
        rcvSeatDeparture.setLayoutManager(new GridLayoutManager(this, 4));
        seatDepartureAdapter = new SeatAdapter(new ArrayList<>(), seatCount, (seatNumber, isSelecting) -> {
            if (isSelecting) selectedSeatsDeparture.add(seatNumber);
            else selectedSeatsDeparture.remove(seatNumber);
        });
        rcvSeatDeparture.setAdapter(seatDepartureAdapter);

        // Load ghế chuyến đi từ ViewModel
        seatViewModelDeparture = new ViewModelProvider(this).get(SeatCoachViewModel.class);
        seatViewModelDeparture.loadCoachSeats(bookingCoach.getDepartureCoach().getId());
        seatViewModelDeparture.getCoachLiveData().observe(this, coach -> {
            if (coach != null && coach.getSeats() != null) {
                seatDepartureAdapter.updateSeats(coach.getSeats());
                updateRecyclerViewHeight(rcvSeatDeparture);
            }
        });

        // Kiểm tra xem có chuyến về không
        if (bookingCoach.getReturnCoach() != null) {
            lnSeatReturn.setVisibility(View.VISIBLE);

            rcvSeatReturn = findViewById(R.id.rcv_seat_return);
            rcvSeatReturn.setLayoutManager(new GridLayoutManager(this, 4));
            seatReturnAdapter = new SeatAdapter(new ArrayList<>(), seatCount, (seatNumber, isSelecting) -> {
                if (isSelecting) selectedSeatsReturn.add(seatNumber);
                else selectedSeatsReturn.remove(seatNumber);
            });
            rcvSeatReturn.setAdapter(seatReturnAdapter);

            // Load ghế chuyến về từ ViewModel
            seatViewModelReturn = new ViewModelProvider(this).get(SeatCoachViewModel.class);
            seatViewModelReturn.loadCoachSeats(bookingCoach.getReturnCoach().getId());
            seatViewModelReturn.getCoachLiveData().observe(this, coach -> {
                if (coach != null && coach.getSeats() != null) {
                    seatReturnAdapter.updateSeats(coach.getSeats());
                    updateRecyclerViewHeight(rcvSeatReturn);
                }
            });
        }

        // Xử lý nút "Xác nhận"
        btnConfirm.setOnClickListener(v -> confirmBooking());

        // Xử lý nút "Back"
        AppCompatButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
    }

    private void confirmBooking() {
        if (selectedSeatsDeparture.size() < seatCount) {
            Toast.makeText(this, "Vui lòng chọn đủ ghế cho chuyến đi!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (bookingCoach.getReturnCoach() != null && selectedSeatsReturn.size() < seatCount) {
            Toast.makeText(this, "Vui lòng chọn đủ ghế cho chuyến về!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(SelectSeatCoachActivity.this, PaymentCoachActivity.class);
        intent.putExtra("searchCoachInfo", searchCoachInfo);
        bookingCoach.setSelectedSeatsDeparture(selectedSeatsDeparture);
        if (bookingCoach.getReturnCoach() != null) {
            bookingCoach.setSelectedSeatsReturn(selectedSeatsReturn);
        }
        intent.putExtra("bookingCoach", bookingCoach);
        startActivity(intent);
    }

    private void updateRecyclerViewHeight(RecyclerView recyclerView) {
        recyclerView.post(() -> {
            ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
            params.height = recyclerView.computeVerticalScrollRange();
            recyclerView.setLayoutParams(params);
        });
    }
}