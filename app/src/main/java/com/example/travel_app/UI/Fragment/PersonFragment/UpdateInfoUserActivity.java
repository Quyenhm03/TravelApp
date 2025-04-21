package com.example.travel_app.UI.Fragment.PersonFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.travel_app.R;
import com.example.travel_app.ViewModel.UserCurrentViewModel;
import com.example.travel_app.utils.Utils;

import java.util.Calendar;
import java.util.Date;

public class UpdateInfoUserActivity extends AppCompatActivity {
    private static final String TAG = "UpdateInfoUserActivity";
    private EditText edtFullName, edtBirthday, edtEmail, edtAddress, edtPhone;
    private ImageView imgBirthDate;
    private Button btnSave, btnBack;
    private UserCurrentViewModel userCurrentViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_info_user);
        userCurrentViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(UserCurrentViewModel.class);
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

        if (fullNameUpdate.isEmpty() || birthdayUpdate.isEmpty()
                || emailUpdate.isEmpty() || addressUpdate.isEmpty() || phoneUpdate.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        userCurrentViewModel.updateInfoUser(fullNameUpdate, birthdayUpdate, emailUpdate, addressUpdate, phoneUpdate);
        Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void getCurrentUser() {


        userCurrentViewModel.user.observe(this, user -> {
            if (user == null) {
                Toast.makeText(this, "User is null", Toast.LENGTH_SHORT).show();
                return;
            }
            if (user.getFullName() != null) {
                edtFullName.setText(user.getFullName());
            }
            if (user.getPhone() != null) {
                String phone = user.getPhone();
                edtPhone.setText(phone);
            }
            if (user.getEmail() != null) {
                String email = user.getEmail();
                edtEmail.setText(email);
            }
            if (user.getAddress() != null) {
                String address = user.getAddress();
                edtAddress.setText(address);
            }
            if (user.getDateOfBirth() != null) {
                String birthday = Utils.dateToString(user.getDateOfBirth(), null);
                edtBirthday.setText(birthday);
            }
        });
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
