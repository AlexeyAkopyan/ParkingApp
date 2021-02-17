package com.example.parkingapp;

import com.google.android.gms.maps.model.LatLng;

public class ParkingPlace {
    LatLng loc;
    String address;
    String working_hours;
    String marker_title;

    public ParkingPlace(double lat, double lng, String address, String working_hours){
        this.loc = new LatLng(lat, lng);
        this.address = address;
        this.working_hours = working_hours;
    }

    public LatLng getLoc()
    {
        return loc;
    }

    public String getAddress()
    {
        return address;
    }

    public String getWorkingHours()
    {
        return working_hours;
    }


}
