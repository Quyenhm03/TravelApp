package com.example.travel_app.UI.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.travel_app.Adapter.Itinerary.ItineraryAdapter;
import com.example.travel_app.Data.Model.Itinerary.Itinerary;
import com.example.travel_app.R;
import com.example.travel_app.UI.Activity.Itinerary.ItineraryDetailActivity;
import com.example.travel_app.UI.Activity.Itinerary.SetUpInfoActivity;
import com.example.travel_app.ViewModel.Itinerary.CreateItineraryViewModel;
import com.example.travel_app.ViewModel.Itinerary.ImageViewModel;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

public class CreateItineraryFragment extends Fragment {
    private CreateItineraryViewModel viewModel;
    private ImageViewModel imageViewModel;
    private ItineraryAdapter adapter;
    private RecyclerView rcvItinerary;
    private AppCompatButton btnMyItinerary, btnSharedItinerary;
    private CardView cvCreateItinerary, cvMenu;
    private EditText edtSearch;
    private TextView txtTitleSearchResult, txtNoResults;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_itinerary, container, false);

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(CreateItineraryViewModel.class);
        imageViewModel = new ViewModelProvider(requireActivity()).get(ImageViewModel.class);

        // Khởi tạo các view
        rcvItinerary = view.findViewById(R.id.rcv_itinerary);
        btnMyItinerary = view.findViewById(R.id.btn_my_itinerary);
        btnSharedItinerary = view.findViewById(R.id.btn_shared_itinerary);
        cvCreateItinerary = view.findViewById(R.id.cv_create_itinerary);
        cvMenu = view.findViewById(R.id.cv_menu);
        edtSearch = view.findViewById(R.id.edt_search);
        txtTitleSearchResult = view.findViewById(R.id.txt_title_search_result);
        txtNoResults = view.findViewById(R.id.txt_no_results);

        // Thiết lập RecyclerView
        rcvItinerary.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ItineraryAdapter(new ArrayList<>(), imageViewModel, new ItineraryAdapter.OnItineraryClickListener() {
            @Override
            public void onItineraryClick(Itinerary itinerary) {
                Intent intent = new Intent(requireContext(), ItineraryDetailActivity.class);
                intent.putExtra("itinerary", itinerary);
                startActivity(intent);
            }

            @Override
            public void onShareClick(Itinerary itinerary) {
                viewModel.shareItinerary(itinerary);
            }
        });
        rcvItinerary.setAdapter(adapter);

        // Đăng ký một Observer duy nhất cho danh sách hành trình hiện tại
        viewModel.getCurrentItineraries().observe(getViewLifecycleOwner(), itineraries -> {
            adapter.updateList(itineraries != null ? itineraries : new ArrayList<>());
            txtNoResults.setVisibility(itineraries == null || itineraries.isEmpty() ? View.VISIBLE : View.GONE);
        });

        // Xử lý sự kiện click vào các nút
        btnMyItinerary.setOnClickListener(v -> {
            btnMyItinerary.setBackgroundResource(R.drawable.button_selected);
            btnMyItinerary.setTextColor(getResources().getColor(R.color.white));
            btnSharedItinerary.setBackgroundResource(android.R.color.white);
            btnSharedItinerary.setTextColor(Color.parseColor("#B8B8B8"));
            viewModel.switchToMyItineraries();
            edtSearch.setText(""); // Xóa nội dung tìm kiếm
        });

        btnSharedItinerary.setOnClickListener(v -> {
            btnSharedItinerary.setBackgroundResource(R.drawable.button_selected);
            btnSharedItinerary.setTextColor(getResources().getColor(R.color.white));
            btnMyItinerary.setBackgroundResource(android.R.color.white);
            btnMyItinerary.setTextColor(Color.parseColor("#B8B8B8"));
            viewModel.switchToSharedItineraries();
            edtSearch.setText(""); // Xóa nội dung tìm kiếm
        });

        cvCreateItinerary.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SetUpInfoActivity.class);
            startActivity(intent);
        });

        // Xử lý tìm kiếm
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.searchItineraries(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Quan sát trạng thái tìm kiếm
        viewModel.getIsSearchingLiveData().observe(getViewLifecycleOwner(), isSearching -> {
            cvMenu.setVisibility(isSearching ? View.GONE : View.VISIBLE);
            txtTitleSearchResult.setVisibility(isSearching ? View.VISIBLE : View.GONE);
        });

        // Mặc định hiển thị "Lộ trình của bạn"
        btnMyItinerary.performClick();

        return view;
    }
}