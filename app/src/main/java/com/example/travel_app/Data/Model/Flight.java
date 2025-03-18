package com.example.travel_app.Data.Model;

import java.io.Serializable;

public class Flight implements Serializable {
    private int id;
    private String airline;
    private String airlineImgUrl;
    private String flightNumber; // Số hiệu chuyến bay (VD: VN123)
    private String departureDate;
    private String departureAirportCode;
    private String arrivalAirportCode;
    private String departureTime;
    private String arrivalTime;
    private double price;

    public Flight() {}

    public Flight(int id, String airline, String airlineImgUrl, String flightNumber, String departureDate, String departureAirportCode, String arrivalAirportCode, String departureTime, String arrivalTime, double price) {
        this.id = id;
        this.airline = airline;
        this.airlineImgUrl = airlineImgUrl;
        this.flightNumber = flightNumber;
        this.departureDate = departureDate;
        this.departureAirportCode = departureAirportCode;
        this.arrivalAirportCode = arrivalAirportCode;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getAirlineImgUrl() {
        return airlineImgUrl;
    }

    public void setAirlineImgUrl(String airlineImgUrl) {
        this.airlineImgUrl = airlineImgUrl;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getDepartureAirportCode() {
        return departureAirportCode;
    }

    public void setDepartureAirportCode(String departureAirportCode) {
        this.departureAirportCode = departureAirportCode;
    }

    public String getArrivalAirportCode() {
        return arrivalAirportCode;
    }

    public void setArrivalAirportCode(String arrivalAirportCode) {
        this.arrivalAirportCode = arrivalAirportCode;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
