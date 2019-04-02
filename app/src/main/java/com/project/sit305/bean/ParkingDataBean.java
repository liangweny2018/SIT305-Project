package com.project.sit305.bean;

import java.io.Serializable;

public class ParkingDataBean implements Serializable {

    String name;
    double price;
    double distance;
    double longitude;
    double latitude;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "ParkingDataBean{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", distance=" + distance +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
