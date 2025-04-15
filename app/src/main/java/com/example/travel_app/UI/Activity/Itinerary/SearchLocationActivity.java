package com.example.travel_app.UI.Activity.Itinerary;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Adapter.Itinerary.SearchLocationAdapter;
import com.example.travel_app.R;
import com.example.travel_app.UI.Activity.BaseActivity;
import com.example.travel_app.ViewModel.Itinerary.ImageViewModel;
import com.example.travel_app.ViewModel.Itinerary.ItineraryViewModel;

import java.util.ArrayList;

public class SearchLocationActivity extends BaseActivity {
    private ItineraryViewModel viewModel;
    private ImageViewModel imageViewModel;
    private RecyclerView rcvSearchLocationResult;
    private SearchLocationAdapter adapter;
    private EditText edtSearchItem;
    private AppCompatButton btnBack;
    private AppCompatImageButton btnSearchItem;
    private TextView txtTimeItinerary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);

        viewModel = new ViewModelProvider(this).get(ItineraryViewModel.class);
        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);

        rcvSearchLocationResult = findViewById(R.id.rcv_search_item_result);
        edtSearchItem = findViewById(R.id.edt_search_item);
        btnBack = findViewById(R.id.btn_back);
        btnSearchItem = findViewById(R.id.btn_search_item);
        txtTimeItinerary = findViewById(R.id.txt_time_itinerary);

        String selectedDate = getIntent().getStringExtra("selectedDate");
        if (selectedDate != null) {
            txtTimeItinerary.setText(selectedDate);
        } else {
            txtTimeItinerary.setText("Chưa chọn ngày");
        }

        rcvSearchLocationResult.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchLocationAdapter(new ArrayList<>(), imageViewModel, location -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedLocation", location);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
        rcvSearchLocationResult.setAdapter(adapter);

        // Quan sát kết quả tìm kiếm
        viewModel.getSearchResultsLiveData().observe(this, locations -> {
            if (locations != null) {
                Log.d("SearchLocationActivity", "Cập nhật adapter với " + locations.size() + " địa điểm");
                adapter.updateLocations(locations);
            } else {
                adapter.updateLocations(new ArrayList<>());
            }
        });

        // Lấy tất cả Location ngay khi mở activity
        viewModel.getAllLocations();

        // Xử lý tìm kiếm khi nhập
        edtSearchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    viewModel.searchLocations(s.toString());
                } else {
                    // Nếu ô tìm kiếm rỗng, hiển thị lại tất cả Location
                    viewModel.getAllLocations();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Xử lý nút tìm kiếm
        btnSearchItem.setOnClickListener(v -> {
            String query = edtSearchItem.getText().toString().trim();
            if (!query.isEmpty()) {
                viewModel.searchLocations(query);
            } else {
                viewModel.getAllLocations();
            }
        });

        // Xử lý nút quay lại
        btnBack.setOnClickListener(v -> finish());
    }
}