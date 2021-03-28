package com.example.parkingapp.client;

import com.example.parkingapp.objects.Parking;

import java.util.ArrayList;
import java.util.List;

public class ParkingService {
    private List<Parking> parkingList;

    public ParkingService() {
        this.parkingList = new ArrayList<>();
    }

    public void updateParking(Parking parking) {
        for (Parking p: parkingList) {
            if (p.getId().equals(parking.getId())) {
                parkingList.remove(p);
                parkingList.add(parking);
                break;
            }
        }
    }

    public List<Parking> getParkingList() {
        return parkingList;
    }

    public void setParkingList(List<Parking> parkingList) {
        this.parkingList = parkingList;
    }
}
