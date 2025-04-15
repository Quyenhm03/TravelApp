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
import androidx.lifecycle.ViewModelProvider;

import com.example.travel_app.Data.Model.BookingFlight;
import com.example.travel_app.Data.Model.Flight;
import com.example.travel_app.Data.Model.Passenger;
import com.example.travel_app.R;
import com.example.travel_app.ViewModel.BookingDetailViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookingFlightDetailActivity extends BaseActivity {
    private TextView txtDepartureDate, txtDepartureTime, txtDepartureCity, txtDepartureCode,
            txtArrivalTime, txtArrivalCity, txtArrivalCode, txtDepartureFlight, txtDepartureSeat,
            txtDepartureSeatType, txtReturnDate, txtReturnTime, txtReturnCity, txtReturnCode,
            txtReturnArrivalTime, txtReturnArrivalCity, txtReturnArrivalCode, txtReturnFlight,
            txtReturnSeat, txtReturnSeatType, txtAmount, txtCustomerName, txtBookingDate, txtStatus;
    private ImageView imgDepartureAirline, imgReturnAirline;
    private LinearLayout lnPaymentReturn;
    private AppCompatButton btnBack, btnBack2;
    private LinearLayout passengerContainer;
    private BookingDetailViewModel viewModel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_booking_flight);

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
        btnBack2 = findViewById(R.id.btn_back2);

        viewModel = new ViewModelProvider(this).get(BookingDetailViewModel.class);

        BookingFlight booking = (BookingFlight) getIntent().getSerializableExtra("bookingFlight");
        if (booking == null) {
            Toast.makeText(this, "Không tìm thấy thông tin đặt vé", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel.setBooking(booking);

        viewModel.getBooking().observe(this, this::updateBookingDetails);
        viewModel.getPassengerList().observe(this, this::displayPassengerList);

        btnBack.setOnClickListener(v -> finish());
        btnBack2.setOnClickListener(v -> finish());
    }

    private void updateBookingDetails(BookingFlight booking) {
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

        txtAmount.setText(String.format("%,.0f VND", booking.getTotalAmount()));
        txtCustomerName.setText(booking.getPassengerList().get(0).getFullName());
        txtBookingDate.setText(booking.getPayment().getTransactionDate());
        txtStatus.setText(booking.getStatus());
    }

    private void displayPassengerList(List<Passenger> passengerList) {
        if (passengerList == null || passengerList.isEmpty()) {
            TextView noPassengerText = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 20, 0, 20);
            noPassengerText.setLayoutParams(params);
            noPassengerText.setText("Không có thông tin hành khách");
            noPassengerText.setTextSize(16);
            noPassengerText.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
            noPassengerText.setGravity(Gravity.CENTER);
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
            cardParams.setMargins(2, 25, 2, 25);
            cardView.setLayoutParams(cardParams);
            cardView.setCardElevation(4);
            cardView.setRadius(12);
            cardView.setUseCompatPadding(false);
            cardView.setContentPadding(20, 25, 20, 25);

            // Tạo LinearLayout chính trong CardView
            LinearLayout cardLayout = new LinearLayout(this);
            cardLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            cardLayout.setOrientation(LinearLayout.VERTICAL);

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
        params.setMargins(0, 8, 0, 0);
        textView.setLayoutParams(params);
        textView.setText(label + ": " + (value != null ? value : "N/A"));
        textView.setTextSize(14);
        textView.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        container.addView(textView);
    }
}