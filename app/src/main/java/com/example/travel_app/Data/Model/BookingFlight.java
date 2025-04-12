package com.example.travel_app.Data.Model;

import java.io.Serializable;
import java.util.List;

public class BookingFlight implements BookingItem, Serializable {
    private String id;
    private String userId;
    private String departureCity;
    private String arrivalCity;
    private Flight departureFlight, returnFlight;
    private List<Passenger> passengerList;
    private List<String> selectedSeatsDeparture, selectedSeatsReturn;
    private double totalAmount;
    private String status;
    private Payment payment;
    private long departureTimestamp;
    private long returnTimestamp;

    public BookingFlight() {
    }

    public BookingFlight(String id, String userId, Flight departureFlight, Flight returnFlight, List<Passenger> passengerList,
                         List<String> selectedSeatsDeparture, List<String> selectedSeatsReturn, double totalAmount, String status) {
        this.id = id;
        this.userId = userId;
        this.departureFlight = departureFlight;
        this.returnFlight = returnFlight;
        this.passengerList = passengerList;
        this.selectedSeatsDeparture = selectedSeatsDeparture;
        this.selectedSeatsReturn = selectedSeatsReturn;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public long getDepartureTimestamp() {
        return departureTimestamp;
    }

    public void setDepartureTimestamp(long departureTimestamp) {
        this.departureTimestamp = departureTimestamp;
    }

    public long getReturnTimestamp() {
        return returnTimestamp;
    }

    public void setReturnTimestamp(long returnTimestamp) {
        this.returnTimestamp = returnTimestamp;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public List<Passenger> getPassengerList() {
        return passengerList;
    }

    public void setPassengerList(List<Passenger> passengerList) {
        this.passengerList = passengerList;
    }

    public List<String> getSelectedSeatsDeparture() {
        return selectedSeatsDeparture;
    }

    public void setSelectedSeatsDeparture(List<String> selectedSeatsDeparture) {
        this.selectedSeatsDeparture = selectedSeatsDeparture;
    }

    public List<String> getSelectedSeatsReturn() {
        return selectedSeatsReturn;
    }

    public void setSelectedSeatsReturn(List<String> selectedSeatsReturn) {
        this.selectedSeatsReturn = selectedSeatsReturn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return departureFlight != null ? departureFlight.getAirline() : "Unknown Airline";
    }

    @Override
    public String getRoute() {
        return this.departureCity + " - " + this.arrivalCity;
    }

    @Override
    public String getDate() {
        return departureFlight != null ? departureFlight.getDepartureDate() : "N/A";
    }

    @Override
    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String getImageUrl() {
        return departureFlight != null ? departureFlight.getFlightImg() : "";
    }
}