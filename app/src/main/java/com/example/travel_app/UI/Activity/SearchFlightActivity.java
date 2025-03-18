package com.example.travel_app.UI.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.example.travel_app.Data.Model.Airport;
import com.example.travel_app.Data.Model.SearchFlightInfo;
import com.example.travel_app.R;
import com.example.travel_app.UI.Fragment.MultiCityFlightFragment;
import com.example.travel_app.UI.Fragment.OneWayFlightFragment;
import com.example.travel_app.UI.Fragment.RoundTripFlightFragment;

public class SearchFlightActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SELECT_AIRPORT = 1;

    private FragmentManager fragmentManager;
    private Button btnOneWay, btnRoundTrip, btnMultiCity, btnSearchFlight;
    private Fragment oneWayFragment, roundTripFragment, multiCityFragment;
    private Fragment currentFragment;
    private String currentSelectionType;  // Lưu loại sân bay đang chọn (departure/arrival)
    private TextView txtSeatType, txtCustomerNumber;
    private ImageButton btnSeatType;
    private CardView cvChooseCustomer;

    private int adultCount = 1, childCount = 0, infantCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_flight);

        fragmentManager = getSupportFragmentManager();

        oneWayFragment = new OneWayFlightFragment();
        roundTripFragment = new RoundTripFlightFragment();
        multiCityFragment = new MultiCityFlightFragment();

        btnOneWay = findViewById(R.id.btn_one_way);
        btnRoundTrip = findViewById(R.id.btn_round_trip);
        btnMultiCity = findViewById(R.id.btn_multi_city);
        btnSearchFlight = findViewById(R.id.btn_search_flight);

        btnOneWay.setOnClickListener(v -> switchFragment(oneWayFragment, btnOneWay));
        btnRoundTrip.setOnClickListener(v -> switchFragment(roundTripFragment, btnRoundTrip));
        btnMultiCity.setOnClickListener(v -> switchFragment(multiCityFragment, btnMultiCity));
        btnSearchFlight.setOnClickListener(v -> openSearchFlightResultActivity());

        // Mặc định hiển thị OneWayFlightFragment
        switchFragment(oneWayFragment, btnOneWay);

        txtCustomerNumber = findViewById(R.id.txt_customer_number);
        cvChooseCustomer = findViewById(R.id.cv_choose_customer_number);
        cvChooseCustomer.setOnClickListener(v -> showCustomerDialog());

        btnSeatType = findViewById(R.id.btn_seat_type);
        txtSeatType = findViewById(R.id.txt_seat_type);
        btnSeatType.setOnClickListener(v -> showSeatTypeMenu(v));
    }

    private void showSeatTypeMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);

        popupMenu.getMenu().add(0, 1, 0, "Economy");
        popupMenu.getMenu().add(0, 2, 1, "Business");

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == 1) {
                txtSeatType.setText("Economy");
                return true;
            } else if (item.getItemId() == 2) {
                txtSeatType.setText("Business");
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    private void switchFragment(Fragment fragment, Button selectedButton) {
        currentFragment = fragment;
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container_search_flight, fragment)
                .commit();

        resetButtonStyles();
        selectedButton.setBackgroundResource(R.drawable.button_selected);
        selectedButton.setTextColor(getResources().getColor(android.R.color.white));
    }

    private void resetButtonStyles() {
        btnOneWay.setTextColor(Color.parseColor("#B8B8B8"));
        btnOneWay.setBackgroundColor(getResources().getColor(android.R.color.white));

        btnRoundTrip.setTextColor(Color.parseColor("#B8B8B8"));
        btnRoundTrip.setBackgroundColor(getResources().getColor(android.R.color.white));

        btnMultiCity.setTextColor(Color.parseColor("#B8B8B8"));
        btnMultiCity.setBackgroundColor(getResources().getColor(android.R.color.white));
    }

    // Nhận dữ liệu sân bay từ SearchAirportActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_AIRPORT && resultCode == RESULT_OK) {
            Airport airport = (Airport) data.getSerializableExtra("selected_airport");

            if (currentFragment instanceof OnAirportSelectedListener) {
                ((OnAirportSelectedListener) currentFragment).onAirportSelected(airport, currentSelectionType);
            }
        }
    }

    public interface OnAirportSelectedListener {
        void onAirportSelected(Airport airport, String selectionType);
    }

    private void showCustomerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_choose_customer, null);
        builder.setView(dialogView);

        // Lấy các view trong dialog
        TextView txtAdult = dialogView.findViewById(R.id.txt_adult);
        TextView txtChild = dialogView.findViewById(R.id.txt_child);
        TextView txtInfant = dialogView.findViewById(R.id.txt_infant);

        ImageButton btnAdultPlus = dialogView.findViewById(R.id.btn_adult_plus);
        ImageButton btnAdultMinus = dialogView.findViewById(R.id.btn_adult_minus);
        ImageButton btnChildPlus = dialogView.findViewById(R.id.btn_child_plus);
        ImageButton btnChildMinus = dialogView.findViewById(R.id.btn_child_minus);
        ImageButton btnInfantPlus = dialogView.findViewById(R.id.btn_infant_plus);
        ImageButton btnInfantMinus = dialogView.findViewById(R.id.btn_infant_minus);

        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);

        // Set giá trị ban đầu
        txtAdult.setText(String.valueOf(adultCount));
        txtChild.setText(String.valueOf(childCount));
        txtInfant.setText(String.valueOf(infantCount));

        // Xử lý tăng/giảm số lượng
        btnAdultPlus.setOnClickListener(v -> {
            adultCount++;
            txtAdult.setText(String.valueOf(adultCount));
        });

        btnAdultMinus.setOnClickListener(v -> {
            if (adultCount > 1) {
                adultCount--;
                txtAdult.setText(String.valueOf(adultCount));
            }
        });

        btnChildPlus.setOnClickListener(v -> {
            childCount++;
            txtChild.setText(String.valueOf(childCount));
        });

        btnChildMinus.setOnClickListener(v -> {
            if (childCount > 0) {
                childCount--;
                txtChild.setText(String.valueOf(childCount));
            }
        });

        btnInfantPlus.setOnClickListener(v -> {
            infantCount++;
            txtInfant.setText(String.valueOf(infantCount));
        });

        btnInfantMinus.setOnClickListener(v -> {
            if (infantCount > 0) {
                infantCount--;
                txtInfant.setText(String.valueOf(infantCount));
            }
        });

        AlertDialog dialog = builder.create();
        btnConfirm.setOnClickListener(v -> {
            int customerCount = adultCount + childCount + infantCount;
            txtCustomerNumber.setText(customerCount + " hành khách");
            dialog.dismiss();
        });

        dialog.show();
    }

    private void openSearchFlightResultActivity() {
        Intent intent = new Intent(this, SearchFlightResultActivity.class);
        SearchFlightInfo searchFlightInfo = null;

        if (currentFragment instanceof OneWayFlightFragment) {
            OneWayFlightFragment oneWayFragment = (OneWayFlightFragment) currentFragment;
            searchFlightInfo = new SearchFlightInfo(
                    oneWayFragment.getDepartureAirportCode(),
                    oneWayFragment.getArrivalAirportCode(),
                    oneWayFragment.getDepartureDate(),
                    oneWayFragment.getDepartureCity(),
                    oneWayFragment.getArrivalCity(),
                    oneWayFragment.getDepartureAirportName(),
                    oneWayFragment.getArrivalAirportName(),
                    txtSeatType.getText().toString(),
                    adultCount, childCount, infantCount
            );
        }

        intent.putExtra("searchFlightInfo", searchFlightInfo);
        startActivity(intent);
    }
}