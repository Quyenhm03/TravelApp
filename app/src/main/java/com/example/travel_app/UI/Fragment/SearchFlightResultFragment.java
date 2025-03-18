package com.example.travel_app.UI.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Adapter.FlightAdapter;
import com.example.travel_app.Data.Model.Flight;
import com.example.travel_app.Data.Model.SearchFlightInfo;
import com.example.travel_app.R;
import com.example.travel_app.ViewModel.SearchFlightViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchFlightResultFragment extends Fragment {
    private RecyclerView rcvSearchFlightResult;
    private FlightAdapter flightAdapter;
    private SearchFlightViewModel viewModel;
    private List<Flight> flightList = new ArrayList<>();
    private SearchFlightInfo searchFlightInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_flight_result, container, false);

        if (getArguments() != null) {
            searchFlightInfo = (SearchFlightInfo) getArguments().getSerializable("searchFlightInfo");
        }

        rcvSearchFlightResult = view.findViewById(R.id.rcv_flight_result);
        rcvSearchFlightResult.setLayoutManager(new LinearLayoutManager(getContext()));
        flightAdapter = new FlightAdapter(flightList);
        rcvSearchFlightResult.setAdapter(flightAdapter);

        viewModel = new ViewModelProvider(this).get(SearchFlightViewModel.class);
        viewModel.searchFlights(searchFlightInfo.getDepartureAirportCode(), searchFlightInfo.getArrivalAirportCode(),
                searchFlightInfo.getDepartureDate()).observe(getViewLifecycleOwner(), flights -> {
            if (flights != null && !flights.isEmpty()) {
                flightList.clear();
                flightList.addAll(flights);
                flightAdapter.setFlightList(flightList);
            } else {
                Toast.makeText(getContext(), "Không tìm thấy chuyến bay", Toast.LENGTH_SHORT).show();
            }
        });

        flightAdapter.setOnItemClickListener(new FlightAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(Flight flight) {
                FlightDetailFragment flightDetailFragment = new FlightDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("flight", flight);
                bundle.putSerializable("searchFlightInfo", searchFlightInfo);
                flightDetailFragment.setArguments(bundle);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_flight_result, flightDetailFragment)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onViewDetailClick(Flight flight) {
                FlightDetailFragment flightDetailFragment = new FlightDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("flight", flight);
                bundle.putSerializable("searchFlightInfo", searchFlightInfo);
                flightDetailFragment.setArguments(bundle);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_flight_result, flightDetailFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}
