package com.example.parkingapp;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class ParkingPlace {
    LatLng loc;
    String address;
    List<Integer> working_hours;
    Integer nfree_places;


    public ParkingPlace(double lat, double lng, String address, List<Integer> working_hours){
        this.loc = new LatLng(lat, lng);
        this.address = address;
        this.working_hours = working_hours;
        this.nfree_places = 20;
    }

    public LatLng getLoc()
    {
        return loc;
    }

    public String getAddress()
    {
        return address;
    }

    public List<Integer> getWorkingHours()
    {
        return working_hours;
    }

    public Integer getNFreePlaces(){
        return nfree_places;
    };


}
