package com.example.travel_app.UI.Activity.Itinerary;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import com.example.travel_app.Data.Model.Itinerary.Itinerary;
import com.example.travel_app.R;
import com.example.travel_app.UI.Activity.BaseActivity;
import com.example.travel_app.ViewModel.Itinerary.ItineraryViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SetUpInfoActivity extends BaseActivity {
    private ItineraryViewModel viewModel;
    private EditText edtTitle, edtStartDate, edtEndDate;
    private AppCompatButton btnContinue, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_info_itinerary);

        viewModel = new ViewModelProvider(this).get(ItineraryViewModel.class);

        // Khởi tạo views
        edtTitle = findViewById(R.id.edt_title_itinerary);
        edtStartDate = findViewById(R.id.edt_start_date);
        edtEndDate = findViewById(R.id.edt_end_date);
        btnContinue = findViewById(R.id.btn_continue);
        btnBack = findViewById(R.id.btn_back);

        // Date picker
        edtStartDate.setOnClickListener(v -> showDatePicker(edtStartDate));
        edtEndDate.setOnClickListener(v -> showDatePicker(edtEndDate));

        // Nút tiếp tục
        btnContinue.setOnClickListener(v -> {
            String title = edtTitle.getText().toString().trim();
            String startDate = edtStartDate.getText().toString().trim();
            String endDate = edtEndDate.getText().toString().trim();

            if (title.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = "user123"; // Thay bằng logic thực tế
            String userName = "User A";

            // Nhận Itinerary từ setItineraryInfo và truyền qua Intent
            Itinerary itinerary = viewModel.setItineraryInfo(title, startDate, endDate, userId, userName);

            Intent intent = new Intent(this, SaveItineraryActivity.class);
            intent.putExtra("itinerary", itinerary); // Truyền Itinerary qua Intent
            startActivity(intent);
        });

        // Nút quay lại
        btnBack.setOnClickListener(v -> finish());
    }

    private void showDatePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    editText.setText(sdf.format(selectedDate.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
}