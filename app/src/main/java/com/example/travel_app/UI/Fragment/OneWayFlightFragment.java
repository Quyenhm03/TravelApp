package com.example.travel_app.UI.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.travel_app.Data.Model.Airport;
import com.example.travel_app.R;
import com.example.travel_app.UI.Activity.SearchAirportActivity;
import com.example.travel_app.UI.Activity.SearchFlightActivity;

import java.util.Calendar;

public class OneWayFlightFragment extends Fragment {
    private static final int REQUEST_CODE_AIRPORT = 100;

    private CardView cvDepartureAirport, cvArrivalAirport, cvDepartureDate;
    private TextView txtDepartureDate, txtDepartureAirportCity, txtDepartureAirportCode, txtDepartureAirportName,
            txtArrivalAirportCity, txtArrivalAirportCode, txtArrivalAirportName;

    private String currentSelectionType; // Lưu loại sân bay đang chọn

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_way_flight, container, false);

        cvDepartureAirport = view.findViewById(R.id.cv_departure_airport);
        cvArrivalAirport = view.findViewById(R.id.cv_arrival_airport);
        cvDepartureDate = view.findViewById(R.id.cv_departure_date);
        txtDepartureDate = view.findViewById(R.id.txt_departure_date);
        txtDepartureAirportCity = view.findViewById(R.id.txt_departure_airport_city);
        txtDepartureAirportCode = view.findViewById(R.id.txt_departure_airport_code);
        txtDepartureAirportName = view.findViewById(R.id.txt_departure_airport_name_country);
        txtArrivalAirportCity = view.findViewById(R.id.txt_arrival_airport_city);
        txtArrivalAirportCode = view.findViewById(R.id.txt_arrival_airport_code);
        txtArrivalAirportName = view.findViewById(R.id.txt_arrival_airport_name_country);

        cvDepartureAirport.setOnClickListener(v -> openSearchActivity("departure"));
        cvArrivalAirport.setOnClickListener(v -> openSearchActivity("arrival"));

        cvDepartureDate.setOnClickListener(v -> showDatePickerDialog());

        return view;
    }

    private void openSearchActivity(String type) {
        currentSelectionType = type;
        Intent intent = new Intent(getActivity(), SearchAirportActivity.class);
        intent.putExtra("search_type", type);
        startActivityForResult(intent, REQUEST_CODE_AIRPORT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_AIRPORT && resultCode == getActivity().RESULT_OK && data != null) {
            Airport airport = (Airport) data.getSerializableExtra("selected_airport");
            if (airport != null) {
                updateAirportInfo(airport);
            }
        }
    }

    private void updateAirportInfo(Airport airport) {
        if ("departure".equals(currentSelectionType)) {
            txtDepartureAirportCity.setText(airport.getCity());
            txtDepartureAirportCode.setText(airport.getAirportCode());
            txtDepartureAirportName.setText(airport.getAirportNameCountry());
        } else if ("arrival".equals(currentSelectionType)) {
            txtArrivalAirportCity.setText(airport.getCity());
            txtArrivalAirportCode.setText(airport.getAirportCode());
            txtArrivalAirportName.setText(airport.getAirportNameCountry());
        }
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = String.format("%02d/%02d/%d", dayOfMonth, month1 + 1, year1);
                    txtDepartureDate.setText(selectedDate);
                },
                year, month, date
        );

        datePickerDialog.show();
    }

    public String getDepartureDate() {
        return txtDepartureDate.getText().toString();
    }

    public String getArrivalAirportCode() {
        return txtArrivalAirportCode.getText().toString();
    }

    public String getDepartureAirportCode() {
        return txtDepartureAirportCode.getText().toString();
    }

    public String getArrivalCity() {
        return txtArrivalAirportCity.getText().toString();
    }

    public String getDepartureCity() {
        return txtDepartureAirportCity.getText().toString();
    }

    public String getArrivalAirportName() {
        return txtArrivalAirportName.getText().toString();
    }

    public String getDepartureAirportName() {
        return txtDepartureAirportName.getText().toString();
    }
}