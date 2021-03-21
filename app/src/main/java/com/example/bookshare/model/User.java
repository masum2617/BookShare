package com.example.bookshare.model;

import java.io.Serializable;

public class User implements Serializable {

    public double latitude;
    public double longitude;
    public String title;
    public int bookCount;
    public int sellBookCount;
    public int rentBookCount;
    public String phone;
    public String email;

    public User() {
    }

    public User(double latitude, double longitude, String title, int bookCount) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.bookCount = bookCount;
    }

    public User(double latitude, double longitude, String title, int bookCount, int sellBookCount, int rentBookCount) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.bookCount = bookCount;
        this.sellBookCount = sellBookCount;
        this.rentBookCount = rentBookCount;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBookCount() {
        return bookCount;
    }

    public void setBookCount(int bookCount) {
        this.bookCount = bookCount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
