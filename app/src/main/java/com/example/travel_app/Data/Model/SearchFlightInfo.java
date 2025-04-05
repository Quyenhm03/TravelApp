package com.example.travel_app.Data.Model;

import java.io.Serializable;

public class SearchFlightInfo implements Serializable {
    private String departureAirportCode;
    private String arrivalAirportCode;
    private String departureDate;
    private String returnDate;
    private String departureCity;
    private String arrivalCity;
    private String departureAirportName;
    private String arrivalAirportName;
    private String seatType;
    private int adultCount;
    private int childCount;
    private int infantCount;

    public SearchFlightInfo(String departureAirportCode, String arrivalAirportCode, String departureDate, String departureCity, String arrivalCity, String departureAirportName, String arrivalAirportName, String seatType, int adultCount, int childCount, int infantCount) {
        this.departureAirportCode = departureAirportCode;
        this.arrivalAirportCode = arrivalAirportCode;
        this.departureDate = departureDate;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.departureAirportName = departureAirportName;
        this.arrivalAirportName = arrivalAirportName;
        this.seatType = seatType;
        this.adultCount = adultCount;
        this.childCount = childCount;
        this.infantCount = infantCount;
    }

    public SearchFlightInfo(String departureAirportCode, String arrivalAirportCode, String departureDate, String returnDate, String departureCity, String arrivalCity, String departureAirportName, String arrivalAirportName, String seatType, int adultCount, int childCount, int infantCount) {
        this.departureAirportCode = departureAirportCode;
        this.arrivalAirportCode = arrivalAirportCode;
        this.departureDate = departureDate;
        this.departureCity = departureCity;
        this.returnDate = returnDate;
        this.arrivalCity = arrivalCity;
        this.departureAirportName = departureAirportName;
        this.arrivalAirportName = arrivalAirportName;
        this.seatType = seatType;
        this.adultCount = adultCount;
        this.childCount = childCount;
        this.infantCount = infantCount;
    }

    public String getDepartureAirportCode() {
        return departureAirportCode;
    }

    public String getArrivalAirportCode() {
        return arrivalAirportCode;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public String getDepartureAirportName() {
        return departureAirportName;
    }

    public String getArrivalAirportName() {
        return arrivalAirportName;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public String getSeatType() {
        return seatType;
    }

    public int getAdultCount() {
        return adultCount;
    }

    public int getChildCount() {
        return childCount;
    }

    public int getInfantCount() {
        return infantCount;
    }

    public int getCustomerCount() {
        return adultCount + childCount + infantCount;
    }

    public String getReturnDate() {
        return returnDate;
    }
}
