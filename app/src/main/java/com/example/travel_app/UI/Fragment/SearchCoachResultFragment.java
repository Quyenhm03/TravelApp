package com.example.travel_app.UI.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Adapter.CoachAdapter;
import com.example.travel_app.Data.Model.Coach;
import com.example.travel_app.Data.Model.FilterCoach;
import com.example.travel_app.Data.Model.SearchCoachInfo;
import com.example.travel_app.Data.Model.SortOption;
import com.example.travel_app.R;
import com.example.travel_app.ViewModel.SearchCoachViewModel;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;

public class SearchCoachResultFragment extends Fragment {
    private RecyclerView rcvSearchCoachResult;
    private CoachAdapter coachAdapter;
    private SearchCoachViewModel viewModel;
    private SearchCoachInfo searchCoachInfo;
    private TextView tvEmptyState;
    private AppCompatButton btnFilter, btnSort;
    private boolean isReturnCoach;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_coach_result, container, false);

        rcvSearchCoachResult = view.findViewById(R.id.rcv_coach_result);
        tvEmptyState = view.findViewById(R.id.tv_empty_state_coach);
        btnFilter = view.findViewById(R.id.btn_filter);
        btnSort = view.findViewById(R.id.btn_sort);

        rcvSearchCoachResult.setLayoutManager(new LinearLayoutManager(getContext()));
        coachAdapter = new CoachAdapter(new ArrayList<>());
        rcvSearchCoachResult.setAdapter(coachAdapter);

        viewModel = new ViewModelProvider(requireActivity()).get(SearchCoachViewModel.class);

        if (getArguments() != null) {
            searchCoachInfo = (SearchCoachInfo) getArguments().getSerializable("searchCoachInfo");
            isReturnCoach = getArguments().getBoolean("isReturnCoach", false);

            String departureStationName = isReturnCoach ? searchCoachInfo.getArrivalStationName() : searchCoachInfo.getDepartureStationName();
            String arrivalStationName = isReturnCoach ? searchCoachInfo.getDepartureStationName() : searchCoachInfo.getArrivalStationName();
            String date = isReturnCoach ? searchCoachInfo.getReturnDate() : searchCoachInfo.getDepartureDate();

            // Tìm kiếm chuyến xe
            viewModel.searchCoaches(departureStationName, arrivalStationName, date);
        }

        // Quan sát danh sách chuyến xe đã lọc và sắp xếp
        viewModel.getFilteredCoachesLiveData().observe(getViewLifecycleOwner(), coaches -> {
            if (coaches != null && !coaches.isEmpty()) {
                coachAdapter.setCoachList(coaches);
                rcvSearchCoachResult.setVisibility(View.VISIBLE);
                tvEmptyState.setVisibility(View.GONE);
            } else {
                coachAdapter.setCoachList(new ArrayList<>());
                rcvSearchCoachResult.setVisibility(View.GONE);
                tvEmptyState.setVisibility(View.VISIBLE);
                tvEmptyState.setText("Không tìm thấy chuyến xe phù hợp");
            }
        });

        // Xử lý nút Lọc
        btnFilter.setOnClickListener(v -> showFilterDialog());

        // Xử lý nút Sắp xếp
        btnSort.setOnClickListener(v -> showSortDialog());

        // Xử lý sự kiện click vào chuyến xe
        coachAdapter.setOnItemClickListener(new CoachAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Coach coach) {
                CoachDetailFragment coachDetailFragment = new CoachDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("coach", coach);
                bundle.putSerializable("searchCoachInfo", searchCoachInfo);
                bundle.putBoolean("isReturnCoach", isReturnCoach);
                coachDetailFragment.setArguments(bundle);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_coach_result, coachDetailFragment)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onViewDetailClick(Coach coach) {
                CoachDetailFragment coachDetailFragment = new CoachDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("coach", coach);
                bundle.putSerializable("searchCoachInfo", searchCoachInfo);
                bundle.putBoolean("isReturnCoach", isReturnCoach);
                coachDetailFragment.setArguments(bundle);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_coach_result, coachDetailFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    private void showFilterDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_filter_coach);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);

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
            FilterCoach criteria = new FilterCoach();
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
        dialog.setContentView(R.layout.dialog_sort_coach);

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