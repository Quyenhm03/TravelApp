package com.example.travel_app.UI.Activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.travel_app.Api.CreateOrder;
import com.example.travel_app.Data.Model.BookingFlight;
import com.example.travel_app.Data.Model.Flight;
import com.example.travel_app.Data.Model.Payment;
import com.example.travel_app.Data.Model.SearchFlightInfo;
import com.example.travel_app.R;
import com.example.travel_app.Receiver.ReminderBroadcastReceiver;
import com.example.travel_app.UI.Login.LoginActivity;
import com.example.travel_app.ViewModel.BookingFlightViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//import vn.zalopay.sdk.Environment;
//import vn.zalopay.sdk.ZaloPayError;
//import vn.zalopay.sdk.ZaloPaySDK;
//import vn.zalopay.sdk.listeners.PayOrderListener;

public class PaymentFlightActivity extends BaseActivity {
    private SearchFlightInfo searchFlightInfo;
    private BookingFlight bookingFlight;
    private List<String> selectedSeatsDeparture = new ArrayList<>();
    private List<String> selectedSeatsReturn = new ArrayList<>();
    private TextView txtAmount;
    private Button btnPayment;
    private BookingFlightViewModel bookingFlightViewModel;
    private double total;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_flight);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        //ZaloPaySDK.init(2553, Environment.SANDBOX);

        searchFlightInfo = (SearchFlightInfo) getIntent().getSerializableExtra("searchFlightInfo");
        bookingFlight = (BookingFlight) getIntent().getSerializableExtra("bookingFlight");

        txtAmount = findViewById(R.id.txt_amount);
        btnPayment = findViewById(R.id.btn_payment);

        bookingFlightViewModel = new ViewModelProvider(this).get(BookingFlightViewModel.class);

        bookingFlightViewModel.getSaveSuccess().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Đặt vé thành công!", Toast.LENGTH_SHORT).show();
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

        total = bookingFlight.getDepartureFlight().getPrice() * searchFlightInfo.getCustomerCount();
        if (bookingFlight.getReturnFlight() != null) {
            total += bookingFlight.getReturnFlight().getPrice() * searchFlightInfo.getCustomerCount();
        }
        bookingFlight.setTotalAmount(total);

        String formattedPrice = String.format("%,.0f VNĐ", total);
        txtAmount.setText(formattedPrice);

       // btnPayment.setOnClickListener(v -> saveBooking());
    }

//    private void saveBooking() {
//        CreateOrder orderApi = new CreateOrder();
//
//        try {
//            JSONObject data = orderApi.createOrder(String.valueOf((int) total));
//            Log.d("PaymentDebug", "CreateOrder Response: " + data.toString());
//            String code = data.getString("return_code");
//            Log.d("PaymentDebug", "Return Code: " + code);
//
//            if (code.equals("1")) {
//                String token = data.getString("zp_trans_token");
//                Log.d("PaymentDebug", "Token: " + token);
//                ZaloPaySDK.getInstance().payOrder(PaymentFlightActivity.this, token, "demozpdk://flight", new PayOrderListener() {
//                    @Override
//                    public void onPaymentSucceeded(String s, String s1, String s2) {
//                        Log.d("PaymentDebug", "Payment Succeeded");
//                        bookingFlight.setStatus("Đã thanh toán");
//                        String transactionDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
//                        String paymentId = "pay_" + System.currentTimeMillis();
//                        String paymentMethod = "ZaloPay";
//                        Payment payment = new Payment(paymentId, bookingFlight.getId(), bookingFlight.getTotalAmount(), "success", paymentMethod, transactionDate);
//                        bookingFlight.setPayment(payment);
//                        bookingFlight.setUserId(getUserId());
//
//                        // Tính departureTimestamp và returnTimestamp (nếu có)
//                        try {
//                            // Chuyến đi
//                            String departureDate = bookingFlight.getDepartureFlight().getDepartureDate();
//                            String departureTime = bookingFlight.getDepartureFlight().getDepartureTime();
//                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
//                            Date departureDateTime = sdf.parse(departureDate + " " + departureTime);
//                            long departureTimestamp = departureDateTime.getTime();
//                            bookingFlight.setDepartureTimestamp(departureTimestamp);
//
//                            // Đặt lịch thông báo cho chuyến đi
//                            long departureReminderTime = departureTimestamp - 30 * 60 * 1000;
//                            scheduleReminder(
//                                    PaymentFlightActivity.this,
//                                    "Chuyến bay đi từ " + bookingFlight.getDepartureCity() + " đến " +
//                                            bookingFlight.getArrivalCity() + " sẽ khởi hành lúc " + departureTime,
//                                    departureReminderTime
//                            );
//
//                            if (bookingFlight.getReturnFlight() != null) {
//                                String returnDate = bookingFlight.getReturnFlight().getDepartureDate();
//                                String returnTime = bookingFlight.getReturnFlight().getDepartureTime();
//                                Date returnDateTime = sdf.parse(returnDate + " " + returnTime);
//                                long returnTimestamp = returnDateTime.getTime();
//                                bookingFlight.setReturnTimestamp(returnTimestamp);
//
//                                // Đặt lịch thông báo cho chuyến về
//                                long returnReminderTime = returnTimestamp - 30 * 60 * 1000; // 30 phút trước
//                                scheduleReminder(
//                                        PaymentFlightActivity.this,
//                                        "Chuyến bay về từ " + bookingFlight.getArrivalCity() + " đến " +
//                                                bookingFlight.getDepartureCity() + " sẽ khởi hành lúc " + returnTime,
//                                        returnReminderTime
//                                );
//                            }
//                        } catch (Exception e) {
//                            Log.e("PaymentFlightActivity", "Error parsing departure/return date/time: " + e.getMessage());
//                        }
//
//                        bookingFlightViewModel.saveBooking(bookingFlight);
//                        Toast.makeText(PaymentFlightActivity.this, "Thanh toán thành công!", Toast.LENGTH_LONG).show();
//                        btnPayment.setEnabled(false);
//                        btnPayment.setText("Đã thanh toán");
//
//                        // Chuyển về HomeActivity và xóa stack điều hướng
//                        Intent intent = new Intent(PaymentFlightActivity.this, HomeActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        startActivity(intent);
//                        finish();
//                    }
//
//                    @Override
//                    public void onPaymentCanceled(String s, String s1) {
//                        Log.d("PaymentDebug", "Payment Canceled");
//                        Toast.makeText(PaymentFlightActivity.this, "Hủy thanh toán!", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
//                        Log.d("PaymentDebug", "Payment Error: " + zaloPayError.toString());
//                        Toast.makeText(PaymentFlightActivity.this, "Lỗi: " + zaloPayError.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            } else {
//                Log.d("PaymentDebug", "CreateOrder failed with code: " + code);
//                Toast.makeText(this, "Tạo đơn hàng thất bại, mã lỗi: " + code, Toast.LENGTH_SHORT).show();
//            }
//
//        } catch (Exception e) {
//            Log.e("PaymentDebug", "Exception: " + e.getMessage());
//            e.printStackTrace();
//            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//
//        bookingFlight.setId(generateBookingId());
//    }

    private String getUserId(){
        // Lấy userId từ FirebaseAuth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // Người dùng chưa đăng nhập
            Toast.makeText(this, "Bạn cần đăng nhập để thực hiện thanh toán", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return currentUser.getUid();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //ZaloPaySDK.getInstance().onResult(intent);
    }

    @SuppressLint("ScheduleExactAlarm")
    private void scheduleReminder(Context context, String message, long triggerTime) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Log.e("ReminderDebug", "Cannot schedule exact alarm due to missing permission");
                return;
            }
        }

        Intent intent = new Intent(context, ReminderBroadcastReceiver.class);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                message.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Đặt lịch thông báo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        }
    }

    private String generateBookingId() {
        return "booking_" + System.currentTimeMillis();
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