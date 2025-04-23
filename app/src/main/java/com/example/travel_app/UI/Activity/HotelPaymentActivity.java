package com.example.travel_app.UI.Activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import com.example.travel_app.Data.Model.BookingHotel;
import com.example.travel_app.Data.Model.Payment;
import com.example.travel_app.R;
import com.example.travel_app.Receiver.ReminderBroadcastReceiver;
import com.example.travel_app.SharedPreferencesUtils;
import com.example.travel_app.UI.Login.LoginActivity;
import com.example.travel_app.ViewModel.HotelDetailViewModel;
import com.example.travel_app.ViewModel.RoomViewModel;
import com.example.travel_app.ViewModel.UserCurrentViewModel;
import com.example.travel_app.ViewModel.Itinerary.BookingHotelViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HotelPaymentActivity extends AppCompatActivity {
    private TextView txtCustomerName, txtBookingDate, txtHotelName, txtCheckInDate, txtCheckOutDate, txtRoomType, txtTotalAmount;
    private EditText edtCardNumber, edtCardHolderName, edtExpiryDate;
    private Button btnConfirmPayment;

    private UserCurrentViewModel userCurrentViewModel;
    private RoomViewModel roomViewModel;
    private HotelDetailViewModel hotelDetailViewModel;
    private BookingHotelViewModel bookingHotelViewModel;

    private static final String TAG = "HotelPaymentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hotel_payment);

        // Đặt màu cho status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.orange));
        }

        // Bỏ qua StrictMode để gọi API (chỉ dùng cho testing, không nên dùng trong production)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Ánh xạ các view từ layout
        txtCustomerName = findViewById(R.id.txtCustomerName);
        txtBookingDate = findViewById(R.id.txtBookingDate);
        txtHotelName = findViewById(R.id.txtHotelName);
        txtCheckInDate = findViewById(R.id.txtCheckInDate);
        txtCheckOutDate = findViewById(R.id.txtCheckOutDate);
        txtRoomType = findViewById(R.id.txtRoomType);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);
        edtCardNumber = findViewById(R.id.edtCardNumber);
        edtCardHolderName = findViewById(R.id.edtCardHolderName);
        edtExpiryDate = findViewById(R.id.edtExpiryDate);
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);

        // Khởi tạo ViewModel
        userCurrentViewModel = new ViewModelProvider(this).get(UserCurrentViewModel.class);
        roomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);
        hotelDetailViewModel = new ViewModelProvider(this).get(HotelDetailViewModel.class);
        bookingHotelViewModel = new ViewModelProvider(this).get(BookingHotelViewModel.class);

        // Quan sát trạng thái lưu đặt phòng
        bookingHotelViewModel.getSaveSuccess().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Thanh toán và lưu đặt phòng thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HotelPaymentActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        bookingHotelViewModel.getErrorMessage().observe(this, error -> {
            Toast.makeText(this, "Lỗi khi lưu đặt phòng: " + error, Toast.LENGTH_SHORT).show();
        });

        // Quan sát khách sạn được chọn
        hotelDetailViewModel.getSelectedHotel().observe(this, hotel -> {
            if (hotel == null) {
                Toast.makeText(this, "Không tìm thấy thông tin khách sạn!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Hiển thị thông tin đặt phòng
        displayHotelDetails();

        // Xử lý sự kiện nút xác nhận thanh toán
        btnConfirmPayment.setOnClickListener(v -> confirmPayment());

        // Tải thông tin người dùng
        loadUserDataFromSharedPreferences();
    }

    private void confirmPayment() {
        // Kiểm tra thông tin thanh toán
        String cardNumber = edtCardNumber.getText().toString().trim();
        String cardHolderName = edtCardHolderName.getText().toString().trim();
        String expiryDate = edtExpiryDate.getText().toString().trim();

        if (cardNumber.isEmpty() || cardHolderName.isEmpty() || expiryDate.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin thanh toán", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra định dạng ngày hết hạn (MM/YY)
        if (!expiryDate.matches("^(0[1-9]|1[0-2])/([0-9]{2})$")) {
            Toast.makeText(this, "Ngày hết hạn không hợp lệ (MM/YY)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy thông tin đặt phòng từ SharedPreferences
        SharedPreferences sharedPreferences = SharedPreferencesUtils.getBookingData(this);
        String hotelId = sharedPreferences.getString("hotel_id", null);
        String hotelName = sharedPreferences.getString("hotel_name", "N/A");
        String roomType = sharedPreferences.getString("room_type", "N/A");
        long totalAmount = sharedPreferences.getLong("total_amount", 0);
        String checkInDate = sharedPreferences.getString("check_in_date", "N/A");
        String checkOutDate = sharedPreferences.getString("check_out_date", "N/A");

        if (hotelId == null) {
            Toast.makeText(this, "Không tìm thấy thông tin đặt phòng!", Toast.LENGTH_SHORT).show();
            return;
        }



        // Lấy userId
        String userId = getUserId();
        if (userId == null) {
            return; // Đã xử lý chuyển hướng đến LoginActivity trong getUserId()
        }

        userCurrentViewModel.user.observe(this, user -> {
                    if (user != null) {
                        // Tạo bookingId và paymentId
                        String bookingId = "booking_hotel_" + System.currentTimeMillis();
                        String paymentId = "pay_" + System.currentTimeMillis();

                        // Tạo đối tượng Payment
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        String transactionDate = dateFormat.format(new Date());
                        Payment payment = new Payment(paymentId, bookingId, totalAmount, "Card", "success", transactionDate);

                        // Chuyển đổi checkInDate thành timestamp
                        long checkInTimestamp = 0;
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        try {
                            Date date = sdf.parse(checkInDate);
                            if (date != null) {
                                checkInTimestamp = date.getTime();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Lỗi khi parse checkInDate: " + e.getMessage());
                            Toast.makeText(this, "Ngày check-in không hợp lệ", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Tạo đối tượng BookingHotel
                        BookingHotel bookingHotel = new BookingHotel(
                                bookingId,
                                hotelId,
                                hotelName,
                                checkInDate,
                                checkOutDate,
                                roomType,
                                totalAmount,
                                user.getUserId(),
                                "Đã thanh toán",
                                payment,
                                checkInTimestamp
                        );

                        // Lưu BookingHotel và Payment vào Firebase
                        bookingHotelViewModel.saveBooking(bookingHotel, payment, hotelId, roomType);
                        scheduleReminder(bookingHotel);
                    }

                });


        // Thiết lập thông báo nhắc nhở

    }

    @SuppressLint({"SetTextI18n", "ScheduleExactAlarm"})
    private void displayHotelDetails() {
        // Lấy dữ liệu từ SharedPreferences
        SharedPreferences sharedPreferences = SharedPreferencesUtils.getBookingData(this);
        String hotelName = sharedPreferences.getString("hotel_name", "N/A");
        String hotelId = sharedPreferences.getString("hotel_id", "N/A");
        String roomType = sharedPreferences.getString("room_type", "N/A");
        long totalAmount = sharedPreferences.getLong("total_amount", 0);
        String checkInDate = sharedPreferences.getString("check_in_date", "N/A");
        String checkOutDate = sharedPreferences.getString("check_out_date", "N/A");

        // Hiển thị thông tin lên giao diện
        txtHotelName.setText(hotelName);
        txtRoomType.setText(roomType);
        txtCheckInDate.setText(checkInDate);
        txtCheckOutDate.setText(checkOutDate);
        txtTotalAmount.setText("Tổng: " + totalAmount + " VNĐ");
        txtBookingDate.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));
    }

    @SuppressLint("ScheduleExactAlarm")
    private void scheduleReminder(BookingHotel bookingHotel) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderBroadcastReceiver.class);
        intent.putExtra("hotel_name", bookingHotel.getHotelName());
        intent.putExtra("check_in_date", bookingHotel.getCheckInDate());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                bookingHotel.getId().hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Đặt thông báo trước 1 ngày (24 giờ trước check-in)
        long triggerTime = bookingHotel.getCheckInTimestamp() - (24 * 60 * 60 * 1000);
        if (triggerTime < System.currentTimeMillis()) {
            triggerTime = System.currentTimeMillis() + 10 * 1000; // Nếu thời gian đã qua, đặt thông báo sau 10 giây để test
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            }
            Log.d(TAG, "Đã thiết lập thông báo nhắc nhở vào: " + new Date(triggerTime));
        } catch (SecurityException e) {
            Log.e(TAG, "Lỗi khi đặt thông báo: " + e.getMessage());
            Toast.makeText(this, "Không thể đặt thông báo nhắc nhở", Toast.LENGTH_SHORT).show();
        }
    }

    private String getUserId() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "Bạn cần đăng nhập để thực hiện thanh toán", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return null;
        }

        return currentUser.getUid();
    }

    private void loadUserDataFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String customerName = sharedPreferences.getString("customer_name", "Khách hàng");
        txtCustomerName.setText(customerName);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Xử lý kết quả từ ZaloPay (bỏ comment nếu tích hợp ZaloPay)
        // ZaloPaySDK.getInstance().onResult(intent);
    }
}