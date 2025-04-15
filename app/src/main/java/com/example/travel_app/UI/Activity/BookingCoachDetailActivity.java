package com.example.travel_app.UI.Activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.travel_app.Data.Model.BookingCoach;
import com.example.travel_app.Data.Model.Coach;
import com.example.travel_app.Data.Model.Passenger;
import com.example.travel_app.R;

import java.util.List;

public class BookingCoachDetailActivity extends BaseActivity {
    private BookingCoach bookingCoach;

    private TextView txtDepartureCoachName, txtDepartureDate, txtDepartureTime, txtDepartureCity, txtDepartureStationName,
            txtArrivalTime, txtArrivalCity, txtArrivalStationName, txtDepartureCoachPlate, txtDepartureSeat,
            txtReturnCoachName, txtReturnDate, txtReturnTime, txtReturnDepartureCity, txtReturnDepartureStationName,
            txtReturnArrivalTime, txtReturnArrivalCity, txtReturnArrivalStationName, txtReturnCoachPlate, txtReturnSeat,
            txtAmount, txtCustomerName, txtBookingDate, txtStatus;
    private LinearLayout lnPaymentReturn, passengerContainer;
    private AppCompatButton btnBack, btnBack2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_booking_coach);

        bookingCoach = (BookingCoach) getIntent().getSerializableExtra("bookingCoach");
        if (bookingCoach == null) {
            Toast.makeText(this, "Không tìm thấy thông tin đặt vé xe!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }


        txtDepartureCoachName = findViewById(R.id.txt_departure_coach_name);
        txtDepartureDate = findViewById(R.id.txt_departure_date_payment);
        txtDepartureTime = findViewById(R.id.txt_departure_time_payment);
        txtDepartureCity = findViewById(R.id.txt_departure_city_payment);
        txtDepartureStationName = findViewById(R.id.txt_departure_station_name);
        txtArrivalTime = findViewById(R.id.txt_arrival_time_payment);
        txtArrivalCity = findViewById(R.id.txt_arrival_city_payment);
        txtArrivalStationName = findViewById(R.id.txt_arrival_station_name);
        txtDepartureCoachPlate = findViewById(R.id.txt_departure_coach_payment);
        txtDepartureSeat = findViewById(R.id.txt_departure_seat_payment);
        txtReturnCoachName = findViewById(R.id.txt_return_coach_name);
        txtReturnDate = findViewById(R.id.txt_return_date_payment);
        txtReturnTime = findViewById(R.id.txt_departure_time_return_payment);
        txtReturnDepartureCity = findViewById(R.id.txt_departure_city_return_payment);
        txtReturnDepartureStationName = findViewById(R.id.txt_departure_station_name_return);
        txtReturnArrivalTime = findViewById(R.id.txt_arrival_time_return_payment);
        txtReturnArrivalCity = findViewById(R.id.txt_arrival_city_return_payment);
        txtReturnArrivalStationName = findViewById(R.id.txt_arrival_station_name_return);
        txtReturnCoachPlate = findViewById(R.id.txt_return_coach_payment);
        txtReturnSeat = findViewById(R.id.txt_return_seat_payment);
        txtAmount = findViewById(R.id.txt_amount);
        txtCustomerName = findViewById(R.id.txt_customer_name);
        txtBookingDate = findViewById(R.id.txt_booking_date);
        txtStatus = findViewById(R.id.txt_status);
        lnPaymentReturn = findViewById(R.id.ln_payment_return);
        passengerContainer = findViewById(R.id.passenger_container);
        btnBack = findViewById(R.id.btn_back);
        btnBack2 = findViewById(R.id.btn_back2);

        displayDepartureInfo();
        displayReturnInfo();
        displayGeneralInfo();
        displayPassengerList(bookingCoach.getPassengerList());

        btnBack.setOnClickListener(v -> finish());
        btnBack2.setOnClickListener(v -> finish());
    }

    private void displayDepartureInfo() {
        Coach departureCoach = bookingCoach.getDepartureCoach();
        if (departureCoach != null) {
            txtDepartureCoachName.setText(departureCoach.getCompanyName());
            txtDepartureDate.setText(departureCoach.getDepartureDate());
            txtDepartureTime.setText(departureCoach.getDepartureTime());
            txtDepartureCity.setText(bookingCoach.getDepartureCity());
            txtDepartureStationName.setText(departureCoach.getDepartureStationName());
            txtArrivalTime.setText(departureCoach.getArrivalTime());
            txtArrivalCity.setText(bookingCoach.getArrivalCity());
            txtArrivalStationName.setText(departureCoach.getArrivalStationName());
            txtDepartureCoachPlate.setText(departureCoach.getCoachNumber());
            if (bookingCoach.getSelectedSeatsDeparture() != null && !bookingCoach.getSelectedSeatsDeparture().isEmpty()) {
                txtDepartureSeat.setText(String.join(", ", bookingCoach.getSelectedSeatsDeparture()));
            }
        }
    }

    private void displayReturnInfo() {
        Coach returnCoach = bookingCoach.getReturnCoach();
        if (returnCoach != null) {
            lnPaymentReturn.setVisibility(View.VISIBLE);
            txtReturnCoachName.setText(returnCoach.getCompanyName());
            txtReturnDate.setText(returnCoach.getDepartureDate());
            txtReturnTime.setText(returnCoach.getDepartureTime());
            txtReturnDepartureCity.setText(bookingCoach.getArrivalCity());
            txtReturnDepartureStationName.setText(returnCoach.getDepartureStationName());
            txtReturnArrivalTime.setText(returnCoach.getArrivalTime());
            txtReturnArrivalCity.setText(bookingCoach.getDepartureCity());
            txtReturnArrivalStationName.setText(returnCoach.getArrivalStationName());
            txtReturnCoachPlate.setText(returnCoach.getCoachNumber());
            if (bookingCoach.getSelectedSeatsReturn() != null && !bookingCoach.getSelectedSeatsReturn().isEmpty()) {
                txtReturnSeat.setText(String.join(", ", bookingCoach.getSelectedSeatsReturn()));
            }
        }
    }

    private void displayGeneralInfo() {
        txtAmount.setText(String.format("%,.0f VNĐ", bookingCoach.getTotalAmount()));
        txtStatus.setText(bookingCoach.getStatus());
        if (bookingCoach.getPayment() != null) {
            txtBookingDate.setText(bookingCoach.getPayment().getTransactionDate());
        }
        if (bookingCoach.getPassengerList() != null && !bookingCoach.getPassengerList().isEmpty()) {
            txtCustomerName.setText(bookingCoach.getPassengerList().get(0).getFullName());
        }
    }

    private void displayPassengerList(List<Passenger> passengerList) {
        if (passengerList == null || passengerList.isEmpty()) {
            TextView noPassengerText = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 20, 0, 20); // Thêm khoảng cách trên/dưới
            noPassengerText.setLayoutParams(params);
            noPassengerText.setText("Không có thông tin hành khách");
            noPassengerText.setTextSize(16);
            noPassengerText.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
            noPassengerText.setGravity(Gravity.CENTER); // Căn giữa khi không có hành khách
            passengerContainer.addView(noPassengerText);
            return;
        }

        for (int i = 0; i < passengerList.size(); i++) {
            Passenger passenger = passengerList.get(i);

            // Tạo CardView
            CardView cardView = new CardView(this);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            cardParams.setMargins(0, 16, 0, 16); // Khoảng cách giữa các CardView
            cardView.setLayoutParams(cardParams);
            cardView.setCardElevation(4);
            cardView.setRadius(12); // Bo góc mềm hơn
            cardView.setUseCompatPadding(true);
            cardView.setContentPadding(16, 16, 16, 16);

            // Tạo LinearLayout chính trong CardView
            LinearLayout cardLayout = new LinearLayout(this);
            cardLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            cardLayout.setOrientation(LinearLayout.VERTICAL);

            // Header (Tên hành khách + icon mở rộng)
            LinearLayout headerLayout = new LinearLayout(this);
            headerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            headerLayout.setOrientation(LinearLayout.HORIZONTAL);
            headerLayout.setGravity(Gravity.CENTER_VERTICAL);

            TextView txtPassengerName = new TextView(this);
            LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1);
            txtPassengerName.setLayoutParams(nameParams);
            txtPassengerName.setText("Hành khách " + (i + 1) + ": " + (passenger.getFullName() != null ? passenger.getFullName() : "N/A"));
            txtPassengerName.setTextSize(16);
            txtPassengerName.setTextColor(Color.parseColor("#676767"));

            ImageView imgExpand = new ImageView(this);
            LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(40, 40);
            iconParams.setMargins(8, 0, 0, 0); // Khoảng cách giữa tên và icon
            imgExpand.setLayoutParams(iconParams);
            imgExpand.setImageResource(R.drawable.ic_expand_more);

            headerLayout.addView(txtPassengerName);
            headerLayout.addView(imgExpand);

            // Layout chứa thông tin chi tiết (ban đầu ẩn)
            LinearLayout detailsLayout = new LinearLayout(this);
            LinearLayout.LayoutParams detailsParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            detailsParams.setMargins(0, 12, 0, 0);
            detailsLayout.setLayoutParams(detailsParams);
            detailsLayout.setOrientation(LinearLayout.VERTICAL);
            detailsLayout.setVisibility(View.GONE);

            // Tạo các TextView cho thông tin chi tiết
            addDetailText(detailsLayout, "Giới tính", passenger.getGender());
            addDetailText(detailsLayout, "Ngày sinh", passenger.getDateOfBirth());
            addDetailText(detailsLayout, "Quốc tịch", passenger.getNationality());
            addDetailText(detailsLayout, "Số điện thoại", passenger.getPhone());
            addDetailText(detailsLayout, "Địa chỉ", passenger.getAddress());

            // Thêm các thành phần vào CardView
            cardLayout.addView(headerLayout);
            cardLayout.addView(detailsLayout);
            cardView.addView(cardLayout);
            passengerContainer.addView(cardView);

            // Xử lý sự kiện mở rộng/thu gọn
            headerLayout.setOnClickListener(v -> {
                if (detailsLayout.getVisibility() == View.VISIBLE) {
                    detailsLayout.setVisibility(View.GONE);
                    imgExpand.setImageResource(R.drawable.ic_expand_more);
                } else {
                    detailsLayout.setVisibility(View.VISIBLE);
                    imgExpand.setImageResource(R.drawable.ic_expand_less);
                }
            });
        }
    }

    // Phương thức hỗ trợ để thêm TextView cho thông tin chi tiết
    private void addDetailText(LinearLayout container, String label, String value) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 8, 0, 0); // Khoảng cách giữa các dòng
        textView.setLayoutParams(params);
        textView.setText(label + ": " + (value != null ? value : "N/A"));
        textView.setTextSize(14);
        textView.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        container.addView(textView);
    }
}