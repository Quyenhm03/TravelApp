package com.example.travel_app.UI.Fragment;

import android.annotation.SuppressLint;
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
import com.example.travel_app.R;
import com.squareup.picasso.Picasso;

import java.time.Duration;
import java.time.LocalTime;

public class FlightDetailFragment extends Fragment {

    private TextView txtDepartureTime, txtDepartureAirportCode, txtDepartureAirportAddress;
    private TextView txtArrivalTime, txtArrivalAirportCode, txtArrivalAirportAddress;
    private TextView txtDepartureDate, txtFlightTime, txtTicketPrice;
    private ImageView imgAirline;
    private Button btnBookFlight, btnCancelFlight;

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
            Flight flight = (Flight) getArguments().getSerializable("flight");
            SearchFlightInfo searchFlightInfo = (SearchFlightInfo) getArguments().getSerializable("searchFlightInfo");
            if (flight != null && searchFlightInfo != null) {
                txtDepartureTime.setText(flight.getDepartureTime());
                txtDepartureAirportCode.setText(flight.getDepartureAirportCode());
                txtDepartureAirportAddress.setText(searchFlightInfo.getDepartureAirportName());
                txtArrivalTime.setText(flight.getArrivalTime());
                txtArrivalAirportCode.setText(flight.getArrivalAirportCode());
                txtArrivalAirportAddress.setText(searchFlightInfo.getArrivalAirportName());
                txtDepartureDate.setText(flight.getDepartureDate());
                txtTicketPrice.setText((int)flight.getPrice() + " VND");
                Picasso.get().load(flight.getAirlineImgUrl()).into(imgAirline);

                LocalTime departureTime = LocalTime.parse(flight.getDepartureTime());
                LocalTime arrivalTime = LocalTime.parse(flight.getArrivalTime());
                Duration duration = Duration.between(departureTime, arrivalTime);
                long hours = duration.toHours();
                long minutes = duration.toMinutes() % 60;
                txtFlightTime.setText(String.format("%02dh %02dm", hours, minutes));
            }
        }


        btnBookFlight.setOnClickListener(v -> {
            // Xử lý logic đặt vé
        });

        btnCancelFlight.setOnClickListener(v -> {
            // Xử lý logic hủy vé
        });

        return view;
    }
}