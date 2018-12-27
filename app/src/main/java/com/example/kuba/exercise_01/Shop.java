package com.example.kuba.exercise_01;

public class Shop {

    private String name;
    private String description;
    // TODO: verify type
    private int radius;
    private double latitude;
    private double longitude;

    public Shop() {
    }

    public Shop(String name, String description, int radius, double latitude, double longitude) {
        this.name = name;
        this.description = description;
        this.radius = radius;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
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
}
