package com.example.travel_app.UI.Activity;

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

import com.example.travel_app.Adapter.SearchItemAdapter;
import com.example.travel_app.R;
import com.example.travel_app.ViewModel.ItineraryViewModel;

import java.util.ArrayList;

public class SearchItemActivity extends AppCompatActivity {
    private ItineraryViewModel viewModel;
    private RecyclerView rcvSearchItemResult;
    private SearchItemAdapter adapter;
    private EditText edtSearchItem;
    private AppCompatButton btnBack;
    private AppCompatImageButton btnSearchItem;
    private TextView txtTimeItinerary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);

        viewModel = new ViewModelProvider(this).get(ItineraryViewModel.class);

        rcvSearchItemResult = findViewById(R.id.rcv_search_item_result);
        edtSearchItem = findViewById(R.id.edt_search_item);
        btnBack = findViewById(R.id.btn_back);
        btnSearchItem = findViewById(R.id.btn_search_item);
        txtTimeItinerary = findViewById(R.id.txt_time_itinerary);

        // Nhận ngày được chọn từ Intent
        String selectedDate = getIntent().getStringExtra("selectedDate");
        if (selectedDate != null) {
            txtTimeItinerary.setText(selectedDate);
        } else {
            txtTimeItinerary.setText("Chưa chọn ngày");
        }

        rcvSearchItemResult.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchItemAdapter(new ArrayList<>(), item -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedItem", item);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
        rcvSearchItemResult.setAdapter(adapter);

        viewModel.getSearchResultsLiveData().observe(this, items -> {
            if (items != null) {
                adapter.updateItems(items);
            }
        });

        edtSearchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    viewModel.searchItems(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnSearchItem.setOnClickListener(v -> {
            String query = edtSearchItem.getText().toString().trim();
            if (!query.isEmpty()) {
                viewModel.searchItems(query);
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }
}