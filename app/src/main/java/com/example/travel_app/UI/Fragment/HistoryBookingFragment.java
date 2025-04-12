package com.example.travel_app.UI.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Data.Model.BookingCoach;
import com.example.travel_app.Data.Model.BookingFlight;
import com.example.travel_app.R;
import com.example.travel_app.Adapter.BookingAdapter;
import com.example.travel_app.UI.Activity.BookingCoachDetailActivity;
import com.example.travel_app.UI.Activity.BookingFlightDetailActivity;
import com.example.travel_app.ViewModel.HistoryBookingViewModel;

public class HistoryBookingFragment extends Fragment {
    private RecyclerView rcvBooking;
    private EditText edtSearch;
    private BookingAdapter adapter;
    private HistoryBookingViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_booking, container, false);

        rcvBooking = view.findViewById(R.id.rcv_booking);
        edtSearch = view.findViewById(R.id.edt_search);

        viewModel = new ViewModelProvider(this).get(HistoryBookingViewModel.class);

        adapter = new BookingAdapter();
        rcvBooking.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvBooking.setAdapter(adapter);

        // Quan sát danh sách đặt vé
        viewModel.getFilteredBookingList().observe(getViewLifecycleOwner(), bookings -> {
            if (bookings == null || bookings.isEmpty()) {
                Toast.makeText(getContext(), "Không có lịch sử đặt vé", Toast.LENGTH_SHORT).show();
            }
            adapter.submitList(bookings);
        });

        // Xử lý tìm kiếm
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.filterBookings(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        adapter.setOnItemClickListener(booking -> {
            Intent intent;
            if (booking instanceof BookingFlight) {
                intent = new Intent(getActivity(), BookingFlightDetailActivity.class);
                intent.putExtra("bookingFlight", (BookingFlight) booking);
            } else if (booking instanceof BookingCoach) {
                intent = new Intent(getActivity(), BookingCoachDetailActivity.class);
                intent.putExtra("bookingCoach", (BookingCoach) booking);
            } else {
                return;
            }
            startActivity(intent);
        });

        return view;
    }
}