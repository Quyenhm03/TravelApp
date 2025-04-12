package com.example.travel_app.Data.Model;

import java.io.Serializable;

public class SearchCoachInfo implements Serializable {
    private String departureDate;
    private String returnDate;
    private String departureCity;
    private String arrivalCity;
    private String departureStationName;
    private String arrivalStationName;
    private int seatCount;

    // Constructor cho chuyến một chiều
    public SearchCoachInfo( String departureDate,
                           String departureCity, String arrivalCity, String departureStationName,
                           String arrivalStationName, int seatCount) {
        this.departureDate = departureDate;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.departureStationName = departureStationName;
        this.arrivalStationName = arrivalStationName;
        this.seatCount = seatCount;
    }

    // Constructor cho chuyến khứ hồi
    public SearchCoachInfo(String departureDate, String returnDate,
                           String departureCity, String arrivalCity, String departureStationName,
                           String arrivalStationName, int seatCount) {
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.departureStationName = departureStationName;
        this.arrivalStationName = arrivalStationName;
        this.seatCount = seatCount;
    }

    // Getters

    public String getDepartureDate() {
        return departureDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public String getDepartureStationName() {
        return departureStationName;
    }

    public String getArrivalStationName() {
        return arrivalStationName;
    }

    public int getSeatCount() {
        return seatCount;
    }
}