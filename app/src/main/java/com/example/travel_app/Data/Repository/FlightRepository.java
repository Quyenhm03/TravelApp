package com.example.travel_app.Data.Repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travel_app.Data.Model.Flight;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FlightRepository {
    private DatabaseReference databaseReference;

    public FlightRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference("flight");
    }

    public LiveData<List<Flight>> searchFlights(String departureAirportCode, String arrivalAirportCode, String departureDate) {
        MutableLiveData<List<Flight>> flightLiveData = new MutableLiveData<>();

        Query query = databaseReference.orderByChild("departureAirportCode").equalTo(departureAirportCode);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Flight> flightList = new ArrayList<>();
                for(DataSnapshot flightSnapshot : snapshot.getChildren()) {
                    Flight flight = flightSnapshot.getValue(Flight.class);
                    if(flight != null && flight.getArrivalAirportCode().equals(arrivalAirportCode) &&
                        flight.getDepartureDate().equals(departureDate)) {
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
}
