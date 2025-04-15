package com.example.travel_app.UI.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Adapter.AirportAdapter;
import com.example.travel_app.Data.Model.Airport;
import com.example.travel_app.R;
import com.example.travel_app.UI.Fragment.OneWayFlightFragment;
import com.example.travel_app.ViewModel.SearchAirportViewModel;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SearchAirportActivity extends BaseActivity {
    private RecyclerView rcvAirportResult;
    private AirportAdapter airportAdapter;
    private SearchAirportViewModel viewModel;
    private List<Airport> airportList = new ArrayList<>();
    private EditText edtSearchAirport;
    private ImageButton btnSearchAirport;
    private ProgressBar progressBar;
    private String searchType;
    private TextView txtNoResults;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_airport);

        edtSearchAirport = findViewById(R.id.edt_search_airport);
        btnSearchAirport = findViewById(R.id.btn_search_airport);
        rcvAirportResult = findViewById(R.id.rcv_airport_result);
        progressBar = findViewById(R.id.progress_bar);
        txtNoResults = findViewById(R.id.txt_no_results);

        searchType = getIntent().getStringExtra("search_type");

        rcvAirportResult.setLayoutManager(new LinearLayoutManager(this));
        airportAdapter = new AirportAdapter(airportList, airport -> returnSelectedAirport(airport));
        rcvAirportResult.setAdapter(airportAdapter);

        viewModel = new ViewModelProvider(this).get(SearchAirportViewModel.class);

        // Tải toàn bộ danh sách sân bay ban đầu
        viewModel.getAirports().observe(this, airports -> {
            progressBar.setVisibility(View.GONE);
            if (airports != null && !airports.isEmpty()) {
                airportList.clear();
                airportList.addAll(airports);
                airportAdapter.updateData(airportList);
            } else {
                Toast.makeText(this, "Không tìm thấy sân bay", Toast.LENGTH_SHORT).show();
                rcvAirportResult.setVisibility(View.GONE);
                txtNoResults.setVisibility(View.VISIBLE);
            }
        });

        // Quan sát kết quả tìm kiếm
        viewModel.getSearchResults().observe(this, searchResults -> {
            progressBar.setVisibility(View.GONE);
            if (searchResults != null && !searchResults.isEmpty()) {
                airportList.clear();
                airportList.addAll(searchResults);
                airportAdapter.updateData(airportList);
                rcvAirportResult.setVisibility(View.VISIBLE);
                txtNoResults.setVisibility(View.GONE);
            } else {
                airportList.clear();
                airportAdapter.updateData(airportList);
                rcvAirportResult.setVisibility(View.GONE);
                txtNoResults.setVisibility(View.VISIBLE);
            }
        });

        btnSearchAirport.setOnClickListener(v -> searchAirport());

        edtSearchAirport.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchAirport();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void searchAirport() {
        String query = edtSearchAirport.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        viewModel.searchAirports(query);
    }

    public static String removeDiacritics(String str) {
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{M}");
        return pattern.matcher(normalized).replaceAll("").toLowerCase();
    }

    private void returnSelectedAirport(Airport airport) {
        Intent intent = new Intent();
        intent.putExtra("selected_airport", airport);
        intent.putExtra("search_type", searchType);
        setResult(RESULT_OK, intent);
        finish();
    }
}