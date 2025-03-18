package com.example.travel_app.Data.Model;

import java.io.Serializable;

public class Airport implements Serializable {
    private int id;
    private String name;
    private String city;
    private String country;
    private String airportCode;

    public Airport() {}

    public Airport(int id, String name, String city, String country, String airportCode) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.country = country;
        this.airportCode = airportCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAirportAddress(){
        return city + ", " + country;
    }

    public String getAirportNameCountry(){
        return name + ", " + country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }
}
