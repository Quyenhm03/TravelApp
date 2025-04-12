package com.example.travel_app.UI.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Adapter.FlightAdapter;
import com.example.travel_app.Data.Model.FilterCriteria;
import com.example.travel_app.Data.Model.Flight;
import com.example.travel_app.Data.Model.SearchFlightInfo;
import com.example.travel_app.Data.Model.SortOption;
import com.example.travel_app.R;
import com.example.travel_app.UI.Activity.SearchFlightResultActivity;
import com.example.travel_app.ViewModel.SearchFlightViewModel;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;

public class SearchFlightResultFragment extends Fragment {
    private RecyclerView rcvSearchFlightResult;
    private FlightAdapter flightAdapter;
    private SearchFlightViewModel viewModel;
    private SearchFlightInfo searchFlightInfo;
    private TextView tvEmptyState;
    private AppCompatButton btnFilter, btnSort;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_flight_result, container, false);

        rcvSearchFlightResult = view.findViewById(R.id.rcv_flight_result);
        tvEmptyState = view.findViewById(R.id.tv_empty_state);
        btnFilter = view.findViewById(R.id.btn_filter);
        btnSort = view.findViewById(R.id.btn_sort);

        rcvSearchFlightResult.setLayoutManager(new LinearLayoutManager(getContext()));
        flightAdapter = new FlightAdapter(new ArrayList<>());
        rcvSearchFlightResult.setAdapter(flightAdapter);

        viewModel = new ViewModelProvider(requireActivity()).get(SearchFlightViewModel.class);

        if (getArguments() != null) {
            searchFlightInfo = (SearchFlightInfo) getArguments().getSerializable("searchFlightInfo");
            boolean isReturnFlight = getArguments().getBoolean("isReturnFlight", false);

            String departureAirportCode = isReturnFlight ? searchFlightInfo.getArrivalAirportCode() : searchFlightInfo.getDepartureAirportCode();
            String arrivalAirportCode = isReturnFlight ? searchFlightInfo.getDepartureAirportCode() : searchFlightInfo.getArrivalAirportCode();
            String date = isReturnFlight ? searchFlightInfo.getReturnDate() : searchFlightInfo.getDepartureDate();
            String seatType = searchFlightInfo.getSeatType();

            // Tìm kiếm chuyến bay
            viewModel.searchFlights(departureAirportCode, arrivalAirportCode, date, seatType);
        }

        // Quan sát danh sách chuyến bay đã lọc và sắp xếp
        viewModel.getFilteredFlightsLiveData().observe(getViewLifecycleOwner(), flights -> {
            if (flights != null && !flights.isEmpty()) {
                flightAdapter.setFlightList(flights);
                rcvSearchFlightResult.setVisibility(View.VISIBLE);
                tvEmptyState.setVisibility(View.GONE);
            } else {
                flightAdapter.setFlightList(new ArrayList<>());
                rcvSearchFlightResult.setVisibility(View.GONE);
                tvEmptyState.setVisibility(View.VISIBLE);
                tvEmptyState.setText("Không tìm thấy chuyến bay phù hợp");
            }
        });

        // Xử lý nút Lọc
        btnFilter.setOnClickListener(v -> showFilterDialog());

        // Xử lý nút Sắp xếp
        btnSort.setOnClickListener(v -> showSortDialog());

        flightAdapter.setOnItemClickListener(new FlightAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Flight flight) {
                FlightDetailFragment flightDetailFragment = new FlightDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("flight", flight);
                bundle.putSerializable("searchFlightInfo", searchFlightInfo);
                bundle.putBoolean("isReturnFlight", ((SearchFlightResultActivity) requireActivity()).isReturnFlight);
                flightDetailFragment.setArguments(bundle);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_flight_result, flightDetailFragment)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onViewDetailClick(Flight flight) {
                FlightDetailFragment flightDetailFragment = new FlightDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("flight", flight);
                bundle.putSerializable("searchFlightInfo", searchFlightInfo);
                bundle.putBoolean("isReturnFlight", ((SearchFlightResultActivity) requireActivity()).isReturnFlight);
                flightDetailFragment.setArguments(bundle);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_flight_result, flightDetailFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    private void showFilterDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_filter_flight);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);

        CheckBox cbVietnamAirlines = dialog.findViewById(R.id.cb_vietnam_airlines);
        CheckBox cbVietjet = dialog.findViewById(R.id.cb_vietjet);
        CheckBox cbBamboo = dialog.findViewById(R.id.cb_bamboo);
        RangeSlider priceRangeSlider = dialog.findViewById(R.id.price_range_slider);
        TextView tvPriceRange = dialog.findViewById(R.id.tv_price_range);
        Button btnApply = dialog.findViewById(R.id.btn_apply_filter);

        priceRangeSlider.addOnChangeListener((slider, value, fromUser) -> {
            List<Float> values = slider.getValues();
            String formattedMinPrice = String.format("%,.0f", values.get(0));
            String formattedMaxPrice = String.format("%,.0f", values.get(1));
            tvPriceRange.setText(String.format("%s - %s VNĐ", formattedMinPrice, formattedMaxPrice));
        });

        btnApply.setOnClickListener(v -> {
            FilterCriteria criteria = new FilterCriteria();
            List<String> airlines = new ArrayList<>();
            if (cbVietnamAirlines.isChecked()) airlines.add("Vietnam Airlines");
            if (cbVietjet.isChecked()) airlines.add("Vietjet Air");
            if (cbBamboo.isChecked()) airlines.add("Bamboo Airways");
            criteria.setAirlines(airlines);

            List<Float> priceRange = priceRangeSlider.getValues();
            criteria.setMinPrice(priceRange.get(0));
            criteria.setMaxPrice(priceRange.get(1));

            viewModel.setFilterCriteria(criteria);
            dialog.dismiss();
        });

        dialog.show();
    }
    private void showSortDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_sort_flight);

        RadioGroup rgSortOptions = dialog.findViewById(R.id.rg_sort_options);
        Button btnApply = dialog.findViewById(R.id.btn_apply_sort);

        btnApply.setOnClickListener(v -> {
            SortOption sortOption = null;
            int selectedId = rgSortOptions.getCheckedRadioButtonId();
            if (selectedId == R.id.rb_price_asc) {
                sortOption = SortOption.PRICE_ASC;
            } else if (selectedId == R.id.rb_price_desc) {
                sortOption = SortOption.PRICE_DESC;
            } else if (selectedId == R.id.rb_departure_early) {
                sortOption = SortOption.DEPARTURE_EARLY;
            } else if (selectedId == R.id.rb_departure_late) {
                sortOption = SortOption.DEPARTURE_LATE;
            }

            if (sortOption != null) {
                viewModel.setSortOption(sortOption);
            }
            dialog.dismiss();
        });

        dialog.show();
    }
}