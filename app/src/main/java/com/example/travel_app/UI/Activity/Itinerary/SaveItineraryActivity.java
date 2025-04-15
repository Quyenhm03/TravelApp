package com.example.travel_app.UI.Activity.Itinerary;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Adapter.Itinerary.DayLocationAdapter;
import com.example.travel_app.Data.Model.Itinerary.Day;
import com.example.travel_app.Data.Model.Itinerary.Itinerary;
import com.example.travel_app.Data.Model.Itinerary.Location;
import com.example.travel_app.R;
import com.example.travel_app.UI.Activity.BaseActivity;
import com.example.travel_app.UI.Activity.Location.LocationActivity;
import com.example.travel_app.UI.Login.LoginActivity;
import com.example.travel_app.ViewModel.Itinerary.ImageViewModel;
import com.example.travel_app.ViewModel.Itinerary.ItineraryViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class SaveItineraryActivity extends BaseActivity {
    private ItineraryViewModel viewModel;
    private ImageViewModel imageViewModel;
    private RecyclerView rcvDayLocation;
    private DayLocationAdapter adapter;
    private LinearLayout lnDaysContainer;
    private AppCompatButton btnAddItem, btnSaveItinerary, btnBack;
    private TextView txtTimeItinerary;
    private Itinerary itinerary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_itinerary);
        viewModel = new ViewModelProvider(this).get(ItineraryViewModel.class);
        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);

        itinerary = (Itinerary) getIntent().getSerializableExtra("itinerary");
        if (itinerary != null) {
            viewModel.setItinerary(itinerary);
        }

        rcvDayLocation = findViewById(R.id.rcv_day_item);
        lnDaysContainer = findViewById(R.id.ln_days_container);
        btnAddItem = findViewById(R.id.btn_add_item);
        btnSaveItinerary = findViewById(R.id.btn_save_itinerary);
        btnBack = findViewById(R.id.btn_back);
        txtTimeItinerary = findViewById(R.id.txt_time_itinerary);

        rcvDayLocation.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DayLocationAdapter(new ArrayList<>(), imageViewModel, new DayLocationAdapter.OnDeleteListener() {
            @Override
            public void onDeleteClick(Location location) {
                Integer dayIndex = viewModel.getSelectedDayIndex().getValue();
                if (dayIndex != null) {
                    viewModel.removeLocationFromDay(dayIndex, location);
                    Itinerary itineraryData = viewModel.getItineraryLiveData().getValue();
                    if (itineraryData != null && itineraryData.getDays() != null && dayIndex < itineraryData.getDays().size()) {
                        adapter.updateLocations(itineraryData.getDays().get(dayIndex).getLocations());
                        Toast.makeText(SaveItineraryActivity.this, "Đã xóa location", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, location -> {
            // Khi click vào item, mở LocationActivity
            Intent intent = new Intent(SaveItineraryActivity.this, LocationActivity.class);
            intent.putExtra("location_id", location.getLocation_id());
            startActivity(intent);
        });
        rcvDayLocation.setAdapter(adapter);

        viewModel.getItineraryLiveData().observe(this, itineraryData -> {
            if (itineraryData != null && itineraryData.getDays() != null) {
                if (!itineraryData.getDays().isEmpty()) {
                    txtTimeItinerary.setText(itineraryData.getDays().get(0).getDate() + " - " +
                            itineraryData.getDays().get(itineraryData.getDays().size() - 1).getDate());
                    setupDayButtons(itineraryData.getDays());
                } else {
                    txtTimeItinerary.setText("Chưa có ngày nào được thiết lập");
                    lnDaysContainer.removeAllViews();
                }
            } else {
                txtTimeItinerary.setText("Chưa có ngày nào được thiết lập");
                lnDaysContainer.removeAllViews();
            }
        });

        viewModel.getSelectedDayIndex().observe(this, dayIndex -> {
            if (dayIndex != null) {
                Itinerary itineraryData = viewModel.getItineraryLiveData().getValue();
                if (itineraryData != null && itineraryData.getDays() != null && dayIndex < itineraryData.getDays().size()) {
                    adapter.updateLocations(itineraryData.getDays().get(dayIndex).getLocations());
                    for (int i = 0; i < lnDaysContainer.getChildCount(); i++) {
                        CardView childCard = (CardView) lnDaysContainer.getChildAt(i);
                        AppCompatButton childButton = (AppCompatButton) childCard.getChildAt(0);
                        childButton.setTextColor(i == dayIndex ? getResources().getColor(R.color.white) : Color.parseColor("#B8B8B8"));
                        childButton.setBackgroundColor(i == dayIndex ? getResources().getColor(R.color.orange) : Color.TRANSPARENT);
                    }
                }
            }
        });

        btnAddItem.setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchLocationActivity.class);
            Integer selectedDayIndex = viewModel.getSelectedDayIndex().getValue();
            Itinerary itineraryData = viewModel.getItineraryLiveData().getValue();
            if (selectedDayIndex != null && itineraryData != null && itineraryData.getDays() != null &&
                    selectedDayIndex < itineraryData.getDays().size()) {
                String selectedDate = itineraryData.getDays().get(selectedDayIndex).getDate();
                intent.putExtra("selectedDate", selectedDate);
            }
            startActivityForResult(intent, 1);
        });

        btnSaveItinerary.setOnClickListener(v -> {
            Itinerary itinerary = viewModel.getItineraryLiveData().getValue();
            if (itinerary != null) {
                String newId = "itinerary_" + System.currentTimeMillis();
                itinerary.setId(newId);
                itinerary.setUserId(getUserId());
                itinerary.setUserName("Nguyễn Thị Quyên");

                viewModel.saveItinerary(new ItineraryViewModel.Callback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(SaveItineraryActivity.this, "Lưu lộ trình thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(SaveItineraryActivity.this, "Lỗi khi lưu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(this, "Lộ trình không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private String getUserId() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "Bạn cần đăng nhập để thực hiện thanh toán", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return currentUser.getUid();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Location selectedLocation = (Location) data.getSerializableExtra("selectedLocation");
            Integer dayIndex = viewModel.getSelectedDayIndex().getValue();
            if (dayIndex != null && selectedLocation != null) {
                viewModel.addLocationToDay(dayIndex, selectedLocation);
                Itinerary itineraryData = viewModel.getItineraryLiveData().getValue();
                if (itineraryData != null && itineraryData.getDays() != null && dayIndex < itineraryData.getDays().size()) {
                    adapter.updateLocations(itineraryData.getDays().get(dayIndex).getLocations());
                }
            }
        }
    }

    private void setupDayButtons(List<Day> days) {
        lnDaysContainer.removeAllViews();
        if (days == null || days.isEmpty()) {
            return;
        }

        Integer selectedIndex = viewModel.getSelectedDayIndex().getValue();
        int initialSelectedIndex = selectedIndex != null ? selectedIndex : 0;

        for (int i = 0; i < days.size(); i++) {
            Day day = days.get(i);
            CardView cardView = new CardView(this);
            cardView.setCardElevation(12);
            cardView.setMaxCardElevation(12);
            cardView.setRadius(48);
            cardView.setPreventCornerOverlap(true);

            AppCompatButton button = new AppCompatButton(this);
            String date = day.getDate();
            button.setText(date != null ? date : "Ngày " + (i + 1));
            button.setTextSize(14);
            button.setTextColor(i == initialSelectedIndex ? getResources().getColor(R.color.white) : Color.parseColor("#B8B8B8"));
            button.setBackgroundColor(i == initialSelectedIndex ? getResources().getColor(R.color.orange) : Color.TRANSPARENT);
            button.setAllCaps(false);

            final int dayIndex = i;
            button.setOnClickListener(v -> {
                viewModel.setSelectedDay(dayIndex);
            });

            cardView.addView(button);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(i == 0 ? 0 : 35, 5, 2, 5);
            cardView.setLayoutParams(params);
            lnDaysContainer.addView(cardView);
        }
    }
}