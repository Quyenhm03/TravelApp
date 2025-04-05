package com.example.travel_app.Data.Model;

import java.io.Serializable;
import java.util.List;

public class BookingFlight implements Serializable {
    private String id;
    private String userId;
    private Flight departureFlight, returnFlight;
    private List<Passenger> passengerList;
    private List<String> selectedSeatsDeparture, selectedSeatsReturn;
    private double totalAmount;
    private String status;
    private Payment payment;

    public BookingFlight() {
    }

    public BookingFlight(String id, String userId, Flight departureFlight, Flight returnFlight, List<Passenger> passengerList, List<String> selectedSeatsDeparture, List<String> selectedSeatsReturn, double totalAmount, String status) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
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
}
