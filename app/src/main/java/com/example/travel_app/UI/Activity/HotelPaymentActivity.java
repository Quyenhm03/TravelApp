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
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import com.example.travel_app.Api.CreateOrder;
import com.example.travel_app.Data.Model.Bookings;
import com.example.travel_app.Data.Model.Hotel;
import com.example.travel_app.Data.Model.Payment;
import com.example.travel_app.R;
import com.example.travel_app.Receiver.ReminderBroadcastReceiver;
import com.example.travel_app.SharedPreferencesUtils;
import com.example.travel_app.UI.Login.LoginActivity;
import com.example.travel_app.ViewModel.BookingHotelViewModel;
import com.example.travel_app.ViewModel.HotelDetailViewModel;
import com.example.travel_app.ViewModel.RoomViewModel;
import com.example.travel_app.ViewModel.UserCurrentViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
// import vn.zalopay.sdk.Environment;
// import vn.zalopay.sdk.ZaloPayError;
// import vn.zalopay.sdk.ZaloPaySDK;
// import vn.zalopay.sdk.listeners.PayOrderListener;

public class HotelPaymentActivity extends AppCompatActivity {
    private TextView txtCustomerName, txtBookingDate, txtHotelName, txtCheckInDate, txtCheckOutDate, txtRoomType, txtTotalAmount;
    private EditText edtCardNumber, edtCardHolderName, edtExpiryDate;
    private Button btnConfirmPayment;
    private BookingHotelViewModel bookingHotelViewModel;
    private UserCurrentViewModel userCurrentViewModel;
    private RoomViewModel roomViewModel;
    private HotelDetailViewModel hotelDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hotel_payment);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.orange));
        }
        // Khởi tạo ZaloPay SDK (bỏ comment nếu sử dụng ZaloPay)
        // ZaloPaySDK.init(2553, Environment.SANDBOX);

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
        userCurrentViewModel = new ViewModelProvider(this).get(UserCurrentViewModel.class);
        roomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);
        bookingHotelViewModel = new ViewModelProvider(this).get(BookingHotelViewModel.class);
        hotelDetailViewModel = new ViewModelProvider(this).get(HotelDetailViewModel.class);
        bookingHotelViewModel.getSaveSuccess().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Thanh toán và lưu đặt vé thành công!", Toast.LENGTH_SHORT).show();
                // Chuyển về HomeActivity và xóa stack điều hướng
                Intent intent = new Intent(HotelPaymentActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });


        hotelDetailViewModel.getSelectedHotel().observe(this, hotel -> {
            if (hotel != null) {

            } else {
                Toast.makeText(this, "Hotel not found!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        displayHotelDetails();
        bookingHotelViewModel.getErrorMessage().observe(this, error -> {
            Toast.makeText(this, "Lỗi khi lưu đặt vé: " + error, Toast.LENGTH_SHORT).show();
        });

        // Xử lý sự kiện nút xác nhận thanh toán
        btnConfirmPayment.setOnClickListener(v -> confirmPayment());
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
        Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    @SuppressLint("SetTextI18n")
    private void displayHotelDetails() {
        SharedPreferences sharedPreferences = SharedPreferencesUtils.getBookingData(this);

        String hotelName = sharedPreferences.getString("hotel_name", "N/A");
        String roomType = sharedPreferences.getString("room_type", "N/A");
        long totalAmount = sharedPreferences.getLong("total_amount", 0);
        String checkInDate = sharedPreferences.getString("check_in_date", "N/A");
        String checkOutDate = sharedPreferences.getString("check_out_date", "N/A");

        txtBookingDate.setText(checkInDate);
        txtHotelName.setText(hotelName);
        txtRoomType.setText(roomType);
        txtCheckInDate.setText(checkInDate);
        txtCheckOutDate.setText(checkOutDate);
        txtTotalAmount.setText("Tổng: " + totalAmount + " VNĐ");
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // ZaloPaySDK.getInstance().onResult(intent); // Bỏ comment nếu tích hợp ZaloPay
    }

    private void loadUserDataFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String customerName = sharedPreferences.getString("customer_name", "Khách hàng");
        String bookingDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        txtCustomerName.setText(customerName);
        txtBookingDate.setText("Ngày đặt: " + bookingDate);
    }



}