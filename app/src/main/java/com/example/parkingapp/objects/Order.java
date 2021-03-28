package com.example.parkingapp.objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.util.Date;

public class Order {
    private Long id;
    private Long parkingId;
    private String carNumber;
    private Date start;
    private Date finish;
    private String paymentInfo;

    public Order(Long id, Long parkingId, String carNumber,
                 Long start, Long finish, String paymentInfo) {
        this.id = id;
        this.parkingId = parkingId;
        this.carNumber = carNumber;
        this.start = new Date(start);
        this.finish = new Date(finish);
        this.paymentInfo = paymentInfo;
    }

    public Order(Long parkingId, String carNumber,
                 Long start, Long finish, Long paymentInfo) {
        this.id = -new Date().getTime();
        this.parkingId = parkingId;
        this.carNumber = carNumber;
        this.start = new Date(start);
        this.finish = new Date(finish);
        this.paymentInfo = paymentInfo.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        if (this.id == null)
            this.id = id;
        else if (this.id < 0) {
            this.id = id;
        }
    }

    public Long getParkingId() {
        return parkingId;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public Date getStart() {
        return start;
    }

    public Date getFinish() {
        return finish;
    }

    public String getPaymentInfo() {
        return paymentInfo;
    }

    public String toString() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(this);
    }

    public static Order fromJson(String json) {
        try {
            return new Gson().fromJson(json, Order.class);
        } catch (JsonSyntaxException e) {
            System.out.println("Illegal string format");
        }
        return null;
    }

    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        if (!order.getParkingId().equals(this.parkingId)) return false;
        if (!order.getCarNumber().equals(this.carNumber)) return false;
        if (!order.getStart().equals(this.start)) return false;
        if (!order.getFinish().equals(this.finish)) return false;
        //if (!order.getPaymentInfo().equals(this.paymentInfo)) return false;
        return true;
    }
}
