package com.example.travel_app.UI.Activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Adapter.FlightAdapter;
import com.example.travel_app.Data.Model.Flight;
import com.example.travel_app.Data.Model.SearchFlightInfo;
import com.example.travel_app.R;
import com.example.travel_app.UI.Fragment.SearchFlightResultFragment;
import com.example.travel_app.ViewModel.SearchFlightViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchFlightResultActivity extends AppCompatActivity {
    private TextView txtSearchFlightInfo1, txtSearchFlightInfo2;
    private SearchFlightInfo searchFlightInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_flight_result);

        searchFlightInfo = (SearchFlightInfo) getIntent().getSerializableExtra("searchFlightInfo");

        txtSearchFlightInfo1 = findViewById(R.id.txt_search_flight_info1);
        String searchFlightInfo1 = searchFlightInfo.getDepartureCity() + " (" + searchFlightInfo.getDepartureAirportCode() + ") -> "
            + searchFlightInfo.getArrivalCity() + " (" + searchFlightInfo.getArrivalAirportCode() + ")";
        txtSearchFlightInfo1.setText(searchFlightInfo1);

        txtSearchFlightInfo2 = findViewById(R.id.txt_search_flight_info2);
        String searchFlightInfo2 = searchFlightInfo.getDepartureDate() + " - " + searchFlightInfo.getCustomerCount()
                + " người - " + searchFlightInfo.getSeatType();
        txtSearchFlightInfo2.setText(searchFlightInfo2);

        if (savedInstanceState == null) {
            SearchFlightResultFragment fragment = new SearchFlightResultFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("searchFlightInfo", searchFlightInfo);
            fragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_flight_result, fragment)  // Giả định bạn có FragmentContainerView với id fragment_container
                    .commit();
        }

    }
}
