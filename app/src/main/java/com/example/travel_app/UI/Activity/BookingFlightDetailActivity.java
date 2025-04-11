package com.example.travel_app.UI.Activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import com.example.travel_app.Data.Model.BookingFlight;
import com.example.travel_app.Data.Model.Flight;
import com.example.travel_app.Data.Model.Passenger;
import com.example.travel_app.R;
import com.example.travel_app.ViewModel.BookingDetailViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookingDetailActivity extends AppCompatActivity {
    private TextView txtDepartureDate, txtDepartureTime, txtDepartureCity, txtDepartureCode,
            txtArrivalTime, txtArrivalCity, txtArrivalCode, txtDepartureFlight, txtDepartureSeat,
            txtDepartureSeatType, txtReturnDate, txtReturnTime, txtReturnCity, txtReturnCode,
            txtReturnArrivalTime, txtReturnArrivalCity, txtReturnArrivalCode, txtReturnFlight,
            txtReturnSeat, txtReturnSeatType, txtAmount, txtCustomerName, txtBookingDate, txtStatus;
    private ImageView imgDepartureAirline, imgReturnAirline;
    private LinearLayout lnPaymentReturn;
    private AppCompatButton btnBack;
    private LinearLayout passengerContainer;
    private BookingDetailViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_booking);

        // Ánh xạ các view
        txtDepartureDate = findViewById(R.id.txt_departure_date_payment);
        txtDepartureTime = findViewById(R.id.txt_departure_time_payment);
        txtDepartureCity = findViewById(R.id.txt_departure_city_payment);
        txtDepartureCode = findViewById(R.id.txt_departure_code_payment);
        txtArrivalTime = findViewById(R.id.txt_arrival_time_payment);
        txtArrivalCity = findViewById(R.id.txt_arrival_city_payment);
        txtArrivalCode = findViewById(R.id.txt_arrival_code_payment);
        txtDepartureFlight = findViewById(R.id.txt_departure_flight_payment);
        txtDepartureSeat = findViewById(R.id.txt_departure_seat_payment);
        txtDepartureSeatType = findViewById(R.id.txt_departure_seat_type_payment);
        txtReturnDate = findViewById(R.id.txt_return_date_payment);
        txtReturnTime = findViewById(R.id.txt_departure_time_return_payment);
        txtReturnCity = findViewById(R.id.txt_departure_city_return_payment);
        txtReturnCode = findViewById(R.id.txt_departure_code_return_payment);
        txtReturnArrivalTime = findViewById(R.id.txt_arrival_time_return_payment);
        txtReturnArrivalCity = findViewById(R.id.txt_arrival_city_return_payment);
        txtReturnArrivalCode = findViewById(R.id.txt_arrival_code_return_payment);
        txtReturnFlight = findViewById(R.id.txt_return_flight_payment);
        txtReturnSeat = findViewById(R.id.txt_return_seat_payment);
        txtReturnSeatType = findViewById(R.id.txt_return_seat_type_payment);
        txtAmount = findViewById(R.id.txt_amount);
        txtCustomerName = findViewById(R.id.txt_customer_name);
        txtBookingDate = findViewById(R.id.txt_booking_date);
        txtStatus = findViewById(R.id.txt_status);
        imgDepartureAirline = findViewById(R.id.img_departure_airline_payment);
        imgReturnAirline = findViewById(R.id.img_return_airline_payment);
        lnPaymentReturn = findViewById(R.id.ln_payment_return);
        btnBack = findViewById(R.id.btn_back);
        passengerContainer = findViewById(R.id.passenger_container);

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(BookingDetailViewModel.class);

        // Lấy dữ liệu từ Intent
        BookingFlight booking = (BookingFlight) getIntent().getSerializableExtra("bookingFlight");
        if (booking == null) {
            Toast.makeText(this, "Không tìm thấy thông tin đặt vé", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Cập nhật ViewModel
        viewModel.setBooking(booking);

        // Quan sát dữ liệu từ ViewModel
        viewModel.getBooking().observe(this, this::updateBookingDetails);
        viewModel.getPassengerList().observe(this, this::displayPassengerList);

        // Xử lý nút Quay lại
        btnBack.setOnClickListener(v -> finish());
    }

    private void updateBookingDetails(BookingFlight booking) {
        // Hiển thị thông tin chuyến đi
        Flight departureFlight = booking.getDepartureFlight();
        txtDepartureDate.setText(departureFlight.getDepartureDate());
        txtDepartureTime.setText(departureFlight.getDepartureTime());
        txtDepartureCity.setText(booking.getDepartureCity());
        txtDepartureCode.setText(departureFlight.getDepartureAirportCode());
        txtArrivalTime.setText(departureFlight.getArrivalTime());
        txtArrivalCity.setText(booking.getArrivalCity());
        txtArrivalCode.setText(departureFlight.getArrivalAirportCode());
        txtDepartureFlight.setText(departureFlight.getFlightNumber());
        txtDepartureSeat.setText(booking.getSelectedSeatsDeparture() != null ? String.join(", ", booking.getSelectedSeatsDeparture()) : "N/A");
        txtDepartureSeatType.setText(departureFlight.getSeatType());
        Picasso.get().load(departureFlight.getAirlineImgUrl()).into(imgDepartureAirline);

        // Hiển thị thông tin chuyến về (nếu có)
        if (booking.getReturnFlight() != null) {
            lnPaymentReturn.setVisibility(View.VISIBLE);
            Flight returnFlight = booking.getReturnFlight();
            txtReturnDate.setText(returnFlight.getDepartureDate());
            txtReturnTime.setText(returnFlight.getDepartureTime());
            txtReturnCity.setText(booking.getArrivalCity());
            txtReturnCode.setText(returnFlight.getDepartureAirportCode());
            txtReturnArrivalTime.setText(returnFlight.getArrivalTime());
            txtReturnArrivalCity.setText(booking.getDepartureCity());
            txtReturnArrivalCode.setText(returnFlight.getArrivalAirportCode());
            txtReturnFlight.setText(returnFlight.getFlightNumber());
            txtReturnSeat.setText(booking.getSelectedSeatsReturn() != null ? String.join(", ", booking.getSelectedSeatsReturn()) : "N/A");
            txtReturnSeatType.setText(returnFlight.getSeatType());
            Picasso.get().load(returnFlight.getAirlineImgUrl()).into(imgReturnAirline);
        } else {
            lnPaymentReturn.setVisibility(View.GONE);
        }

        // Hiển thị thông tin tổng quan
        txtAmount.setText(String.format("%,.0f VND", booking.getTotalAmount()));
        txtCustomerName.setText(booking.getPassengerList().get(0).getFullName());
        txtBookingDate.setText(booking.getPayment().getTransactionDate());
        txtStatus.setText(booking.getStatus());
    }

    private void displayPassengerList(List<Passenger> passengerList) {
        if (passengerList == null || passengerList.isEmpty()) {
            TextView noPassengerText = new TextView(this);
            noPassengerText.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            noPassengerText.setText("Không có thông tin hành khách");
            noPassengerText.setTextSize(16);
            noPassengerText.setTextColor(getResources().getColor(android.R.color.darker_gray));
            noPassengerText.setPadding(0, 20, 0, 20);
            passengerContainer.addView(noPassengerText);
            return;
        }

        for (int i = 0; i < passengerList.size(); i++) {
            Passenger passenger = passengerList.get(i);

            // Tạo CardView cho mỗi hành khách
            CardView cardView = new CardView(this);
            cardView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            cardView.setCardElevation(4);
            cardView.setRadius(8);
            cardView.setUseCompatPadding(true);
            cardView.setContentPadding(16, 16, 16, 16);

            LinearLayout cardLayout = new LinearLayout(this);
            cardLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            cardLayout.setOrientation(LinearLayout.VERTICAL);

            // Header của CardView (Tên hành khách và nút mở rộng)
            LinearLayout headerLayout = new LinearLayout(this);
            headerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            headerLayout.setOrientation(LinearLayout.HORIZONTAL);
            headerLayout.setGravity(Gravity.CENTER_VERTICAL);

            TextView txtPassengerName = new TextView(this);
            txtPassengerName.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1));
            txtPassengerName.setText("Hành khách " + (i + 1) + ": " + (passenger.getFullName() != null ? passenger.getFullName() : "N/A"));
            txtPassengerName.setTextSize(16);
//            txtPassengerName.setTextStyle(Typeface.BOLD);

            ImageView imgExpand = new ImageView(this);
            imgExpand.setLayoutParams(new LinearLayout.LayoutParams(24, 24));
            imgExpand.setImageResource(R.drawable.ic_expand_more);

            headerLayout.addView(txtPassengerName);
            headerLayout.addView(imgExpand);

            // Layout chứa thông tin chi tiết (ban đầu ẩn)
            LinearLayout detailsLayout = new LinearLayout(this);
            detailsLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            detailsLayout.setOrientation(LinearLayout.VERTICAL);
            detailsLayout.setVisibility(View.GONE);

            TextView txtGender = new TextView(this);
            txtGender.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            txtGender.setText("Giới tính: " + (passenger.getGender() != null ? passenger.getGender() : "N/A"));
            txtGender.setTextSize(14);
            txtGender.setTextColor(getResources().getColor(android.R.color.black));
            txtGender.setPadding(0, 5, 0, 0);

            TextView txtDateOfBirth = new TextView(this);
            txtDateOfBirth.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            txtDateOfBirth.setText("Ngày sinh: " + (passenger.getDateOfBirth() != null ? passenger.getDateOfBirth() : "N/A"));
            txtDateOfBirth.setTextSize(14);
            txtDateOfBirth.setTextColor(getResources().getColor(android.R.color.black));
            txtDateOfBirth.setPadding(0, 5, 0, 0);

            TextView txtNationality = new TextView(this);
            txtNationality.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            txtNationality.setText("Quốc tịch: " + (passenger.getNationality() != null ? passenger.getNationality() : "N/A"));
            txtNationality.setTextSize(14);
            txtNationality.setTextColor(getResources().getColor(android.R.color.black));
            txtNationality.setPadding(0, 5, 0, 0);

            TextView txtPhone = new TextView(this);
            txtPhone.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            txtPhone.setText("Số điện thoại: " + (passenger.getPhone() != null ? passenger.getPhone() : "N/A"));
            txtPhone.setTextSize(14);
            txtPhone.setTextColor(getResources().getColor(android.R.color.black));
            txtPhone.setPadding(0, 5, 0, 0);

            TextView txtAddress = new TextView(this);
            txtAddress.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            txtAddress.setText("Địa chỉ: " + (passenger.getAddress() != null ? passenger.getAddress() : "N/A"));
            txtAddress.setTextSize(14);
            txtAddress.setTextColor(getResources().getColor(android.R.color.black));
            txtAddress.setPadding(0, 5, 0, 0);

            detailsLayout.addView(txtGender);
            detailsLayout.addView(txtDateOfBirth);
            detailsLayout.addView(txtNationality);
            detailsLayout.addView(txtPhone);
            detailsLayout.addView(txtAddress);

            cardLayout.addView(headerLayout);
            cardLayout.addView(detailsLayout);
            cardView.addView(cardLayout);
            passengerContainer.addView(cardView);

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
}