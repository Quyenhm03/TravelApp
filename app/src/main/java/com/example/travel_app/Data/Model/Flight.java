package com.example.travel_app.Data.Model;

import java.io.Serializable;
import java.util.List;

public class Flight implements Serializable {
    private int id;
    private String airline;
    private String airlineImgUrl;
    private String flightNumber; // Số hiệu chuyến bay
    private String departureDate;
    private String departureAirportCode;
    private String arrivalAirportCode;
    private String departureTime;
    private String arrivalTime;
    private String flightTime;
    private String seatType;
    private double price;
    private List<Seat> seats;

    public Flight() {}

    public Flight(int id, String airline, String airlineImgUrl, String flightNumber, String departureDate, String departureAirportCode, String arrivalAirportCode, String departureTime, String arrivalTime, String flightTime, String seatType, double price, List<Seat> seats) {
        this.id = id;
        this.airline = airline;
        this.airlineImgUrl = airlineImgUrl;
        this.flightNumber = flightNumber;
        this.departureDate = departureDate;
        this.departureAirportCode = departureAirportCode;
        this.arrivalAirportCode = arrivalAirportCode;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.flightTime = flightTime;
        this.seatType = seatType;
        this.price = price;
        this.seats = seats;
    }

    public int getId() {
        return id;
    }

    public String getAirline() {
        return airline;
    }

    public String getAirlineImgUrl() {
        return airlineImgUrl;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public String getDepartureAirportCode() {
        return departureAirportCode;
    }

    public String getArrivalAirportCode() {
        return arrivalAirportCode;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getFlightTime() {
        return flightTime;
    }

    public double getPrice() {
        return price;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }
}
