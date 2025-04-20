package com.example.travel_app.UI.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Adapter.CoachHomeAdapter;
import com.example.travel_app.Adapter.FlightHomeAdapter;
import com.example.travel_app.Adapter.LocationHomeAdapter;
import com.example.travel_app.R;
import com.example.travel_app.UI.Activity.FindHotelActivity;
import com.example.travel_app.UI.Activity.Location.LocationActivity;
import com.example.travel_app.UI.Activity.SearchCoachActivity;
import com.example.travel_app.UI.Activity.SearchFlightActivity;
import com.example.travel_app.ViewModel.CoachViewModel;
import com.example.travel_app.ViewModel.FlightViewModel;
import com.example.travel_app.ViewModel.Itinerary.ImageViewModel;
import com.example.travel_app.ViewModel.Itinerary.LocationViewModel;

import java.util.ArrayList;

public class HomeFragmentQ extends Fragment {
    private CardView cvFlight, cvCoach;
    private RecyclerView rcvFlight, rcvCoach, rcvLocation;
    private FlightHomeAdapter flightAdapter;
    private CoachHomeAdapter coachAdapter;
    private LocationHomeAdapter locationAdapter;
    private FlightViewModel flightViewModel;
    private CoachViewModel coachViewModel;
    private LocationViewModel locationViewModel;
    private ImageViewModel imageViewModel;
    private LinearLayout findHotel;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        cvFlight = view.findViewById(R.id.cv_flight);
        cvCoach = view.findViewById(R.id.cv_coach);
        findHotel = view.findViewById(R.id.findHotel);
        findHotel.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), FindHotelActivity.class);
            startActivity(intent);
        });

        cvFlight.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SearchFlightActivity.class);
            startActivity(intent);
        });

        cvCoach.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SearchCoachActivity.class);
            startActivity(intent);
        });

        // Khởi tạo RecyclerView cho Flight
        rcvFlight = view.findViewById(R.id.rcv_flight);
        rcvFlight.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        flightAdapter = new FlightHomeAdapter(requireContext(), new ArrayList<>());
        rcvFlight.setAdapter(flightAdapter);

        // Khởi tạo RecyclerView cho Coach
        rcvCoach = view.findViewById(R.id.rcv_coach);
        rcvCoach.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        coachAdapter = new CoachHomeAdapter(requireContext(), new ArrayList<>());
        rcvCoach.setAdapter(coachAdapter);

        // Khởi tạo RecyclerView cho Location
        rcvLocation = view.findViewById(R.id.rcv_location);
        rcvLocation.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        // Khởi tạo ViewModel
        flightViewModel = new ViewModelProvider(this).get(FlightViewModel.class);
        coachViewModel = new ViewModelProvider(this).get(CoachViewModel.class);
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);

        // Khởi tạo LocationHomeAdapter với ImageViewModel và callback cho sự kiện click
        locationAdapter = new LocationHomeAdapter(requireContext(), getViewLifecycleOwner(), new ArrayList<>(), imageViewModel, locationId -> {
            // Khi một item được click, chuyển sang LocationActivity
            Intent intent = new Intent(requireContext(), LocationActivity.class);
            intent.putExtra("location_id", locationId);
            startActivity(intent);
        });
        rcvLocation.setAdapter(locationAdapter);

        // Quan sát dữ liệu từ ViewModel
        flightViewModel.getFlightsHome().observe(getViewLifecycleOwner(), flights -> {
            if (flights != null) {
                flightAdapter.setFlightList(flights);
            }
        });

        coachViewModel.getCoachesHome().observe(getViewLifecycleOwner(), coaches -> {
            if (coaches != null) {
                coachAdapter.setCoachList(coaches);
            }
        });

        locationViewModel.getLocationsHome().observe(getViewLifecycleOwner(), locations -> {
            if (locations != null) {
                locationAdapter.setLocationList(locations);
            }
        });

        return view;
    }
}