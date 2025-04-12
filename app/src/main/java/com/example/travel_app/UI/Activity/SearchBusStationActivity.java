package com.example.travel_app.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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

public class SearchBusStationActivity extends AppCompatActivity {
    private RecyclerView rcvBusStationResult;
    private BusStationAdapter busStationAdapter;
    private SearchBusStationViewModel viewModel;
    private List<BusStation> busStationList = new ArrayList<>();
    private EditText edtSearchBusStation;
    private ImageButton btnSearchBusStation;
    private String searchType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bus_station);

        edtSearchBusStation = findViewById(R.id.edt_search_bus_station);
        btnSearchBusStation = findViewById(R.id.btn_search_bus_station);
        rcvBusStationResult = findViewById(R.id.rcv_bus_station_result);

        searchType = getIntent().getStringExtra("search_type");

        rcvBusStationResult.setLayoutManager(new LinearLayoutManager(this));
        busStationAdapter = new BusStationAdapter(busStationList, busStation -> returnSelectedBusStation(busStation));
        rcvBusStationResult.setAdapter(busStationAdapter);

        viewModel = new ViewModelProvider(this).get(SearchBusStationViewModel.class);
        viewModel.getBusStations().observe(this, busStations -> {
            if (busStations != null && !busStations.isEmpty()) {
                busStationList.clear();
                busStationList.addAll(busStations);
                busStationAdapter.updateData(busStationList);
            } else {
                Toast.makeText(this, "Không tìm thấy bến xe", Toast.LENGTH_SHORT).show();
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
        String query = removeDiacritics(edtSearchBusStation.getText().toString().trim().toLowerCase());
        List<BusStation> filteredList = new ArrayList<>();
        for (BusStation busStation : busStationList) {
            String busStationName = removeDiacritics(busStation.getName());
            String busStationCity = removeDiacritics(busStation.getCity());

            if (busStationName.contains(query) || busStationCity.contains(query)) {
                filteredList.add(busStation);
            }
        }
        busStationAdapter.updateData(filteredList);
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
