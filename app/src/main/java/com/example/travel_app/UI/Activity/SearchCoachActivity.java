package com.example.travel_app.UI.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.travel_app.Data.Model.SearchCoachInfo;
import com.example.travel_app.R;
import com.example.travel_app.UI.Fragment.OneWayCoachFragment;
import com.example.travel_app.UI.Fragment.RoundTripCoachFragment;

public class SearchCoachActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SELECT_BUS_STATION = 1;

    private FragmentManager fragmentManager;
    private Button btnOneWay, btnRoundTrip, btnSearchCoach;
    private Fragment oneWayFragment, roundTripFragment;
    private Fragment currentFragment;
    private TextView txtSeatNumber;
    private CardView cvChooseSeatNumber;

    private int seatCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_coach);

        fragmentManager = getSupportFragmentManager();

        oneWayFragment = new OneWayCoachFragment();
        roundTripFragment = new RoundTripCoachFragment();

        btnOneWay = findViewById(R.id.btn_one_way);
        btnRoundTrip = findViewById(R.id.btn_return);
        btnSearchCoach = findViewById(R.id.btn_find_flight);

        btnOneWay.setOnClickListener(v -> switchFragment(oneWayFragment, btnOneWay));
        btnRoundTrip.setOnClickListener(v -> switchFragment(roundTripFragment, btnRoundTrip));
        btnSearchCoach.setOnClickListener(v -> openSearchCoachResultActivity());

        // Mặc định hiển thị OneWayCoachFragment
        switchFragment(oneWayFragment, btnOneWay);

        txtSeatNumber = findViewById(R.id.txt_seat_number);
        cvChooseSeatNumber = findViewById(R.id.cv_choose_seat_number);
        cvChooseSeatNumber.setOnClickListener(v -> showSeatNumberDialog());

        // Cập nhật số ghế ban đầu
        txtSeatNumber.setText(seatCount + " ghế ngồi");
    }

    private void switchFragment(Fragment fragment, Button selectedButton) {
        currentFragment = fragment;
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container_search_coach, fragment)
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
    }

    private void showSeatNumberDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_choose_seat_number, null); // Cần tạo layout này
        builder.setView(dialogView);

        // Lấy các view trong dialog
        TextView txtSeatCount = dialogView.findViewById(R.id.txt_seat_count);
        ImageButton btnSeatPlus = dialogView.findViewById(R.id.btn_seat_plus);
        ImageButton btnSeatMinus = dialogView.findViewById(R.id.btn_seat_minus);
        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);

        // Set giá trị ban đầu
        txtSeatCount.setText(String.valueOf(seatCount));

        // Xử lý tăng/giảm số lượng ghế
        btnSeatPlus.setOnClickListener(v -> {
            seatCount++;
            txtSeatCount.setText(String.valueOf(seatCount));
        });

        btnSeatMinus.setOnClickListener(v -> {
            if (seatCount > 1) {
                seatCount--;
                txtSeatCount.setText(String.valueOf(seatCount));
            }
        });

        AlertDialog dialog = builder.create();
        btnConfirm.setOnClickListener(v -> {
            txtSeatNumber.setText(seatCount + " ghế ngồi");
            dialog.dismiss();
        });

        dialog.show();
    }

    private void openSearchCoachResultActivity() {
        Intent intent = new Intent(this, SearchCoachResultActivity.class);
        SearchCoachInfo searchCoachInfo = null;

        if (currentFragment instanceof OneWayCoachFragment) {
            OneWayCoachFragment oneWayFragment = (OneWayCoachFragment) currentFragment;
            searchCoachInfo = new SearchCoachInfo(
                    oneWayFragment.getDepartureDate(),
                    oneWayFragment.getDepartureCity(),
                    oneWayFragment.getArrivalCity(),
                    oneWayFragment.getDepartureStationName(),
                    oneWayFragment.getArrivalStationName(),
                    seatCount
            );
        } else {
            RoundTripCoachFragment roundTripFragment = (RoundTripCoachFragment) currentFragment;
            searchCoachInfo = new SearchCoachInfo(
                    roundTripFragment.getDepartureDate(),
                    roundTripFragment.getReturnDate(),
                    roundTripFragment.getDepartureCity(),
                    roundTripFragment.getArrivalCity(),
                    roundTripFragment.getDepartureStationName(),
                    roundTripFragment.getArrivalStationName(),
                    seatCount
            );
        }

        intent.putExtra("searchCoachInfo", searchCoachInfo);
        startActivity(intent);
    }
}
