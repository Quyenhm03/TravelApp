package com.example.travel_app.UI.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Adapter.DayItemAdapter;
import com.example.travel_app.Data.Model.Day;
import com.example.travel_app.Data.Model.Item;
import com.example.travel_app.Data.Model.Itinerary;
import com.example.travel_app.R;
import com.example.travel_app.ViewModel.ItineraryViewModel;

import java.util.ArrayList;
import java.util.List;

public class SaveItineraryActivity extends AppCompatActivity {
    private ItineraryViewModel viewModel;
    private RecyclerView rcvDayItem;
    private DayItemAdapter adapter;
    private LinearLayout lnDaysContainer;
    private AppCompatButton btnAddItem, btnSaveItinerary, btnBack;
    private TextView txtTimeItinerary;
    private Itinerary itinerary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_itinerary);

        viewModel = new ViewModelProvider(this).get(ItineraryViewModel.class);

        itinerary = (Itinerary) getIntent().getSerializableExtra("itinerary");
        if (itinerary != null) {
            viewModel.setItinerary(itinerary);
        }

        rcvDayItem = findViewById(R.id.rcv_day_item);
        lnDaysContainer = findViewById(R.id.ln_days_container);
        btnAddItem = findViewById(R.id.btn_add_item);
        btnSaveItinerary = findViewById(R.id.btn_save_itinerary);
        btnBack = findViewById(R.id.btn_back);
        txtTimeItinerary = findViewById(R.id.txt_time_itinerary);

        rcvDayItem.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DayItemAdapter(new ArrayList<>(), new DayItemAdapter.OnDeleteListener() {
            @Override
            public void onDeleteClick(Item item) {
                Integer dayIndex = viewModel.getSelectedDayIndex().getValue();
                if (dayIndex != null) {
                    viewModel.removeItemFromDay(dayIndex, item);
                    Itinerary itineraryData = viewModel.getItineraryLiveData().getValue();
                    if (itineraryData != null && itineraryData.getDays() != null && dayIndex < itineraryData.getDays().size()) {
                        adapter.updateItems(itineraryData.getDays().get(dayIndex).getItems());
                        Toast.makeText(SaveItineraryActivity.this, "Đã xóa item", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        rcvDayItem.setAdapter(adapter);

        viewModel.getItineraryLiveData().observe(this, itineraryData -> {
            Log.d("SaveItinerary", "Itinerary: " + (itineraryData != null ? "not null" : "null"));
            if (itineraryData != null && itineraryData.getDays() != null) {
                Log.d("SaveItinerary", "Days size: " + itineraryData.getDays().size());
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
                    adapter.updateItems(itineraryData.getDays().get(dayIndex).getItems());
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
            Intent intent = new Intent(this, SearchItemActivity.class);
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
                // Sinh và gán ID ngay tại đây trước khi lưu
                String newId = "itinerary_" + System.currentTimeMillis();
                itinerary.setId(newId);

                viewModel.saveItinerary(new ItineraryViewModel.Callback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(SaveItineraryActivity.this, "Lưu lộ trình thành công", Toast.LENGTH_SHORT).show();
                        Log.d("SaveItinerary", "Save successful, finishing activity");
                        finish();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(SaveItineraryActivity.this, "Lỗi khi lưu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("SaveItinerary", "Save failed: " + e.getMessage(), e);
                    }
                });
            } else {
                Toast.makeText(this, "Lộ trình không hợp lệ", Toast.LENGTH_SHORT).show();
                Log.e("SaveItinerary", "Itinerary is null");
            }
        });

        btnBack.setOnClickListener(v -> finish());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Item selectedItem = (Item) data.getSerializableExtra("selectedItem");
            Integer dayIndex = viewModel.getSelectedDayIndex().getValue();
            if (dayIndex != null && selectedItem != null) {
                viewModel.addItemToDay(dayIndex, selectedItem);
                Itinerary itineraryData = viewModel.getItineraryLiveData().getValue();
                if (itineraryData != null && itineraryData.getDays() != null && dayIndex < itineraryData.getDays().size()) {
                    adapter.updateItems(itineraryData.getDays().get(dayIndex).getItems());
                }
            }
        }
    }
}