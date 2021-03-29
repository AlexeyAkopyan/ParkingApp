package com.example.parkingapp;

import android.util.Log;

import com.example.parkingapp.objects.Parking;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class ParkingPlace {
    private static Integer Count = 0;
    private Long Id;
    private  LatLng loc;
    private  String address;
    private  List<Integer> working_hours;
    private  Integer number_free_places;


    public ParkingPlace(Long id, double lat, double lng, String address,
                        List<Integer> working_hours, Integer number_free_places){
        this.Id = id;
        this.loc = new LatLng(lat, lng);
        this.address = address;
        if (working_hours != null){
            this.working_hours = working_hours;}
        else{
            this.working_hours = working_hours;}
        this.number_free_places = number_free_places;

    }

    public Long getId() {
        return Id;
    }

    public ParkingPlace(Parking parking){
        this.Id = parking.getId();
        this.loc = new LatLng(parking.getCoordinates().getLatitude(), parking.getCoordinates().getLongitude());
        this.address = parking.getAddress();
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
