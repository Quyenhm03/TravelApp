package com.example.travel_app.Data.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travel_app.Data.Model.Flight;
import com.example.travel_app.Data.Model.Seat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlightRepository {
    private DatabaseReference databaseReference;

    public FlightRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Flight");
    }

    public LiveData<List<Flight>> searchFlights(String departureAirportCode, String arrivalAirportCode, String departureDate, String seatType) {
        MutableLiveData<List<Flight>> flightLiveData = new MutableLiveData<>();

        Query query = databaseReference.orderByChild("departureAirportCode").equalTo(departureAirportCode);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Flight> flightList = new ArrayList<>();
                for(DataSnapshot flightSnapshot : snapshot.getChildren()) {
                    Flight flight = flightSnapshot.getValue(Flight.class);
                    if(flight != null && flight.getArrivalAirportCode().equals(arrivalAirportCode) &&
                        flight.getDepartureDate().equals(departureDate) && flight.getSeatType().equals(seatType)) {
                        flightList.add(flight);
                    }
                }
                flightLiveData.setValue(flightList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                flightLiveData.setValue(null);
            }
        });
        return flightLiveData;
    }

    public LiveData<Flight> getFlightSeats(int flightId) {
        MutableLiveData<Flight> flightLiveData = new MutableLiveData<>();
        databaseReference.child(String.valueOf(flightId)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Flight flight = snapshot.getValue(Flight.class);
                if (flight != null) {
                    List<Seat> seats = new ArrayList<>();
                    DataSnapshot seatsSnapshot = snapshot.child("seats");
                    for (DataSnapshot seatSnapshot : seatsSnapshot.getChildren()) {
                        Seat seat = seatSnapshot.getValue(Seat.class);
                        if (seat != null) {
                            seats.add(seat);
                        }
                    }
                    flight.setSeats(seats);
                    flightLiveData.setValue(flight);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return flightLiveData;
    }

    public LiveData<List<Flight>> getFirstTenFlights() {
        MutableLiveData<List<Flight>> flightLiveData = new MutableLiveData<>();

        databaseReference.limitToFirst(10).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Flight> flightList = new ArrayList<>();
                for (DataSnapshot flightSnapshot : snapshot.getChildren()) {
                    Flight flight = flightSnapshot.getValue(Flight.class);
                    if (flight != null) {
                        flightList.add(flight);
                    }
                }
                flightLiveData.setValue(flightList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FlightRepository", "Failed to load flights: " + error.getMessage());
                flightLiveData.setValue(null);
            }
        });
        return flightLiveData;
    }
}
