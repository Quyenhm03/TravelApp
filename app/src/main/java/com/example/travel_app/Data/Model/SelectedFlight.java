package com.example.travel_app.Data.Model;

import java.io.Serializable;

public class SelectedFlight implements Serializable {
    private Flight departureFlight, returnFlight;

    public SelectedFlight() {
    }

    public Flight getDepartureFlight() {
        return departureFlight;
    }

    public void setDepartureFlight(Flight departureFlight) {
        this.departureFlight = departureFlight;
    }

    public Flight getReturnFlight() {
        return returnFlight;
    }

    public void setReturnFlight(Flight returnFlight) {
        this.returnFlight = returnFlight;
    }
}
