package com.example.travel_app.UI.Activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.travel_app.Data.Model.Hotel;
import com.example.travel_app.Data.Model.Room;
import com.example.travel_app.R;
import com.example.travel_app.ViewModel.HotelDetailViewModel;
import com.example.travel_app.ViewModel.RoomViewModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailBookingRoomActivity extends AppCompatActivity {

    private ImageView imageHotel;
    private TextView textHotelName, textHotelPrice;
    private EditText edtStartDate, edtEndDate;
    private ImageView imgStartDate, imgEndDate;
    private Button buttonConfirmBooking;
    private RadioGroup radioGroupRoomType;
    private RadioButton radioButtonRegular, radioButtonVIP;
    private HotelDetailViewModel hotelDetailViewModel;
    private RoomViewModel roomViewModel;
    private Room selectedRoom;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private Calendar startDate, endDate;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_room);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.orange));
        }
        // Xử lý WindowInsets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            androidx.core.graphics.Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo các view
        imageHotel = findViewById(R.id.imageHotel);
        textHotelName = findViewById(R.id.textHotelName);
        textHotelPrice = findViewById(R.id.textHotelPrice);
        edtStartDate = findViewById(R.id.edtStartDate);
        edtEndDate = findViewById(R.id.edt_birthday);
        imgStartDate = findViewById(R.id.imgStartDate);
        imgEndDate = findViewById(R.id.imgBirthDate);
        radioGroupRoomType = findViewById(R.id.radioGroupRoomType);
        radioButtonRegular = findViewById(R.id.radioButtonRegular);
        radioButtonVIP = findViewById(R.id.radioButtonVIP);
        buttonConfirmBooking = findViewById(R.id.buttonConfirmBooking);

        // Khởi tạo ViewModel
        hotelDetailViewModel = new ViewModelProvider(this).get(HotelDetailViewModel.class);
        roomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);

        // Lấy hotel_id từ Intent
        Intent intent = getIntent();
        String hotelId = intent.getStringExtra("hotel_id");

        // Lấy dữ liệu khách sạn
        hotelDetailViewModel.fetchHotelById(hotelId);
        hotelDetailViewModel.getSelectedHotel().observe(this, hotel -> {
            if (hotel != null) {
                displayHotelDetails(hotel);
            } else {
                Toast.makeText(this, "Không tìm thấy khách sạn!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Lấy danh sách phòng
        roomViewModel.fetchRoomsByHotelId(hotelId);
        roomViewModel.getRooms().observe(this, rooms -> {
            if (rooms != null && !rooms.isEmpty()) {
                setupRoomSelection(rooms);
            } else {
                Toast.makeText(this, "Khong co phong", Toast.LENGTH_SHORT).show();
                radioButtonRegular.setEnabled(false);
                radioButtonVIP.setEnabled(false);
            }
        });

        // Xử lý sự kiện chọn loại phòng
        radioGroupRoomType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonRegular) {
                selectedRoom = findRoomByType("Thường");
            } else if (checkedId == R.id.radioButtonVIP) {
                selectedRoom = findRoomByType("VIP");
            }
            updatePriceDisplay();
        });

        // Xử lý chọn ngày bắt đầu
        imgStartDate.setOnClickListener(v -> showDatePickerDialog(true));
        edtStartDate.setOnClickListener(v -> showDatePickerDialog(true));

        // Xử lý chọn ngày kết thúc
        imgEndDate.setOnClickListener(v -> showDatePickerDialog(false));
        edtEndDate.setOnClickListener(v -> showDatePickerDialog(false));

        // Xử lý nút xác nhận đặt phòng
        buttonConfirmBooking.setOnClickListener(v -> {
            if (selectedRoom == null) {
                Toast.makeText(this, "Vui lòng chọn loại phòng!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!validateDates()) {
                Toast.makeText(this, "Vui lòng chọn ngày bắt đầu và kết thúc!", Toast.LENGTH_SHORT).show();
                return;
            }
            long numberOfNights = calculateNumberOfNights();
            if (numberOfNights <= 0) {
                Toast.makeText(this, "Ngay ket thuc khong hop le!", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Xac nhan dat phong" + textHotelName.getText() + " - " + selectedRoom.getRoomType() + " cho " + numberOfNights + " ngày/đêm", Toast.LENGTH_LONG).show();
            Intent intentBooking = new Intent(this, HotelPaymentActivity.class);
            startActivity(intentBooking);
        });
    }

    private void showDatePickerDialog(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, dayOfMonth);
                    if (isStartDate) {
                        startDate = selectedDate;
                        edtStartDate.setText(dateFormat.format(selectedDate.getTime()));
                    } else {
                        endDate = selectedDate;
                        edtEndDate.setText(dateFormat.format(selectedDate.getTime()));
                    }
                    updatePriceDisplay();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private boolean validateDates() {
        return startDate != null && endDate != null;
    }

    private long calculateNumberOfNights() {
        if (startDate == null || endDate == null) return 0;
        long diffInMillies = endDate.getTimeInMillis() - startDate.getTimeInMillis();
        return diffInMillies / (1000 * 60 * 60 * 24);
    }

    private void displayHotelDetails(Hotel hotel) {
        textHotelName.setText(hotel.getName());
        if (hotel.getHotelUrl() != null && !hotel.getHotelUrl().isEmpty()) {
            Picasso.get()
                    .load(hotel.getHotelUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.img_hotel_default)
                    .into(imageHotel, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
        } else {

            imageHotel.setImageResource(R.drawable.img_hotel_default);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setupRoomSelection(List<Room> rooms) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        for (Room room : rooms) {
            if (room.getRoomType().equals("Thường")) {
                radioButtonRegular.setText("Phòng thường (Còn: " + room.getAvailability() + ", Giá: " + numberFormat.format(room.getPrice()) + " VNĐ/ngày/đêm)");
                radioButtonRegular.setEnabled(room.getAvailability() > 0);
            } else if (room.getRoomType().equals("VIP")) {
                radioButtonVIP.setText("Phòng VIP (Còn: " + room.getAvailability() + ", Giá: " + numberFormat.format(room.getPrice()) + " VNĐ/ngày/đêm)");
                radioButtonVIP.setEnabled(room.getAvailability() > 0);
            }
        }

        // Chọn mặc định phòng Thường nếu có, nếu không thì chọn VIP
        selectedRoom = findRoomByType("Thường");
        if (selectedRoom != null && selectedRoom.getAvailability() > 0) {
            radioButtonRegular.setChecked(true);
        } else {
            selectedRoom = findRoomByType("VIP");
            if (selectedRoom != null && selectedRoom.getAvailability() > 0) {
                radioButtonVIP.setChecked(true);
            }
        }

        updatePriceDisplay();
    }

    private Room findRoomByType(String roomType) {
        List<Room> rooms = roomViewModel.getRooms().getValue();
        if (rooms != null) {
            for (Room room : rooms) {
                if (room.getRoomType().equals(roomType)) {
                    return room;
                }
            }
        }
        return null;
    }

    @SuppressLint("SetTextI18n")
    private void updatePriceDisplay() {
        if (selectedRoom == null) {
            textHotelPrice.setText("Giá: N/A");
            return;
        }

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        long numberOfNights = calculateNumberOfNights();
        if (numberOfNights <= 0) {
            textHotelPrice.setText("Giá: " + numberFormat.format(selectedRoom.getPrice()) + " VNĐ/ngày/đêm");
        } else {
            long totalPrice = selectedRoom.getPrice() * numberOfNights;
            textHotelPrice.setText("Tổng giá: " + numberFormat.format(totalPrice) + " VNĐ (" + numberOfNights + " Ngày/đêm)");
        }
    }
}