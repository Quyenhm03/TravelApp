package com.example.travel_app.UI.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Adapter.ItineraryAdapter;
import com.example.travel_app.Data.Model.Itinerary;
import com.example.travel_app.R;
import com.example.travel_app.UI.Activity.ItineraryDetailActivity;
import com.example.travel_app.UI.Activity.SetUpInfoActivity;
import com.example.travel_app.ViewModel.CreateItineraryViewModel;

import java.util.ArrayList;

public class CreateItineraryFragment extends Fragment {
    private CreateItineraryViewModel viewModel;
    private ItineraryAdapter adapter;
    private RecyclerView rcvItinerary;
    private AppCompatButton btnMyItinerary, btnSharedItinerary;
    private CardView cvCreateItinerary;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_itinerary, container, false);

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(CreateItineraryViewModel.class);

        // Khởi tạo các view
        rcvItinerary = view.findViewById(R.id.rcv_itinerary);
        btnMyItinerary = view.findViewById(R.id.btn_my_itinerary);
        btnSharedItinerary = view.findViewById(R.id.btn_shared_itinerary);
        cvCreateItinerary = view.findViewById(R.id.cv_create_itinerary);

        // Thiết lập RecyclerView
        rcvItinerary.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ItineraryAdapter(new ArrayList<>(), new ItineraryAdapter.OnItineraryClickListener() {
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

        // Xử lý sự kiện click vào các nút
        btnMyItinerary.setOnClickListener(v -> {
            btnMyItinerary.setBackgroundResource(R.drawable.button_selected);
            btnMyItinerary.setTextColor(getResources().getColor(R.color.white));
            btnSharedItinerary.setBackgroundResource(android.R.color.white);
            btnSharedItinerary.setTextColor(Color.parseColor("#B8B8B8"));
            viewModel.loadMyItineraries();
        });

        btnSharedItinerary.setOnClickListener(v -> {
            btnSharedItinerary.setBackgroundResource(R.drawable.button_selected);
            btnSharedItinerary.setTextColor(getResources().getColor(R.color.white));
            btnMyItinerary.setBackgroundResource(android.R.color.white);
            btnMyItinerary.setTextColor(Color.parseColor("#B8B8B8"));
            viewModel.loadSharedItineraries();
        });

        // Quan sát dữ liệu từ ViewModel
        viewModel.getItineraryListLiveData().observe(getViewLifecycleOwner(), itineraries -> {
            if (itineraries != null) {
                adapter.updateList(itineraries);
            }
        });

        // Mặc định hiển thị "Lộ trình của bạn"
        btnMyItinerary.performClick();

        cvCreateItinerary.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SetUpInfoActivity.class);
            startActivity(intent);
        });

        return view;
    }
}