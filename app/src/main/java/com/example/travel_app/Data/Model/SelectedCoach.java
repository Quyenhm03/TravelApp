package com.example.travel_app.Data.Model;

import java.io.Serializable;

public class SelectedCoach implements Serializable {
    private Coach departureCoach;
    private Coach returnCoach;

    public SelectedCoach() {
    }

    public Coach getDepartureCoach() {
        return departureCoach;
    }

    public void setDepartureCoach(Coach departureCoach) {
        this.departureCoach = departureCoach;
    }

    public Coach getReturnCoach() {
        return returnCoach;
    }

    public void setReturnCoach(Coach returnCoach) {
        this.returnCoach = returnCoach;
    }
}