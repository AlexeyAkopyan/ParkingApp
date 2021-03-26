package com.example.parkingapp;

import com.example.parkingapp.objects.Parking;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class ParkingPlace {
    private  LatLng loc;
    private  String address;
    private  List<Integer> working_hours;
    private  Integer number_free_places;


    public ParkingPlace(double lat, double lng, String address,
                        List<Integer> working_hours, Integer number_free_places){
        this.loc = new LatLng(lat, lng);
        this.address = address;
        this.working_hours = working_hours;
        this.number_free_places = number_free_places;
    }

    public ParkingPlace(Parking parking) {
        this.loc = new LatLng(parking.getCoordinates().getLatitude(), parking.getCoordinates().getLongitude());
        this.address = parking.getAddress().replace('\"',' ').trim();
        this.working_hours = parking.getWorkingHours();
        this.number_free_places = parking.getAvailable();
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

    public Integer getNumberFreePlaces(){
        return number_free_places;
    };


}
