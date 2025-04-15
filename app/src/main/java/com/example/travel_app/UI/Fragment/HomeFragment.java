package com.example.travel_app.UI.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Adapter.CoachHomeAdapter;
import com.example.travel_app.Adapter.FlightHomeAdapter;
import com.example.travel_app.R;
import com.example.travel_app.UI.Activity.SearchCoachActivity;
import com.example.travel_app.UI.Activity.SearchFlightActivity;
import com.example.travel_app.ViewModel.CoachViewModel;
import com.example.travel_app.ViewModel.FlightViewModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private CardView cvFlight, cvCoach;
    private RecyclerView rcvFlight, rcvCoach;
    private FlightHomeAdapter flightAdapter;
    private CoachHomeAdapter coachAdapter;
    private FlightViewModel flightViewModel;
    private CoachViewModel coachViewModel;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        cvFlight = view.findViewById(R.id.cv_flight);
        cvCoach = view.findViewById(R.id.cv_coach);

        cvFlight.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SearchFlightActivity.class);
            startActivity(intent);
        });

        cvCoach.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SearchCoachActivity.class);
            startActivity(intent);
        });

        rcvFlight = view.findViewById(R.id.rcv_flight);
        rcvFlight.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        flightAdapter = new FlightHomeAdapter(requireContext(), new ArrayList<>());
        rcvFlight.setAdapter(flightAdapter);

        rcvCoach = view.findViewById(R.id.rcv_coach);
        rcvCoach.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        coachAdapter = new CoachHomeAdapter(requireContext(), new ArrayList<>());
        rcvCoach.setAdapter(coachAdapter);

        flightViewModel = new ViewModelProvider(this).get(FlightViewModel.class);
        coachViewModel = new ViewModelProvider(this).get(CoachViewModel.class);

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

        return view;
    }
}
