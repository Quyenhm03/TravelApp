package com.example.travel_app.UI.Fragment.PersonFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travel_app.R;
import com.example.travel_app.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Date;

public class UpdateInfoUserActivity extends AppCompatActivity {
    private static final String TAG = "UpdateInfoUserActivity";
    private EditText edtFullName, edtBirthday, edtEmail, edtAddress, edtPhone;
    private ImageView imgBirthDate;
    private Button btnSave, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_info_user);
        initViews();
        getCurrentUser();

    }

    private void initViews() {
        edtFullName = findViewById(R.id.edt_full_name);
        edtBirthday = findViewById(R.id.edt_birthday);
        edtEmail = findViewById(R.id.edtEmail);
        edtAddress = findViewById(R.id.edt_address);
        edtPhone = findViewById(R.id.edt_phone);
        btnSave = findViewById(R.id.btn_save_customer_info);
        imgBirthDate = findViewById(R.id.imgBirthDate);
        imgBirthDate.setOnClickListener(v -> showDatePickerDialog());
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
        btnSave.setOnClickListener(v -> {
            updateInfoUser();
        });
    }

    private void updateInfoUser() {
        String fullNameUpdate = edtFullName.getText().toString().trim();
        String birthdayUpdate = edtBirthday.getText().toString().trim();
        String emailUpdate = edtEmail.getText().toString().trim();
        String addressUpdate = edtAddress.getText().toString().trim();
        String phoneUpdate = edtPhone.getText().toString().trim();
    }

    private void getCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User is null", Toast.LENGTH_SHORT).show();
            return;
        }
        if (user.getDisplayName() != null) {
            edtFullName.setText(user.getDisplayName());
        }
        if (user.getPhoneNumber() != null) {
            String phone = user.getPhoneNumber();
            edtPhone.setText(phone);
        }
        if (user.getEmail() != null) {
            String email = user.getEmail();
            edtEmail.setText(email);
        }

    }

    private void showDatePickerDialog() {
        // Lấy ngày hiện tại làm mặc định
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Khi người dùng chọn ngày
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);
                    Date date = selectedDate.getTime();

                    // Chuyển Date sang String và hiển thị lên EditText
                    String dateString = Utils.dateToString(date, "dd/MM/yyyy");
                    edtBirthday.setText(dateString);
                },
                year, month, day
        );

        // (Tùy chọn) Giới hạn ngày tối đa là ngày hiện tại
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        // Hiển thị dialog
        datePickerDialog.show();
    }
}