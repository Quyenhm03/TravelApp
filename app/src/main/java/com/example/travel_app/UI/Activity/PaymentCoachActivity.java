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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import com.example.travel_app.Api.CreateOrder;
import com.example.travel_app.Data.Model.BookingCoach;
import com.example.travel_app.Data.Model.Coach;
import com.example.travel_app.Data.Model.Payment;
import com.example.travel_app.Data.Model.SearchCoachInfo;
import com.example.travel_app.R;
import com.example.travel_app.Receiver.ReminderBroadcastReceiver;
import com.example.travel_app.UI.Login.LoginActivity;
import com.example.travel_app.ViewModel.BookingCoachViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class PaymentCoachActivity extends BaseActivity {
    private SearchCoachInfo searchCoachInfo;
    private BookingCoach bookingCoach;
    private List<String> selectedSeatsDeparture = new ArrayList<>();
    private List<String> selectedSeatsReturn = new ArrayList<>();
    private TextView txtAmount;
    private Button btnPayment;
    private BookingCoachViewModel bookingCoachViewModel;
    private double total;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_coach);

        // Cấu hình StrictMode để cho phép thực hiện các tác vụ mạng trên main thread (chỉ để test)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Khởi tạo ZaloPay SDK
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        // Lấy dữ liệu từ Intent
        searchCoachInfo = (SearchCoachInfo) getIntent().getSerializableExtra("searchCoachInfo");
        bookingCoach = (BookingCoach) getIntent().getSerializableExtra("bookingCoach");

        // Kiểm tra searchCoachInfo và bookingCoach
        if (searchCoachInfo == null) {
            Toast.makeText(this, "Không tìm thấy thông tin tìm kiếm chuyến xe!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (bookingCoach == null) {
            Toast.makeText(this, "Không tìm thấy thông tin đặt vé!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Ánh xạ các view
        txtAmount = findViewById(R.id.txt_amount);
        btnPayment = findViewById(R.id.btn_payment);

        // Khởi tạo ViewModel
        bookingCoachViewModel = new ViewModelProvider(this).get(BookingCoachViewModel.class);

        // Quan sát kết quả lưu đặt vé
        bookingCoachViewModel.getSaveSuccess().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Đặt vé thành công!", Toast.LENGTH_SHORT).show();
            }
        });

        bookingCoachViewModel.getErrorMessage().observe(this, error -> {
            Toast.makeText(this, "Lỗi khi lưu đặt vé: " + error, Toast.LENGTH_SHORT).show();
        });

        // Kiểm tra và lấy danh sách ghế đã chọn
        if (bookingCoach.getSelectedSeatsDeparture() != null) {
            selectedSeatsDeparture = bookingCoach.getSelectedSeatsDeparture();
        } else {
            Log.w("PaymentCoachActivity", "selectedSeatsDeparture is null");
            selectedSeatsDeparture = new ArrayList<>(); // Khởi tạo mặc định để tránh lỗi
        }
        setUpDeparturePaymentInfo();

        // Kiểm tra nếu có chuyến về
        if (bookingCoach.getReturnCoach() != null) {
            if (bookingCoach.getSelectedSeatsReturn() != null) {
                selectedSeatsReturn = bookingCoach.getSelectedSeatsReturn();
            } else {
                Log.w("PaymentCoachActivity", "selectedSeatsReturn is null");
                selectedSeatsReturn = new ArrayList<>(); // Khởi tạo mặc định để tránh lỗi
            }
            LinearLayout lnReturn = findViewById(R.id.ln_payment_return);
            lnReturn.setVisibility(View.VISIBLE);
            setUpReturnPaymentInfo();
        }

        // Tính tổng số tiền
        total = bookingCoach.getDepartureCoach().getPrice() * searchCoachInfo.getSeatCount();
        if (bookingCoach.getReturnCoach() != null) {
            total += bookingCoach.getReturnCoach().getPrice() * searchCoachInfo.getSeatCount();
        }
        bookingCoach.setTotalAmount(total);

        // Hiển thị tổng số tiền
        String formattedPrice = String.format("%,.0f VNĐ", total);
        txtAmount.setText(formattedPrice);

        // Xử lý nút "Thanh toán"
        btnPayment.setOnClickListener(v -> saveBooking());

        // Xử lý nút "Back"
        AppCompatButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
    }

    private void saveBooking() {
        CreateOrder orderApi = new CreateOrder();

        try {
            JSONObject data = orderApi.createOrder(String.valueOf((int) total));
            Log.d("PaymentDebug", "CreateOrder Response: " + data.toString());
            String code = data.getString("return_code");
            Log.d("PaymentDebug", "Return Code: " + code);

            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                Log.d("PaymentDebug", "Token: " + token);
                ZaloPaySDK.getInstance().payOrder(PaymentCoachActivity.this, token, "demozpdk://coach", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {
                        Log.d("PaymentDebug", "Payment Succeeded");
                        bookingCoach.setStatus("Đã thanh toán");
                        String transactionDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                        String paymentId = "pay_" + System.currentTimeMillis();
                        String paymentMethod = "ZaloPay";
                        Payment payment = new Payment(paymentId, bookingCoach.getId(), bookingCoach.getTotalAmount(), "success", paymentMethod, transactionDate);
                        bookingCoach.setPayment(payment);

                        // Tính departureTimestamp và returnTimestamp
                        try {
                            String departureDate = bookingCoach.getDepartureCoach().getDepartureDate();
                            String departureTime = bookingCoach.getDepartureCoach().getDepartureTime();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                            Date departureDateTime = sdf.parse(departureDate + " " + departureTime);
                            long departureTimestamp = departureDateTime.getTime();
                            bookingCoach.setDepartureTimestamp(departureTimestamp);
                            bookingCoach.setUserId(getUserId());

                            // Đặt lịch thông báo cho chuyến đi
                            long departureReminderTime = departureTimestamp - 30 * 60 * 1000; // 30 phút trước
                            scheduleReminder(
                                    PaymentCoachActivity.this,
                                    "Chuyến xe đi từ " + bookingCoach.getDepartureCity() + " đến " +
                                            bookingCoach.getArrivalCity() + " sẽ khởi hành lúc " + departureTime,
                                    departureReminderTime
                            );

                            if (bookingCoach.getReturnCoach() != null) {
                                String returnDate = bookingCoach.getReturnCoach().getDepartureDate();
                                String returnTime = bookingCoach.getReturnCoach().getDepartureTime();
                                Date returnDateTime = sdf.parse(returnDate + " " + returnTime);
                                long returnTimestamp = returnDateTime.getTime();
                                bookingCoach.setReturnTimestamp(returnTimestamp);

                                // Đặt lịch thông báo cho chuyến về
                                long returnReminderTime = returnTimestamp - 30 * 60 * 1000; // 30 phút trước
                                scheduleReminder(
                                        PaymentCoachActivity.this,
                                        "Chuyến xe về từ " + bookingCoach.getArrivalCity() + " đến " +
                                                bookingCoach.getDepartureCity() + " sẽ khởi hành lúc " + returnTime,
                                        returnReminderTime
                                );
                            }
                        } catch (Exception e) {
                            Log.e("PaymentCoachActivity", "Error parsing departure/return date/time: " + e.getMessage());
                        }

                        bookingCoachViewModel.saveBooking(bookingCoach);
                        Toast.makeText(PaymentCoachActivity.this, "Thanh toán thành công!", Toast.LENGTH_LONG).show();
                        btnPayment.setEnabled(false);
                        btnPayment.setText("Đã thanh toán");

                        // Chuyển về HomeActivity và xóa stack điều hướng
                        Intent intent = new Intent(PaymentCoachActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {
                        Log.d("PaymentDebug", "Payment Canceled");
                        Toast.makeText(PaymentCoachActivity.this, "Hủy thanh toán!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                        Log.d("PaymentDebug", "Payment Error: " + zaloPayError.toString());
                        Toast.makeText(PaymentCoachActivity.this, "Lỗi: " + zaloPayError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Log.d("PaymentDebug", "CreateOrder failed with code: " + code);
                Toast.makeText(this, "Tạo đơn hàng thất bại, mã lỗi: " + code, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e("PaymentDebug", "Exception: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        bookingCoach.setId(generateBookingId());
    }

    @SuppressLint("ScheduleExactAlarm")
    private void scheduleReminder(Context context, String message, long triggerTime) {
        try {
            Log.d("ReminderDebug", "Scheduling reminder for: " + message + " at time: " + new Date(triggerTime));

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
                Log.d("ReminderDebug", "Notification scheduled using setExactAndAllowWhileIdle");
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                Log.d("ReminderDebug", "Notification scheduled using setExact");
            }
        } catch (Exception e) {
            Log.e("ReminderDebug", "Error scheduling reminder: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

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

    private String generateBookingId() {
        return "booking_" + System.currentTimeMillis();
    }

    private void setUpDeparturePaymentInfo() {
        TextView txtDepartureCoachName, txtDepartureDate, txtDepartureTime, txtDepartureCity, txtDepartureStation,
                txtArrivalTime, txtArrivalCity, txtArrivalStation, txtDepartureCoach, txtDepartureSeat;

        txtDepartureCoachName = findViewById(R.id.txt_departure_coach_name);
        txtDepartureDate = findViewById(R.id.txt_departure_date_payment);
        txtDepartureTime = findViewById(R.id.txt_departure_time_payment);
        txtDepartureCity = findViewById(R.id.txt_departure_city_payment);
        txtDepartureStation = findViewById(R.id.txt_departure_station_name);
        txtArrivalTime = findViewById(R.id.txt_arrival_time_payment);
        txtArrivalCity = findViewById(R.id.txt_arrival_city_payment);
        txtArrivalStation = findViewById(R.id.txt_arrival_station_name);
        txtDepartureCoach = findViewById(R.id.txt_departure_coach_payment);
        txtDepartureSeat = findViewById(R.id.txt_departure_seat_payment);

        Coach coach = bookingCoach.getDepartureCoach();
        if (coach == null) {
            Toast.makeText(this, "Không tìm thấy thông tin chuyến xe đi!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        txtDepartureCoachName.setText(coach.getCompanyName() != null ? coach.getCompanyName() : "N/A");
        txtDepartureDate.setText(coach.getDepartureDate() != null ? coach.getDepartureDate() : "N/A");
        txtDepartureTime.setText(coach.getDepartureTime() != null ? coach.getDepartureTime() : "N/A");
        txtDepartureCity.setText(searchCoachInfo.getDepartureCity() != null ? searchCoachInfo.getDepartureCity() : "N/A");
        txtDepartureStation.setText(coach.getDepartureStationName() != null ? coach.getDepartureStationName() : "N/A");
        txtArrivalTime.setText(coach.getArrivalTime() != null ? coach.getArrivalTime() : "N/A");
        txtArrivalCity.setText(searchCoachInfo.getArrivalCity() != null ? searchCoachInfo.getArrivalCity() : "N/A");
        txtArrivalStation.setText(coach.getArrivalStationName() != null ? coach.getArrivalStationName() : "N/A");
        txtDepartureCoach.setText(coach.getCoachNumber() != null ? coach.getCoachNumber() : "N/A");

        String seat = selectedSeatsDeparture != null && !selectedSeatsDeparture.isEmpty() ? selectedSeatsDeparture.get(0) : "N/A";
        for (int i = 1; i < (selectedSeatsDeparture != null ? selectedSeatsDeparture.size() : 0); i++) {
            seat += "," + selectedSeatsDeparture.get(i);
        }
        txtDepartureSeat.setText(seat);
    }

    private void setUpReturnPaymentInfo() {
        TextView txtReturnCoachName, txtReturnDate, txtDepartureTimeReturn, txtDepartureCityReturn, txtDepartureStationReturn,
                txtArrivalTimeReturn, txtArrivalCityReturn, txtArrivalStationReturn, txtReturnCoach, txtReturnSeat;

        txtReturnCoachName = findViewById(R.id.txt_return_coach_name);
        txtReturnDate = findViewById(R.id.txt_return_date_payment);
        txtDepartureTimeReturn = findViewById(R.id.txt_departure_time_return_payment);
        txtDepartureCityReturn = findViewById(R.id.txt_departure_city_return_payment);
        txtDepartureStationReturn = findViewById(R.id.txt_departure_station_name_return);
        txtArrivalTimeReturn = findViewById(R.id.txt_arrival_time_return_payment);
        txtArrivalCityReturn = findViewById(R.id.txt_arrival_city_return_payment);
        txtArrivalStationReturn = findViewById(R.id.txt_arrival_station_name_return);
        txtReturnCoach = findViewById(R.id.txt_return_coach_payment);
        txtReturnSeat = findViewById(R.id.txt_return_seat_payment);

        Coach coach = bookingCoach.getReturnCoach();
        if (coach == null) {
            Toast.makeText(this, "Không tìm thấy thông tin chuyến xe về!", Toast.LENGTH_LONG).show();
            return;
        }

        txtReturnCoachName.setText(coach.getCompanyName() != null ? coach.getCompanyName() : "N/A");
        txtReturnDate.setText(coach.getDepartureDate() != null ? coach.getDepartureDate() : "N/A");
        txtDepartureTimeReturn.setText(coach.getDepartureTime() != null ? coach.getDepartureTime() : "N/A");
        txtDepartureCityReturn.setText(searchCoachInfo.getArrivalCity() != null ? searchCoachInfo.getArrivalCity() : "N/A");
        txtDepartureStationReturn.setText(coach.getDepartureStationName() != null ? coach.getDepartureStationName() : "N/A");
        txtArrivalTimeReturn.setText(coach.getArrivalTime() != null ? coach.getArrivalTime() : "N/A");
        txtArrivalCityReturn.setText(searchCoachInfo.getDepartureCity() != null ? searchCoachInfo.getDepartureCity() : "N/A");
        txtArrivalStationReturn.setText(coach.getArrivalStationName() != null ? coach.getArrivalStationName() : "N/A");
        txtReturnCoach.setText(coach.getCoachNumber() != null ? coach.getCoachNumber() : "N/A");

        String seat = selectedSeatsReturn != null && !selectedSeatsReturn.isEmpty() ? selectedSeatsReturn.get(0) : "N/A";
        for (int i = 1; i < (selectedSeatsReturn != null ? selectedSeatsReturn.size() : 0); i++) {
            seat += "," + selectedSeatsReturn.get(i);
        }
        txtReturnSeat.setText(seat);
    }
}