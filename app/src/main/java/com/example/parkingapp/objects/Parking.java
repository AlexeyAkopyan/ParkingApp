package com.example.parkingapp.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class Parking {
    private Long id;
    private Coordinates coordinates;
    private String info;
    private int capacity;
    private int available;

    public Parking(long id, Coordinates coordinates, String info,
                   int capacity, int available) {
        this.id = id;
        this.coordinates = coordinates;
        this.info = info;
        this.capacity = capacity;
        this.available = available;
    }

    public Long getId() { return id; }

    public void setId(Long id) {
        if (this.id == null)
            this.id = id;
    }

    public Coordinates getCoordinates() { return coordinates; }

    public String getInfo() { return info; }

    public int getCapacity() { return capacity; }

    public int getAvailable() { return available; }

    public String toString() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(this);
    }

    public static Parking fromJson(String json) {
        try {
            return new Gson().fromJson(json, Parking.class);
        } catch (JsonSyntaxException e) {
            System.out.println("Illegal string format");
        }
        return null;
    }
}
