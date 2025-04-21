package com.example.travel_app.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.travel_app.Data.Model.Passenger;
import com.example.travel_app.Data.Model.User;
import com.example.travel_app.R;
import com.example.travel_app.ViewModel.UserCurrentViewModel;
import com.example.travel_app.utils.Utils;

public class BookingRoomInfoPassenger extends AppCompatActivity {
    private EditText edtFullName, edtAddress, edtPhone, edtBirthday;
    private TextView txtNationality, txtGender;
    private int customerIndex;
    private ImageButton btnNationality, btnGender;
    private Button btnSave, btnBack;
    private UserCurrentViewModel userCurrentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_room_info_passenger);
        initViews();
    }

    private void initViews() {
        edtFullName = findViewById(R.id.edt_full_name);
        edtAddress = findViewById(R.id.edt_address);
        edtPhone = findViewById(R.id.edt_phone);
        edtBirthday = findViewById(R.id.edt_birthday);
        txtNationality = findViewById(R.id.txt_nationality);
        txtGender = findViewById(R.id.txt_gender);
        btnNationality = findViewById(R.id.btn_nationality);
        btnGender = findViewById(R.id.btn_gender);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> onBackPressed());
        btnSave = findViewById(R.id.btn_save_customer_info);
        userCurrentViewModel = new ViewModelProvider(this).get(UserCurrentViewModel.class);
        userCurrentViewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                edtFullName.setText(user.getFullName());
                edtAddress.setText(user.getAddress());
                edtPhone.setText(user.getPhone());
                edtBirthday.setText(Utils.dateToString(user.getDateOfBirth(), null));

            }
        });
        btnNationality.setOnClickListener(this::showNationalityMenu);
        btnGender.setOnClickListener(this::showGenderMenu);

        Button btnSave = findViewById(R.id.btn_save_customer_info);
        btnSave.setOnClickListener(v -> {
            if (validateUserInfo()) {
                Toast.makeText(this, "Thông tin đã được lưu", Toast.LENGTH_SHORT).show();
            }
        });

        AppCompatButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
    }

    private boolean validateUserInfo() {
        String fullName = edtFullName.getText().toString().trim();
        if (fullName.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Họ và Tên", Toast.LENGTH_SHORT).show();
            return false;
        }

        String phone = edtPhone.getText().toString().trim();
        if (phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Số điện thoại", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!phone.matches("\\d{10}")) { // Kiểm tra số điện thoại phải là 10 chữ số
            Toast.makeText(this, "Số điện thoại phải có 10 chữ số", Toast.LENGTH_SHORT).show();
            return false;
        }

        String dateOfBirth = edtBirthday.getText().toString().trim();
        if (dateOfBirth.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Ngày sinh", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!dateOfBirth.matches("\\d{2}/\\d{2}/\\d{4}")) {
            Toast.makeText(this, "Ngày sinh phải có định dạng dd/MM/yyyy", Toast.LENGTH_SHORT).show();
            return false;
        }

        String nationality = txtNationality.getText().toString().trim();
        if (nationality.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn Quốc tịch", Toast.LENGTH_SHORT).show();
            return false;
        }

        String gender = txtGender.getText().toString().trim();
        if (gender.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn Giới tính", Toast.LENGTH_SHORT).show();
            return false;
        }

        String address = edtAddress.getText().toString().trim();
        if (address.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Địa chỉ", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveCustomerInfo(User user) {
        user.setFullName(edtFullName.getText().toString().trim());
        user.setAddress(edtAddress.getText().toString().trim());
        user.setPhone(edtPhone.getText().toString().trim());
        user.setDateOfBirth(Utils.stringToDate(edtBirthday.getText().toString(), null));
        Intent intent = new Intent();
    }

    private void showNationalityMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);

        String[] countries = {
                "Vietnam", "Singapore", "China", "Korea", "Japan",
                "United States", "Canada", "United Kingdom", "France", "Germany",
                "Italy", "Spain", "Australia", "India", "Thailand",
                "Malaysia", "Indonesia", "Philippines", "Russia", "Brazil"
        };

        for (int i = 0; i < countries.length; i++) {
            popupMenu.getMenu().add(0, i + 1, i, countries[i]);
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            txtNationality.setText(countries[item.getItemId() - 1]);
            return true;
        });

        popupMenu.show();
    }

    private void showGenderMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);

        String[] genders = {
                "Nam", "Nữ"
        };

        for (int i = 0; i < genders.length; i++) {
            popupMenu.getMenu().add(0, i + 1, i, genders[i]);
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            txtGender.setText(genders[item.getItemId() - 1]);
            return true;
        });

        popupMenu.show();
    }

}