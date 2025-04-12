package com.example.travel_app.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_app.Data.Model.BookingCoach;
import com.example.travel_app.Data.Model.BookingFlight;
import com.example.travel_app.Data.Model.BookingItem;
import com.example.travel_app.Data.Repository.BookingCoachRepository;
import com.example.travel_app.Data.Repository.BookingFlightRepository;

import java.util.ArrayList;
import java.util.List;

public class HistoryBookingViewModel extends ViewModel {
    private BookingFlightRepository flightRepository;
    private BookingCoachRepository coachRepository;
    private MutableLiveData<List<BookingItem>> bookingListLiveData = new MutableLiveData<>();
    private MutableLiveData<List<BookingItem>> filteredBookingListLiveData = new MutableLiveData<>();
    private String userId = "user123"; // Thay bằng userId thực tế

    public HistoryBookingViewModel() {
        flightRepository = new BookingFlightRepository();
        coachRepository = new BookingCoachRepository();
        fetchBookingHistory();
    }

    private void fetchBookingHistory() {
        // Lấy danh sách BookingFlight
        flightRepository.getBookingFlightHistory(userId).observeForever(flightBookings -> {
            List<BookingItem> combinedList = new ArrayList<>();
            if (flightBookings != null) {
                combinedList.addAll(flightBookings);
            }

            // Lấy danh sách BookingCoach
            coachRepository.getBookingCoachHistory(userId).observeForever(coachBookings -> {
                if (coachBookings != null) {
                    combinedList.addAll(coachBookings);
                }
                bookingListLiveData.setValue(combinedList);
                filteredBookingListLiveData.setValue(combinedList);
            });
        });
    }

    public LiveData<List<BookingItem>> getFilteredBookingList() {
        return filteredBookingListLiveData;
    }

    public void filterBookings(String query) {
        List<BookingItem> bookingList = bookingListLiveData.getValue();
        if (bookingList == null) return;

        List<BookingItem> filteredList = new ArrayList<>();
        for (BookingItem booking : bookingList) {
            if (booking instanceof BookingFlight) {
                BookingFlight flight = (BookingFlight) booking;
                if ( flight.getName().toLowerCase().contains(query.toLowerCase()) ||
                flight.getDepartureCity().toLowerCase().contains(query.toLowerCase()) ||
                flight.getArrivalCity().toLowerCase().contains(query.toLowerCase()) ||
                flight.getDate().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(booking);
                }
            } else if (booking instanceof BookingCoach) {
                BookingCoach coach = (BookingCoach) booking;
                if (coach.getName().toLowerCase().contains(query.toLowerCase()) ||
                        coach.getDepartureCity().toLowerCase().contains(query.toLowerCase()) ||
                        coach.getArrivalCity().toLowerCase().contains(query.toLowerCase()) ||
                        coach.getDate().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(booking);
                }
            }
        }
        filteredBookingListLiveData.setValue(filteredList);
    }
}