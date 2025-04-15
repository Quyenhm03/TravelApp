package com.example.travel_app.UI.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import com.example.travel_app.Data.Model.BookingCoach;
import com.example.travel_app.Data.Model.Passenger;
import com.example.travel_app.Data.Model.SearchCoachInfo;
import com.example.travel_app.Data.Model.SelectedCoach;
import com.example.travel_app.R;

import java.util.ArrayList;
import java.util.List;

public class AddPassengerInfoCoachActivity extends BaseActivity {
    private SearchCoachInfo searchCoachInfo;
    private SelectedCoach selectedCoach;
    private List<Passenger> passengerList = new ArrayList<>();
    private TextView txtDepartureItinerary, txtDepartureCoachInfo1, txtDepartureCoachInfo2,
            txtReturnItinerary, txtReturnCoachInfo1, txtReturnCoachInfo2, txtTitle;
    private Button btnConfirm;
    private BookingCoach bookingCoach = new BookingCoach();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_passenger_info_coach);

        // Lấy dữ liệu từ Intent
        searchCoachInfo = (SearchCoachInfo) getIntent().getSerializableExtra("searchCoachInfo");
        selectedCoach = (SelectedCoach) getIntent().getSerializableExtra("selectedCoach");

        // Khởi tạo đối tượng BookingCoach
        bookingCoach.setDepartureCoach(selectedCoach.getDepartureCoach());
        bookingCoach.setDepartureCity(searchCoachInfo.getDepartureCity());
        bookingCoach.setArrivalCity(searchCoachInfo.getArrivalCity());

        // Ánh xạ các view
        txtDepartureItinerary = findViewById(R.id.txt_departure_itinerary);
        txtDepartureCoachInfo1 = findViewById(R.id.txt_departure_coach_info1);
        txtDepartureCoachInfo2 = findViewById(R.id.txt_departure_coach_info2);

        // Hiển thị thông tin chuyến đi
        txtDepartureItinerary.setText(searchCoachInfo.getDepartureCity() + " đến " + searchCoachInfo.getArrivalCity());
        String departureCoachInfo1 = searchCoachInfo.getDepartureCity() + " -> " + searchCoachInfo.getArrivalCity()
                + " | " + searchCoachInfo.getDepartureDate();
        txtDepartureCoachInfo1.setText(departureCoachInfo1);

        String departureCoachInfo2 = selectedCoach.getDepartureCoach().getDepartureTime() + " - " + selectedCoach.getDepartureCoach().getArrivalTime()
                + " | " + selectedCoach.getDepartureCoach().getTravelTime();
        txtDepartureCoachInfo2.setText(departureCoachInfo2);

        // Kiểm tra nếu có chuyến về
        if (selectedCoach.getReturnCoach() != null) {
            LinearLayout lnReturnCoach = findViewById(R.id.ln_return_coach);
            lnReturnCoach.setVisibility(View.VISIBLE);

            bookingCoach.setReturnCoach(selectedCoach.getReturnCoach());

            txtReturnItinerary = findViewById(R.id.txt_return_itinerary);
            txtReturnCoachInfo1 = findViewById(R.id.txt_return_coach_info1);
            txtReturnCoachInfo2 = findViewById(R.id.txt_return_coach_info2);
            txtTitle = findViewById(R.id.txt_title_add_customer);

            // Điều chỉnh margin top của tiêu đề "Hành khách" nếu có chuyến về
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) txtTitle.getLayoutParams();
            int marginTopPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 175, getResources().getDisplayMetrics());
            params.setMargins(0, marginTopPx, 0, 0);
            txtTitle.setLayoutParams(params);
            txtTitle.requestLayout();

            // Hiển thị thông tin chuyến về
            txtReturnItinerary.setText(searchCoachInfo.getArrivalCity() + " đến " + searchCoachInfo.getDepartureCity());
            String returnCoachInfo1 = searchCoachInfo.getArrivalCity() + " -> " + searchCoachInfo.getDepartureCity()
                    + " | " + searchCoachInfo.getReturnDate();
            txtReturnCoachInfo1.setText(returnCoachInfo1);

            String returnCoachInfo2 = selectedCoach.getReturnCoach().getDepartureTime() + " - " + selectedCoach.getReturnCoach().getArrivalTime()
                    + " | " + selectedCoach.getReturnCoach().getTravelTime();
            txtReturnCoachInfo2.setText(returnCoachInfo2);
        }

        // Khởi tạo danh sách hành khách và thêm các card nhập thông tin
        LinearLayout containerCustomerInfo = findViewById(R.id.container_customer_info);

        initializeCustomerList();

        int currentIndex = 0;
        currentIndex = addPassengerInfoCards(containerCustomerInfo, "Hành khách ", currentIndex, searchCoachInfo.getSeatCount());

        // Xử lý nút "Xác nhận"
        btnConfirm = findViewById(R.id.btn_confirm_add_customer_info);
        btnConfirm.setOnClickListener(v -> {
            if (validateAllPassengers()) {
                Intent intent = new Intent(AddPassengerInfoCoachActivity.this, SelectSeatCoachActivity.class);
                bookingCoach.setPassengerList(passengerList);
                intent.putExtra("bookingCoach", bookingCoach);
                intent.putExtra("searchCoachInfo", searchCoachInfo);
                startActivity(intent);
            }
        });

        // Xử lý nút Back
        AppCompatButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
    }

    private void initializeCustomerList() {
        int totalCustomers = searchCoachInfo.getSeatCount();
        for (int i = 0; i < totalCustomers; i++) {
            passengerList.add(new Passenger());
        }
    }

    private int addPassengerInfoCards(LinearLayout container, String type, int startIndex, int count) {
        for (int i = 0; i < count; i++) {
            CardView cardView = new CardView(this);
            cardView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            cardView.setCardElevation(8);
            cardView.setRadius(12);
            cardView.setPreventCornerOverlap(true);
            cardView.setUseCompatPadding(true);
            cardView.setContentPadding(20, 10, 20, 10);

            LinearLayout layout = new LinearLayout(this);
            layout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setGravity(Gravity.CENTER_VERTICAL);

            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            textView.setText(type + " " + String.valueOf(i + 1));
            textView.setTextSize(16);

            ImageButton addButton = new ImageButton(this);
            addButton.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
            addButton.setImageResource(R.drawable.add_itinerary);
            addButton.setBackgroundColor(Color.TRANSPARENT);

            layout.addView(textView);
            layout.addView(addButton);
            cardView.addView(layout);
            container.addView(cardView);

            int currentIndex = startIndex + i;

            cardView.setOnClickListener(v -> {
                Passenger currentPassenger = passengerList.get(currentIndex);
                Intent intent = new Intent(AddPassengerInfoCoachActivity.this, PassengerInfoActivity.class);
                intent.putExtra("passenger", currentPassenger);
                intent.putExtra("passengerIndex", currentIndex);
                startActivityForResult(intent, 1);
            });
        }
        return startIndex + count;
    }

    private boolean validateAllPassengers() {
        for (int i = 0; i < passengerList.size(); i++) {
            Passenger passenger = passengerList.get(i);
            if (!isPassengerInfoComplete(passenger)) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin cho hành khách " + (i + 1), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private boolean isPassengerInfoComplete(Passenger passenger) {
        return passenger.getFullName() != null && !passenger.getFullName().isEmpty() &&
                passenger.getPhone() != null && !passenger.getPhone().isEmpty() &&
                passenger.getDateOfBirth() != null && !passenger.getDateOfBirth().isEmpty() &&
                passenger.getNationality() != null && !passenger.getNationality().isEmpty() &&
                passenger.getGender() != null && !passenger.getGender().isEmpty() &&
                passenger.getAddress() != null && !passenger.getAddress().isEmpty();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Passenger updatedPassenger = (Passenger) data.getSerializableExtra("updatedPassenger");
            int customerIndex = data.getIntExtra("passengerIndex", -1);
            if (updatedPassenger != null && customerIndex != -1) {
                passengerList.set(customerIndex, updatedPassenger);
            }
        }
    }
}