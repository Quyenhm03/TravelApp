package com.example.travel_app.Data.Model;

import java.io.Serializable;
import java.util.List;

public class BookingCoach implements BookingItem, Serializable {
    private String id;
    private String userId;
    private String departureCity;
    private String arrivalCity;
    private Coach departureCoach, returnCoach;
    private List<Passenger> passengerList;
    private List<String> selectedSeatsDeparture, selectedSeatsReturn;
    private double totalAmount;
    private String status;
    private Payment payment;
    private long departureTimestamp;
    private long returnTimestamp;

    public BookingCoach() {}

    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return getDepartureCoach().getCompanyName();
    }

    @Override
    public String getRoute() {
        return getDepartureCity() + " - " + getArrivalCity();
    }

    @Override
    public String getDate() {
        return getDepartureCoach().getDepartureDate();
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

    @Override
    public String getImageUrl() {
        return departureCoach != null ? departureCoach.getCoachImg() : "";
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
}
