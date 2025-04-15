package com.example.travel_app.UI.Activity.Itinerary;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Adapter.Itinerary.LocationAdapter;
import com.example.travel_app.Data.Model.Itinerary.Itinerary;
import com.example.travel_app.Data.Model.Itinerary.Day;
import com.example.travel_app.R;
import com.example.travel_app.UI.Activity.BaseActivity;
import com.example.travel_app.ViewModel.Itinerary.ImageViewModel;
import com.example.travel_app.ViewModel.Itinerary.ItineraryDetailViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItineraryDetailActivity extends BaseActivity {
    private ItineraryDetailViewModel viewModel;
    private ImageViewModel imageViewModel;
    private LocationAdapter adapter;
    private RecyclerView rcvItems;
    private LinearLayout lnDaysContainer;
    private TextView txtTitle, txtDetail, txtUserName, txtCreateDate;
    private AppCompatButton btnBack;
    private ImageView imgUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_itinerary);

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(ItineraryDetailViewModel.class);
        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);

        // Lấy dữ liệu từ Intent
        Itinerary itinerary = (Itinerary) getIntent().getSerializableExtra("itinerary");
        viewModel.setItinerary(itinerary);

        // Khởi tạo các view
        rcvItems = findViewById(R.id.rcv_location);
        lnDaysContainer = findViewById(R.id.ln_days_container);
        txtTitle = findViewById(R.id.txt_title_itinerary);
        txtDetail = findViewById(R.id.txt_detail_itinerary);
        txtUserName = findViewById(R.id.txt_user_name);
        txtCreateDate = findViewById(R.id.txt_create_date);
        btnBack = findViewById(R.id.btn_back);
        imgUser = findViewById(R.id.img_user);

        // Thiết lập thông tin cơ bản
        txtTitle.setText(itinerary.getTitle() != null ? itinerary.getTitle() : "Không có tiêu đề");
        int totalLocations = itinerary.getDays() != null ?
                itinerary.getDays().stream().mapToInt(day -> day.getLocations() != null ? day.getLocations().size() : 0).sum() : 0;
        int totalDays = itinerary.getDays() != null ? itinerary.getDays().size() : 0;
        txtDetail.setText(totalLocations + " địa điểm - " + totalDays + "N" + (totalDays > 0 ? totalDays - 1 : 0) + "D");
        txtUserName.setText(itinerary.getUserName() != null ? itinerary.getUserName() : "Không xác định");
        txtCreateDate.setText("Ngày tạo: " + (itinerary.getCreateDate() != null ? itinerary.getCreateDate() : "Không xác định"));
        if(itinerary.getUserImg() != null) {
            Picasso.get().load(itinerary.getUserImg()).into(imgUser);
        } else {
            Picasso.get().load(R.drawable.ic_acc).into(imgUser);
        }

        // Thiết lập RecyclerView
        rcvItems.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LocationAdapter(new ArrayList<>(), imageViewModel);
        adapter.setDeleteButtonVisible(false);
        rcvItems.setAdapter(adapter);

        // Thiết lập các nút ngày trong HorizontalScrollView
        setupDayButtons(itinerary.getDays());

        // Quan sát dữ liệu từ ViewModel
        viewModel.getLocationsLiveData().observe(this, locations -> {
            if (locations != null) {
                adapter.updateList(locations);
            }
        });

        // Xử lý sự kiện click nút Back
        btnBack.setOnClickListener(v -> finish());
    }

    private void setupDayButtons(List<Day> days) {
        lnDaysContainer.removeAllViews();
        if (days == null || days.isEmpty()) {
            return;
        }

        for (int i = 0; i < days.size(); i++) {
            Day day = days.get(i);
            CardView cardView = new CardView(this);
            cardView.setCardElevation(12);
            cardView.setMaxCardElevation(12);
            cardView.setRadius(48);
            cardView.setPreventCornerOverlap(true);

            AppCompatButton button = new AppCompatButton(this);
            button.setText(day.getDate() != null ? day.getDate() : "Ngày " + (i + 1));
            button.setTextSize(14);
            button.setTextColor(i == 0 ? getResources().getColor(R.color.white) : Color.parseColor("#B8B8B8"));
            button.setBackgroundColor(i == 0 ? getResources().getColor(R.color.orange) : Color.TRANSPARENT);
            button.setAllCaps(false);

            final int dayIndex = i;
            button.setOnClickListener(v -> {
                for (int j = 0; j < lnDaysContainer.getChildCount(); j++) {
                    CardView childCard = (CardView) lnDaysContainer.getChildAt(j);
                    AppCompatButton childButton = (AppCompatButton) childCard.getChildAt(0);
                    childButton.setTextColor(j == dayIndex ? getResources().getColor(R.color.white) : Color.parseColor("#B8B8B8"));
                    childButton.setBackgroundColor(j == dayIndex ? getResources().getColor(R.color.orange) : Color.TRANSPARENT);
                }
                viewModel.selectDay(dayIndex);
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