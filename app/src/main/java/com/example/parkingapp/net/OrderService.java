package com.example.parkingapp.net;

import com.example.parkingapp.objects.Constants;
import com.example.parkingapp.objects.Order;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    List<Order> orderList;

    public OrderService() {
        orderList = new ArrayList<>();
    }

    public void add(Order order) {
        this.orderList.add(order);
    }

    public void remove(Order order) {
        this.orderList.remove(order);
    }

    public void updateOrderId(Long oldId, Long newId) {
        for (Order order : orderList) {
            if (order.getId().equals(oldId))
                order.setId(newId);
        }
    }

    public List<Order> getOrderList() {
        return this.orderList;
    }

    public File getQR(Order order) throws Exception {
        Long id = order.getId();
        if (id < 0) {
            throw new Exception("Order is not confirmed");
        }
        File dir = new File(Constants.PATH_TO_QR);
        File qrFile = new File(dir, "qr" + id + ".svg");
        if (!qrFile.exists())
            throw new FileNotFoundException("QR code not found: get QR again from server");
        return qrFile;
    }
}