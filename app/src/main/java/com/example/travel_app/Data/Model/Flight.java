package com.example.travel_app.Data.Model;

import java.io.Serializable;
import java.util.List;

public class Flight implements Serializable {
    private int id;
    private String airline;
    private String airlineImgUrl;
    private String flightImg;
    private String flightNumber;
    private String departureDate;
    private String departureAirportCode;
    private String arrivalAirportCode;
    private String departureCity;
    private String arrivalCity;
    private String departureAirportName;
    private String arrivalAirportName;
    private String departureTime;
    private String arrivalTime;
    private String flightTime;
    private String seatType;
    private double price;
    private List<Seat> seats;

    public Flight() {}

    public Flight(int id, String airline, String airlineImgUrl, String flightImg, String flightNumber, String departureDate, String departureAirportCode, String arrivalAirportCode, String departureTime, String arrivalTime, String flightTime, String seatType, double price, List<Seat> seats) {
        this.id = id;
        this.airline = airline;
        this.airlineImgUrl = airlineImgUrl;
        this.flightImg = flightImg;
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

    public String getFlightImg() {
        return flightImg;
    }

    public void setFlightImg(String flightImg) {
        this.flightImg = flightImg;
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

    public String getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getArrivalAirportName() {
        return arrivalAirportName;
    }

    public void setArrivalAirportName(String arrivalAirportName) {
        this.arrivalAirportName = arrivalAirportName;
    }

    public String getDepartureAirportName() {
        return departureAirportName;
    }

    public void setDepartureAirportName(String departureAirportName) {
        this.departureAirportName = departureAirportName;
    }
}
