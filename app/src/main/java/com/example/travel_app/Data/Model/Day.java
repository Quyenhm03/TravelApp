package com.example.travel_app.Data.Model;

import java.io.Serializable;
import java.util.List;

public class Day implements Serializable {
    private String date;
    private List<Item> items;

    public Day() {
    }

    public Day(String date, List<Item> items) {
        this.date = date;
        this.items = items;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
