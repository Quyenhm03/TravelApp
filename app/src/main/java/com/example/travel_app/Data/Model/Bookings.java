package com.example.travel_app.Data.Model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Bookings implements Serializable {
    private int id;
    private Date check_in;
    private Date check_out;
    private float total_price;
    private int hotel_id;
    private int user_id;
    private int room_id;
    private int payment_id;

    public Bookings() {}

    public Bookings(int id, Date check_in, Date check_out, float total_price, int hotel_id, int user_id, int room_id, int payment_id) {
        this.id = id;
        this.check_in = check_in;
        this.check_out = check_out;
        this.total_price = total_price;
        this.hotel_id = hotel_id;
        this.user_id = user_id;
        this.room_id = room_id;
        this.payment_id = payment_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCheck_in() {
        return check_in;
    }

    public void setCheck_in(Date check_in) {
        this.check_in = check_in;
    }

    public Date getCheck_out() {
        return check_out;
    }

    public void setCheck_out(Date check_out) {
        this.check_out = check_out;
    }

    public float getTotal_price() {
        return total_price;
    }

    public void setTotal_price(float total_price) {
        this.total_price = total_price;
    }

    public int getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(int hotel_id) {
        this.hotel_id = hotel_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }
}
