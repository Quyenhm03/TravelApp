package com.example.travel_app.Data.Model;

import com.example.travel_app.utils.Utils;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private String userId;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private Date dateOfBirth;

    public User(String userId, String fullName, String email, String phone, String address, Date dateOfBirth) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    public User(String userId, String fullName, String email, String phone, String address, String dateOfBirth) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.dateOfBirth = Utils.stringToDate(dateOfBirth, null);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
