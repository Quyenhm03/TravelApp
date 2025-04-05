package com.example.travel_app.UI.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.travel_app.Data.Model.BookingFlight;
import com.example.travel_app.Data.Model.Flight;
import com.example.travel_app.Data.Model.Passenger;
import com.example.travel_app.Data.Model.Payment;
import com.example.travel_app.Data.Model.SearchFlightInfo;
import com.example.travel_app.Data.Model.Seat;
import com.example.travel_app.Data.Model.SelectedFlight;
import com.example.travel_app.R;
import com.example.travel_app.ViewModel.BookingFlightViewModel;
import com.example.travel_app.ViewModel.SeatViewModel;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PaymentFlightActivity extends AppCompatActivity {
    private SearchFlightInfo searchFlightInfo;
    private BookingFlight bookingFlight;
    private List<String> selectedSeatsDeparture = new ArrayList<>();
    private List<String> selectedSeatsReturn = new ArrayList<>();
    private TextView txtAmount;
    private Button btnPayment;
    private BookingFlightViewModel bookingFlightViewModel;
//    private SeatViewModel seatViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_flight);

        searchFlightInfo = (SearchFlightInfo) getIntent().getSerializableExtra("searchFlightInfo");
        bookingFlight = (BookingFlight) getIntent().getSerializableExtra("bookingFlight");

        txtAmount = findViewById(R.id.txt_amount);
        btnPayment = findViewById(R.id.btn_payment);

        // Khởi tạo ViewModel
        bookingFlightViewModel = new ViewModelProvider(this).get(BookingFlightViewModel.class);
//        seatViewModel = new ViewModelProvider(this).get(SeatViewModel.class);

        // Quan sát kết quả lưu BookingFlight
        bookingFlightViewModel.getSaveSuccess().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Đặt vé thành công!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        bookingFlightViewModel.getErrorMessage().observe(this, error -> {
            Toast.makeText(this, "Lỗi khi lưu đặt vé: " + error, Toast.LENGTH_SHORT).show();
        });

        selectedSeatsDeparture = bookingFlight.getSelectedSeatsDeparture();
        setUpDeparturePaymentInfo();
        if (bookingFlight.getReturnFlight() != null) {
            selectedSeatsReturn = bookingFlight.getSelectedSeatsReturn();
            LinearLayout lnReturn = findViewById(R.id.ln_payment_return);
            lnReturn.setVisibility(View.VISIBLE);
            setUpReturnPaymentInfo();
        }

        double total = bookingFlight.getDepartureFlight().getPrice() * searchFlightInfo.getCustomerCount();
        if (bookingFlight.getReturnFlight() != null) {
            total += bookingFlight.getReturnFlight().getPrice() * searchFlightInfo.getCustomerCount();
        }
        bookingFlight.setTotalAmount(total);
        txtAmount.setText(String.valueOf(total));

        // Xử lý sự kiện nhấn nút Payment
        btnPayment.setOnClickListener(v -> saveBooking());
    }

    private void saveBooking() {
        bookingFlight.setId(generateBookingId());
//        bookingFlight.setUserId(getCurrentUserId());
        bookingFlight.setStatus("Đã thanh toán");

        String transactionDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String paymentId = "pay_" + System.currentTimeMillis();
        String paymentMethod = "credit card";
        Payment payment = new Payment(paymentId, bookingFlight.getId(), bookingFlight.getTotalAmount(), "success", paymentMethod, transactionDate);
        bookingFlight.setPayment(payment);

        // Lưu BookingFlight (đã bao gồm cập nhật ghế trong BookingFlightRepository)
        bookingFlightViewModel.saveBooking(bookingFlight);
    }

    private String generateBookingId() {
        return "booking_" + System.currentTimeMillis();
    }

    private String getCurrentUserId() {
        return "user123"; // Thay bằng logic thực tế
    }

    private void setUpDeparturePaymentInfo() {
        TextView txtDepartureDate, txtDepartureTime, txtDepartureCity, txtDepartureCode, txtArrivalTime,
                txtArrivalCity, txtArrivalCode, txtDepartureFlight, txtDepartureSeat, txtDepartureSeatType;
        ImageView imgDeparture;

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
        imgDeparture = findViewById(R.id.img_departure_airline_payment);

        Flight flight = bookingFlight.getDepartureFlight();
        Picasso.get().load(flight.getAirlineImgUrl()).into(imgDeparture);

        txtDepartureDate.setText(flight.getDepartureDate());
        txtDepartureTime.setText(flight.getDepartureTime());
        txtDepartureCity.setText(searchFlightInfo.getDepartureCity());
        txtDepartureCode.setText(flight.getDepartureAirportCode());
        txtArrivalTime.setText(flight.getArrivalTime());
        txtArrivalCity.setText(searchFlightInfo.getArrivalCity());
        txtArrivalCode.setText(flight.getArrivalAirportCode());
        txtDepartureFlight.setText(flight.getFlightNumber());
        txtDepartureSeatType.setText(flight.getSeatType());

        String seat = selectedSeatsDeparture.get(0);
        for (int i = 1; i < selectedSeatsDeparture.size(); i++) {
            seat += "," + selectedSeatsDeparture.get(i);
        }
        txtDepartureSeat.setText(seat);
    }

    private void setUpReturnPaymentInfo() {
        TextView txtReturnDate, txtDepartureTimeReturn, txtDepartureCityReturn, txtDepartureCodeReturn, txtArrivalTimeReturn,
                txtArrivalCityReturn, txtArrivalCodeReturn, txtReturnFlight, txtReturnSeat, txtReturnSeatType;
        ImageView imgReturn;

        txtReturnDate = findViewById(R.id.txt_return_date_payment);
        txtDepartureTimeReturn = findViewById(R.id.txt_departure_time_return_payment);
        txtDepartureCityReturn = findViewById(R.id.txt_departure_city_return_payment);
        txtDepartureCodeReturn = findViewById(R.id.txt_departure_code_return_payment);
        txtArrivalTimeReturn = findViewById(R.id.txt_arrival_time_return_payment);
        txtArrivalCityReturn = findViewById(R.id.txt_arrival_city_return_payment);
        txtArrivalCodeReturn = findViewById(R.id.txt_arrival_code_return_payment);
        txtReturnFlight = findViewById(R.id.txt_return_flight_payment);
        txtReturnSeat = findViewById(R.id.txt_return_seat_payment);
        txtReturnSeatType = findViewById(R.id.txt_return_seat_type_payment);
        imgReturn = findViewById(R.id.img_return_airline_payment);

        Flight flight = bookingFlight.getReturnFlight();
        Picasso.get().load(flight.getAirlineImgUrl()).into(imgReturn);

        txtReturnDate.setText(flight.getDepartureDate());
        txtDepartureTimeReturn.setText(flight.getDepartureTime());
        txtDepartureCityReturn.setText(searchFlightInfo.getArrivalCity());
        txtDepartureCodeReturn.setText(flight.getDepartureAirportCode());
        txtArrivalTimeReturn.setText(flight.getArrivalTime());
        txtArrivalCityReturn.setText(searchFlightInfo.getDepartureCity());
        txtArrivalCodeReturn.setText(flight.getArrivalAirportCode());
        txtReturnFlight.setText(flight.getFlightNumber());
        txtReturnSeatType.setText(flight.getSeatType());

        String seat = selectedSeatsReturn.get(0);
        for (int i = 1; i < selectedSeatsReturn.size(); i++) {
            seat += "," + selectedSeatsReturn.get(i);
        }
        txtReturnSeat.setText(seat);
    }
}