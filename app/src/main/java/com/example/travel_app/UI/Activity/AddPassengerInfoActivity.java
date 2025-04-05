package com.example.travel_app.UI.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import com.example.travel_app.Data.Model.BookingFlight;
import com.example.travel_app.Data.Model.Passenger;
import com.example.travel_app.Data.Model.SearchFlightInfo;
import com.example.travel_app.Data.Model.SelectedFlight;
import com.example.travel_app.R;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddPassengerInfoActivity extends AppCompatActivity {
    private SearchFlightInfo searchFlightInfo;
    private SelectedFlight selectedFlight;
    private List<Passenger> passengerList = new ArrayList<>();
    private TextView txtDepartureIntinerary, txtDepartureFlightInfo1, txtDepartureFlightInfo2,
            txtReturnIntinerary, txtReturnFlightInfo1, txtReturnFlightInfo2, txtTitle;
    private ImageView imgDepartureAirline, imgReturnAirline;
    private Button btnConfirm;
    private BookingFlight bookingFlight = new BookingFlight();

    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_passenger_info);

        searchFlightInfo = (SearchFlightInfo) getIntent().getSerializableExtra("searchFlightInfo");
        selectedFlight = (SelectedFlight) getIntent().getSerializableExtra("selectedFlight");

        bookingFlight.setDepartureFlight(selectedFlight.getDepartureFlight());

        txtDepartureIntinerary = findViewById(R.id.txt_departure_itinerary);
        txtDepartureFlightInfo1 = findViewById(R.id.txt_departure_flight_info1);
        txtDepartureFlightInfo2 = findViewById(R.id.txt_departure_flight_info2);

        txtDepartureIntinerary.setText(searchFlightInfo.getDepartureCity() + " đến " + searchFlightInfo.getArrivalCity());
        String departureFlightInfo1 = searchFlightInfo.getDepartureAirportCode() + " -> " + searchFlightInfo.getArrivalAirportCode()
                + " | " + searchFlightInfo.getDepartureDate();
        txtDepartureFlightInfo1.setText(departureFlightInfo1);

        String departureFlightInfo2 =  selectedFlight.getDepartureFlight().getDepartureTime() + " - " + selectedFlight.getDepartureFlight().getArrivalTime()
                + " | " + selectedFlight.getDepartureFlight().getFlightTime();
        txtDepartureFlightInfo2.setText(departureFlightInfo2);

        imgDepartureAirline = findViewById(R.id.img_departure_airline_logo);
        Picasso.get().load(selectedFlight.getDepartureFlight().getAirlineImgUrl()).into(imgDepartureAirline);

        if(selectedFlight.getReturnFlight() != null) {
            LinearLayout lnReturnFlight = findViewById(R.id.ln_return_flight);
            lnReturnFlight.setVisibility(TextView.VISIBLE);

            bookingFlight.setReturnFlight(selectedFlight.getReturnFlight());

            txtReturnIntinerary = findViewById(R.id.txt_return_itinerary);
            txtReturnFlightInfo1 = findViewById(R.id.txt_return_flight_info1);
            txtReturnFlightInfo2 = findViewById(R.id.txt_return_flight_info2);
            txtTitle = findViewById(R.id.txt_title_add_customer);

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) txtTitle.getLayoutParams();
            int marginTopPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 175, getResources().getDisplayMetrics());
            params.setMargins(0, marginTopPx, 0, 0);
            txtTitle.setLayoutParams(params);
            txtTitle.requestLayout();

            txtReturnIntinerary.setText(searchFlightInfo.getArrivalCity() + " đến " + searchFlightInfo.getDepartureCity());
            String returnFlightInfo1 = searchFlightInfo.getArrivalAirportCode() + " -> " + searchFlightInfo.getDepartureAirportCode()
                    + " | " + searchFlightInfo.getReturnDate();
            txtReturnFlightInfo1.setText(returnFlightInfo1);

            String returnFlightInfo2 =  selectedFlight.getReturnFlight().getDepartureTime() + " - " + selectedFlight.getReturnFlight().getArrivalTime()
                    + " | " + selectedFlight.getReturnFlight().getFlightTime();
            txtReturnFlightInfo2.setText(returnFlightInfo2);

            imgReturnAirline = findViewById(R.id.img_return_airline_logo);
            Picasso.get().load(selectedFlight.getReturnFlight().getAirlineImgUrl()).into(imgReturnAirline);
        }

        LinearLayout containerCustomerInfo = findViewById(R.id.container_customer_info);

        initializeCustomerList();

        int currentIndex = 0;
        currentIndex = addPassengerInfoCards(containerCustomerInfo, "Người lớn", currentIndex, searchFlightInfo.getAdultCount());
        currentIndex = addPassengerInfoCards(containerCustomerInfo, "Trẻ em", currentIndex, searchFlightInfo.getChildCount());
        addPassengerInfoCards(containerCustomerInfo, "Em bé", currentIndex, searchFlightInfo.getInfantCount());

        btnConfirm = findViewById(R.id.btn_confirm_add_customer_info);
        btnConfirm.setOnClickListener(v -> {
            Intent intent = new Intent(AddPassengerInfoActivity.this, SelectSeatActivity.class);
            bookingFlight.setPassengerList(passengerList);
//            intent.putExtra("passengerList", (Serializable) passengerList);
//            intent.putExtra("selectedFlight", selectedFlight);
            intent.putExtra("bookingFlight",bookingFlight);
            intent.putExtra("searchFlightInfo", searchFlightInfo);
            startActivity(intent);
        });
    }

    private void initializeCustomerList() {
        int totalCustomers = searchFlightInfo.getAdultCount() + searchFlightInfo.getChildCount() + searchFlightInfo.getInfantCount();
        for (int i = 0; i < totalCustomers; i++) {
            passengerList.add(new Passenger());
        }
    }

    private int addPassengerInfoCards(LinearLayout container, String type, int startIndex, int count) {
        for (int i = 0; i < count; i++) {
            CardView cardView = new CardView(this);
            cardView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            cardView.setCardElevation(8);
            cardView.setRadius(12);
            cardView.setPreventCornerOverlap(true);
            cardView.setUseCompatPadding(true);
            cardView.setContentPadding(20, 10, 20, 10);

            LinearLayout layout = new LinearLayout(this);
            layout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setGravity(Gravity.CENTER_VERTICAL);

            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            textView.setText(type + " " + String.valueOf(i + 1));
            textView.setTextSize(16);

            ImageButton addButton = new ImageButton(this);
            addButton.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
            addButton.setImageResource(R.drawable.add_itinerary);
            addButton.setBackgroundColor(Color.TRANSPARENT);

            layout.addView(textView);
            layout.addView(addButton);
            cardView.addView(layout);
            container.addView(cardView);

            int currentIndex = startIndex + i;

            cardView.setOnClickListener(v -> {
                Passenger currentPassenger = passengerList.get(currentIndex);
                Intent intent = new Intent(AddPassengerInfoActivity.this, PassengerInfoActivity.class);
                intent.putExtra("passenger", currentPassenger);
                intent.putExtra("passengerIndex", currentIndex);
                startActivityForResult(intent, 1);
            });
        }
        return startIndex + count;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Passenger updatedPassenger = (Passenger) data.getSerializableExtra("updatedPassenger");
            int customerIndex = data.getIntExtra("passengerIndex", -1);
            if(updatedPassenger != null && customerIndex != -1) {
                passengerList.set(customerIndex, updatedPassenger);
            }
        }
    }
}
