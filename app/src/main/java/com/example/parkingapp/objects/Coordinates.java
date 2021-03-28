package com.example.parkingapp.objects;

public class Coordinates {
    private double latitude;
    private double longitude;

    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Coordinates(double[] coords) {
            this.latitude = coords[0];
            this.longitude = coords[1];

    }

    public Coordinates() {
        this(0,0);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String toString() {
        String res = "latitude=" + latitude + "; longitude=" + longitude;
        return res;
    }
}
