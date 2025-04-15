package com.example.travel_app.UI.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Adapter.BusStationAdapter;
import com.example.travel_app.Data.Model.BusStation;
import com.example.travel_app.R;
import com.example.travel_app.ViewModel.SearchBusStationViewModel;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SearchBusStationActivity extends BaseActivity {
    private RecyclerView rcvBusStationResult;
    private BusStationAdapter busStationAdapter;
    private SearchBusStationViewModel viewModel;
    private List<BusStation> busStationList = new ArrayList<>();
    private EditText edtSearchBusStation;
    private ImageButton btnSearchBusStation;
    private ProgressBar progressBar;
    private TextView txtNoResults;
    private String searchType;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bus_station);

        edtSearchBusStation = findViewById(R.id.edt_search_bus_station);
        btnSearchBusStation = findViewById(R.id.btn_search_bus_station);
        rcvBusStationResult = findViewById(R.id.rcv_bus_station_result);
        progressBar = findViewById(R.id.progress_bar);
        txtNoResults = findViewById(R.id.txt_no_results);

        searchType = getIntent().getStringExtra("search_type");

        rcvBusStationResult.setLayoutManager(new LinearLayoutManager(this));
        busStationAdapter = new BusStationAdapter(busStationList, busStation -> returnSelectedBusStation(busStation));
        rcvBusStationResult.setAdapter(busStationAdapter);

        viewModel = new ViewModelProvider(this).get(SearchBusStationViewModel.class);

        // Tải toàn bộ danh sách bến xe ban đầu
        viewModel.getBusStations().observe(this, busStations -> {
            progressBar.setVisibility(View.GONE);
            if (busStations != null && !busStations.isEmpty()) {
                busStationList.clear();
                busStationList.addAll(busStations);
                busStationAdapter.updateData(busStationList);
                rcvBusStationResult.setVisibility(View.VISIBLE);
                txtNoResults.setVisibility(View.GONE);
            } else {
                Toast.makeText(this, "Không tìm thấy bến xe", Toast.LENGTH_SHORT).show();
                rcvBusStationResult.setVisibility(View.GONE);
                txtNoResults.setVisibility(View.VISIBLE);
            }
        });

        // Quan sát kết quả tìm kiếm
        viewModel.getSearchResults().observe(this, searchResults -> {
            progressBar.setVisibility(View.GONE);
            if (searchResults != null && !searchResults.isEmpty()) {
                busStationList.clear();
                busStationList.addAll(searchResults);
                busStationAdapter.updateData(busStationList);
                rcvBusStationResult.setVisibility(View.VISIBLE);
                txtNoResults.setVisibility(View.GONE);
            } else {
                busStationList.clear();
                busStationAdapter.updateData(busStationList);
                rcvBusStationResult.setVisibility(View.GONE);
                txtNoResults.setVisibility(View.VISIBLE);
            }
        });

        btnSearchBusStation.setOnClickListener(v -> searchBusStation());

        edtSearchBusStation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchBusStation();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void searchBusStation() {
        String query = edtSearchBusStation.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        viewModel.searchBusStations(query);
    }

    public static String removeDiacritics(String str) {
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{M}");
        return pattern.matcher(normalized).replaceAll("").toLowerCase();
    }

    private void returnSelectedBusStation(BusStation busStation) {
        Intent intent = new Intent();
        intent.putExtra("selected_bus_station", busStation);
        intent.putExtra("search_type", searchType);
        setResult(RESULT_OK, intent);
        finish();
    }
}
