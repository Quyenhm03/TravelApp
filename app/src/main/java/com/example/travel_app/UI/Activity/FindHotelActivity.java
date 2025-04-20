package com.example.travel_app.UI.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Adapter.HotelAdapter;
import com.example.travel_app.Data.Model.Hotel;
import com.example.travel_app.Data.Repository.HotelRepository;
import com.example.travel_app.R;
import com.example.travel_app.ViewModel.HotelViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class FindHotelActivity extends AppCompatActivity {
    private static final String TAG = "FindHotelActivity";
    private HotelViewModel hotelViewModel;
    private RecyclerView recyclerViewHotels;
    private HotelAdapter hotelAdapter;
    private EditText editTextSearch;
    private ImageView iconSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_hotel);

        recyclerViewHotels = findViewById(R.id.recyclerViewHotels);
        editTextSearch = findViewById(R.id.editTextSearch);
        iconSort = findViewById(R.id.iconSort); // Lấy icon sort mới

        recyclerViewHotels.setLayoutManager(new LinearLayoutManager(this));
        hotelAdapter = new HotelAdapter(new ArrayList<>());
        recyclerViewHotels.setAdapter(hotelAdapter);

        hotelViewModel = new ViewModelProvider(this).get(HotelViewModel.class);

        hotelViewModel.getHotels().observe(this, hotels -> {
            if (hotels != null) {
                hotelAdapter.setHotelList(hotels);
                Log.d(TAG, "Loaded " + hotels.size() + " hotels");
            }
        });

        hotelViewModel.getError().observe(this, error -> {
            if (error != null) {
                Log.e(TAG, "Error: " + error);
            }
        });


        iconSort.setOnClickListener(v -> showSortBottomSheet());

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                hotelAdapter.filter(s.toString());
            }
        });
    }

    private void showSortBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_sort, null);
        bottomSheetDialog.setContentView(sheetView);

        TextView sortPriceAsc = sheetView.findViewById(R.id.sort_price_asc);
        TextView sortPriceDesc = sheetView.findViewById(R.id.sort_price_desc);
        TextView sortRatingAsc = sheetView.findViewById(R.id.sort_rating_asc);
        TextView sortRatingDesc = sheetView.findViewById(R.id.sort_rating_desc);

        sortPriceAsc.setOnClickListener(v -> {
            sortAndDisplay("price_asc");
            bottomSheetDialog.dismiss();
        });

        sortPriceDesc.setOnClickListener(v -> {
            sortAndDisplay("price_desc");
            bottomSheetDialog.dismiss();
        });

        sortRatingAsc.setOnClickListener(v -> {
            sortAndDisplay("rating_asc");
            bottomSheetDialog.dismiss();
        });

        sortRatingDesc.setOnClickListener(v -> {
            sortAndDisplay("rating_desc");
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    private void sortAndDisplay(String sortType) {
        hotelViewModel.sortHotels(sortType, new HotelRepository.HotelCallback() {
            @Override
            public void onSuccess(List<Hotel> hotels) {
                hotelAdapter.setHotelList(hotels);
                hotelAdapter.filter(editTextSearch.getText().toString());
            }

            @Override
            public void onFailure(String error) {
                Log.e(TAG, "Sort failed: " + error);
            }
        });
    }
}
