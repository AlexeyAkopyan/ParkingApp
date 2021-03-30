package com.example.parkingapp;

import com.example.parkingapp.objects.Parking;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class ParkingPlace {
    private Long id;
    private  LatLng loc;
    private  String address;
    private  List<Integer> working_hours;
    private  Integer number_free_places;
    private static List<String> addresses = new ArrayList<String>(){
        {
            add("пр. Пятилеток, 1, лит. А");
            add("наб. р. Мойки, 77");
            add("ул. Рубинштейна, 11");
            add("пл. Восстания");
            add("Клубный пер., 15 корпус 2");
            add("ул. Профессора Попова, 42");
            add("Кирочная ул., 65");
            add("Малодетскосельский пр., 25/12");
            add("Литовская ул., 1");
            add("Дегтярный пер., 11Б");
        }};


    public ParkingPlace(double lat, double lng, String address,
                        List<Integer> working_hours, Integer number_free_places){
        this.loc = new LatLng(lat, lng);
        this.address = address;
        if (working_hours != null){
            this.working_hours = working_hours;}
        else{
            this.working_hours = working_hours;}
        this.number_free_places = number_free_places;
    }

    public ParkingPlace(Parking parking){
        this.id = parking.getId();
        this.loc = new LatLng(parking.getCoordinates().getLatitude(), parking.getCoordinates().getLongitude());
        this.address = addresses.get(id.intValue());
        this.working_hours = parking.getWorkingHours();
        this.number_free_places = parking.getAvailable();
    }

    public Long getId() {
        return id;
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
