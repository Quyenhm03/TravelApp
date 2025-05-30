package com.example.travel_app.UI.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.travel_app.Data.Model.Flight;
import com.example.travel_app.Data.Model.SearchFlightInfo;
import com.example.travel_app.Data.Model.SelectedFlight;
import com.example.travel_app.R;
import com.example.travel_app.UI.Activity.AddPassengerInfoFlightActivity;
import com.example.travel_app.UI.Activity.SearchFlightResultActivity;
import com.squareup.picasso.Picasso;

public class FlightDetailFragment extends Fragment {

    private TextView txtDepartureTime, txtDepartureAirportCode, txtDepartureAirportAddress;
    private TextView txtArrivalTime, txtArrivalAirportCode, txtArrivalAirportAddress;
    private TextView txtDepartureDate, txtFlightTime, txtTicketPrice;
    private ImageView imgAirline;
    private Button btnBookFlight, btnCancelFlight;
    private SearchFlightInfo searchFlightInfo;
    private Flight flight;
    private boolean isReturnFlight;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_detail, container, false);

        txtDepartureTime = view.findViewById(R.id.txt_departure_time);
        txtDepartureAirportCode = view.findViewById(R.id.txt_departure_airport_code);
        txtDepartureAirportAddress = view.findViewById(R.id.txt_departure_airport_address);
        txtArrivalTime = view.findViewById(R.id.txt_arrival_time);
        txtArrivalAirportCode = view.findViewById(R.id.txt_arrival_airport_code);
        txtArrivalAirportAddress = view.findViewById(R.id.txt_arrival_airport_address);
        txtDepartureDate = view.findViewById(R.id.txt_departure_date);
        txtFlightTime = view.findViewById(R.id.txt_flight_time);
        txtTicketPrice = view.findViewById(R.id.txt_ticket_flight_price);
        imgAirline= view.findViewById(R.id.img_airline_logo);
        btnBookFlight = view.findViewById(R.id.btn_book_flight);
        btnCancelFlight = view.findViewById(R.id.btn_cancel_flight);

        if (getArguments() != null) {
            flight = (Flight) getArguments().getSerializable("flight");
            searchFlightInfo = (SearchFlightInfo) getArguments().getSerializable("searchFlightInfo");
            isReturnFlight = getArguments().getBoolean("isReturnFlight", false);
            if (flight != null && searchFlightInfo != null) {
                txtDepartureTime.setText(flight.getDepartureTime());
                txtDepartureAirportCode.setText(flight.getDepartureAirportCode());
                txtDepartureAirportAddress.setText(searchFlightInfo.getDepartureAirportName());
                txtArrivalTime.setText(flight.getArrivalTime());
                txtArrivalAirportCode.setText(flight.getArrivalAirportCode());
                txtArrivalAirportAddress.setText(searchFlightInfo.getArrivalAirportName());
                txtDepartureDate.setText(flight.getDepartureDate());
                String formattedPrice = String.format("%,.0f VNĐ", flight.getPrice());
                txtTicketPrice.setText(formattedPrice);
                txtFlightTime.setText(flight.getFlightTime());
                Picasso.get().load(flight.getAirlineImgUrl()).into(imgAirline);
            }
        }


        btnBookFlight.setOnClickListener(v -> {
            SelectedFlight selectedFlight;
            if(getActivity().getIntent().hasExtra("selectedFlight")) {
                selectedFlight = (SelectedFlight) getActivity().getIntent().getSerializableExtra("selectedFlight");
            } else {
                selectedFlight = new SelectedFlight();
            }

            if(!isReturnFlight) {
                selectedFlight.setDepartureFlight(flight);

                if (searchFlightInfo.getReturnDate() != null) {
                    // quay về SearchFlightActivity để chọn chuyến bay về
                    Intent intent = new Intent(requireActivity(), SearchFlightResultActivity.class);
                    intent.putExtra("searchFlightInfo", searchFlightInfo);
                    intent.putExtra("selectedFlight", selectedFlight);
                    intent.putExtra("isReturnFlight", true);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(requireActivity(), AddPassengerInfoFlightActivity.class);
                    intent.putExtra("searchFlightInfo", searchFlightInfo);
                    intent.putExtra("selectedFlight", selectedFlight);
                    startActivity(intent);
                }
            } else {
                selectedFlight.setReturnFlight(flight);
                Intent intent = new Intent(requireActivity(), AddPassengerInfoFlightActivity.class);
                intent.putExtra("searchFlightInfo", searchFlightInfo);
                intent.putExtra("selectedFlight", selectedFlight);
                startActivity(intent);
            }

        });

        btnCancelFlight.setOnClickListener(v -> requireActivity().onBackPressed());

        return view;
    }
}