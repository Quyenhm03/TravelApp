package com.example.travel_app.UI.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.travel_app.R;
import com.example.travel_app.UI.Activity.SearchFlightActivity;

public class HomeFragment extends Fragment {
    private CardView cvFlight;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        cvFlight = view.findViewById(R.id.cv_flight);

        cvFlight.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SearchFlightActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
