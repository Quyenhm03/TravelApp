package com.example.travel_app.UI.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.travel_app.Data.Model.Coach;
import com.example.travel_app.Data.Model.SearchCoachInfo;
import com.example.travel_app.Data.Model.SelectedCoach;
import com.example.travel_app.R;
import com.example.travel_app.UI.Activity.AddPassengerInfoCoachActivity;
import com.example.travel_app.UI.Activity.SearchCoachResultActivity;

public class CoachDetailFragment extends Fragment {

    private TextView txtCoachName, txtDepartureTime, txtArrivalTime, txtDepartureStation, txtArrivalStation;
    private TextView txtDepartureDate, txtTravelTime, txtPrice;
    private AppCompatButton btnBookCoach, btnCancelCoach;
    private SearchCoachInfo searchCoachInfo;
    private Coach coach;
    private boolean isReturnCoach;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coach_detail, container, false);

        txtCoachName = view.findViewById(R.id.txt_company_name);
        txtDepartureTime = view.findViewById(R.id.txt_departure_time);
        txtArrivalTime = view.findViewById(R.id.txt_arrival_time);
        txtDepartureStation = view.findViewById(R.id.txt_departure_station_name);
        txtArrivalStation = view.findViewById(R.id.txt_arrival_station_name);
        txtDepartureDate = view.findViewById(R.id.txt_departure_date);
        txtTravelTime = view.findViewById(R.id.txt_travel_time);
        txtPrice = view.findViewById(R.id.txt_price);
        btnBookCoach = view.findViewById(R.id.btn_book_coach);
        btnCancelCoach = view.findViewById(R.id.btn_cancel_coach);

        if (getArguments() != null) {
            coach = (Coach) getArguments().getSerializable("coach");
            searchCoachInfo = (SearchCoachInfo) getArguments().getSerializable("searchCoachInfo");
            isReturnCoach = getArguments().getBoolean("isReturnCoach", false);

            if (coach != null && searchCoachInfo != null) {
                txtCoachName.setText(coach.getCompanyName());
                txtDepartureTime.setText(coach.getDepartureTime());
                txtArrivalTime.setText(coach.getArrivalTime());
                txtDepartureStation.setText(isReturnCoach ? searchCoachInfo.getArrivalStationName() : searchCoachInfo.getDepartureStationName());
                txtArrivalStation.setText(isReturnCoach ? searchCoachInfo.getDepartureStationName() : searchCoachInfo.getArrivalStationName());
                txtDepartureDate.setText(isReturnCoach ? searchCoachInfo.getReturnDate() : searchCoachInfo.getDepartureDate());
                txtTravelTime.setText(coach.getTravelTime());

                String formattedPrice = String.format("%,.0f VNĐ", coach.getPrice());
                txtPrice.setText(formattedPrice);
            }
        }

        btnBookCoach.setOnClickListener(v -> {
            SelectedCoach selectedCoach;
            if (getActivity().getIntent().hasExtra("selectedCoach")) {
                selectedCoach = (SelectedCoach) getActivity().getIntent().getSerializableExtra("selectedCoach");
            } else {
                selectedCoach = new SelectedCoach();
            }

            if (!isReturnCoach) {
                selectedCoach.setDepartureCoach(coach);

                if (searchCoachInfo.getReturnDate() != null) {
                    Intent intent = new Intent(requireActivity(), SearchCoachResultActivity.class);
                    intent.putExtra("searchCoachInfo", searchCoachInfo);
                    intent.putExtra("selectedCoach", selectedCoach);
                    intent.putExtra("isReturnCoach", true);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(requireActivity(), AddPassengerInfoCoachActivity.class);
                    intent.putExtra("searchCoachInfo", searchCoachInfo);
                    intent.putExtra("selectedCoach", selectedCoach);
                    startActivity(intent);
                }
            } else {
                selectedCoach.setReturnCoach(coach);
                Intent intent = new Intent(requireActivity(), AddPassengerInfoCoachActivity.class);
                intent.putExtra("searchCoachInfo", searchCoachInfo);
                intent.putExtra("selectedCoach", selectedCoach);
                startActivity(intent);
            }
        });

        btnCancelCoach.setOnClickListener(v -> requireActivity().onBackPressed());
        return view;
    }
}
