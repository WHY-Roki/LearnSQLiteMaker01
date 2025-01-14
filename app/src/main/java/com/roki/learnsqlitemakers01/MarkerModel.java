package com.roki.learnsqlitemakers01;

public class MarkerModel {
    private String name;
    private String address;
    private double latitude;
    private double longitude;

    public MarkerModel(String name, String address, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
