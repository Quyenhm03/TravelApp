package com.example.travel_app.UI.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.travel_app.Data.Model.BusStation;
import com.example.travel_app.R;
import com.example.travel_app.UI.Activity.SearchBusStationActivity;

import java.util.Calendar;

public class RoundTripCoachFragment extends Fragment {
    private static final int REQUEST_CODE_BUS_STATION = 1000;

    private CardView cvDepartureStation, cvArrivalStation, cvDepartureDate, cvReturnDate;
    private TextView txtDepartureDate, txtReturnDate, txtDepartureStationCity, txtDepartureStationName,
            txtArrivalStationCity, txtArrivalStationName;

    private String currentSelectionType;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_round_trip_coach, container, false);

        cvDepartureStation = view.findViewById(R.id.cv_departure_station);
        cvArrivalStation = view.findViewById(R.id.cv_arrival_station);
        cvDepartureDate = view.findViewById(R.id.cv_departure_date);
        cvReturnDate = view.findViewById(R.id.cv_return_date);
        txtDepartureDate = view.findViewById(R.id.departure_date);
        txtReturnDate = view.findViewById(R.id.return_date);
        txtDepartureStationCity = view.findViewById(R.id.departure_destination);
        txtDepartureStationName = view.findViewById(R.id.departure_station_name);
        txtArrivalStationCity = view.findViewById(R.id.arrival_destination);
        txtArrivalStationName = view.findViewById(R.id.arrival_station_name);

        cvDepartureStation.setOnClickListener(v -> openSearchActivity("departure"));
        cvArrivalStation.setOnClickListener(v -> openSearchActivity("arrival"));
        cvDepartureDate.setOnClickListener(v -> showDatePickerDialog(txtDepartureDate));
        cvReturnDate.setOnClickListener(v -> showDatePickerDialog(txtReturnDate));

        return view;
    }

    private void openSearchActivity(String type) {
        currentSelectionType = type;
        Intent intent = new Intent(getActivity(), SearchBusStationActivity.class);
        intent.putExtra("search_type", type);
        startActivityForResult(intent, REQUEST_CODE_BUS_STATION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BUS_STATION && resultCode == getActivity().RESULT_OK && data != null) {
            BusStation busStation = (BusStation) data.getSerializableExtra("selected_bus_station");
            if (busStation != null) {
                updateBusStationInfo(busStation);
            }
        }
    }

    private void updateBusStationInfo(BusStation busStation) {
        if ("departure".equals(currentSelectionType)) {
            txtDepartureStationCity.setText(busStation.getCity());
            txtDepartureStationName.setText(busStation.getName());
        } else if ("arrival".equals(currentSelectionType)) {
            txtArrivalStationCity.setText(busStation.getCity());
            txtArrivalStationName.setText(busStation.getName());
        }
    }

    private void showDatePickerDialog(TextView txt) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = String.format("%02d/%02d/%d", dayOfMonth, month1 + 1, year1);
                    txt.setText(selectedDate);
                },
                year, month, date
        );

        datePickerDialog.show();
    }

    public String getDepartureDate() {
        return txtDepartureDate.getText().toString();
    }

    public String getReturnDate() {
        return txtReturnDate.getText().toString();
    }

    public String getArrivalCity() {
        return txtArrivalStationCity.getText().toString();
    }

    public String getDepartureCity() {
        return txtDepartureStationCity.getText().toString();
    }

    public String getArrivalStationName() {
        return txtArrivalStationName.getText().toString();
    }

    public String getDepartureStationName() {
        return txtDepartureStationName.getText().toString();
    }
}