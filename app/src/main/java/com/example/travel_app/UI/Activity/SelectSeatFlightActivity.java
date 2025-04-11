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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Adapter.SeatAdapter;
import com.example.travel_app.Data.Model.BookingFlight;
import com.example.travel_app.Data.Model.SearchFlightInfo;
import com.example.travel_app.R;
import com.example.travel_app.ViewModel.SeatViewModel;

import java.util.ArrayList;
import java.util.List;

public class SelectSeatActivity extends AppCompatActivity {
    private SearchFlightInfo searchFlightInfo;
    private BookingFlight bookingFlight;
    private List<String> selectedSeatsDeparture = new ArrayList<>();
    private List<String> selectedSeatsReturn = new ArrayList<>();
    private SeatViewModel seatViewModelDeparture, seatViewModelReturn;
    private SeatAdapter seatDepartureAdapter, seatReturnAdapter;
    private RecyclerView rcvSeatDeparture, rcvSeatReturn;
    private LinearLayout lnSeatReturn;
    private Button btnConfirm;

    private int seatCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_seat_flight);

        searchFlightInfo = (SearchFlightInfo) getIntent().getSerializableExtra("searchFlightInfo");
        bookingFlight = (BookingFlight) getIntent().getSerializableExtra("bookingFlight");

        seatCount = searchFlightInfo.getCustomerCount();

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
        seatViewModelDeparture = new ViewModelProvider(this).get(SeatViewModel.class);
        seatViewModelDeparture.loadFlightSeats(bookingFlight.getDepartureFlight().getId());
        seatViewModelDeparture.getFlightLiveData().observe(this, flight -> {
            if (flight != null && flight.getSeats() != null) {
                seatDepartureAdapter.updateSeats(flight.getSeats());
                updateRecyclerViewHeight(rcvSeatDeparture);
            }
        });

        // Kiểm tra xem có chuyến về không
        if (bookingFlight.getReturnFlight() != null) {
            lnSeatReturn.setVisibility(View.VISIBLE);

            rcvSeatReturn = findViewById(R.id.rcv_seat_return);
            rcvSeatReturn.setLayoutManager(new GridLayoutManager(this, 4));
            seatReturnAdapter = new SeatAdapter(new ArrayList<>(), seatCount, (seatNumber, isSelecting) -> {
                if (isSelecting) selectedSeatsReturn.add(seatNumber);
                else selectedSeatsReturn.remove(seatNumber);
            });
            rcvSeatReturn.setAdapter(seatReturnAdapter);

            // Load ghế chuyến về từ ViewModel
            seatViewModelReturn = new ViewModelProvider(this).get(SeatViewModel.class);
            seatViewModelReturn.loadFlightSeats(bookingFlight.getReturnFlight().getId());
            seatViewModelReturn.getFlightLiveData().observe(this, flight -> {
                if (flight != null && flight.getSeats() != null) {
                    seatReturnAdapter.updateSeats(flight.getSeats());
                    updateRecyclerViewHeight(rcvSeatReturn);
                }
            });
        }

        // Xác nhận đặt vé
        btnConfirm.setOnClickListener(v -> confirmBooking());
    }

    private void confirmBooking() {
        if (selectedSeatsDeparture.size() < seatCount) {
            Toast.makeText(this, "Vui lòng chọn đủ ghế cho chuyến đi!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (bookingFlight.getReturnFlight() != null && selectedSeatsReturn.size() < seatCount) {
            Toast.makeText(this, "Vui lòng chọn đủ ghế cho chuyến về!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(SelectSeatActivity.this, PaymentFlightActivity.class);
        intent.putExtra("searchFlightInfo", searchFlightInfo);
        bookingFlight.setSelectedSeatsDeparture(selectedSeatsDeparture);
        if(bookingFlight.getReturnFlight() != null) {
            bookingFlight.setSelectedSeatsReturn(selectedSeatsReturn);
        }
        intent.putExtra("bookingFlight", bookingFlight);
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
