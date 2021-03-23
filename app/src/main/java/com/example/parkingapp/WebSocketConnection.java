package com.example.parkingapp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.parkingapp.objects.MessageGenerator;
import com.example.parkingapp.objects.Order;
import com.example.parkingapp.objects.Parking;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.List;

public class WebSocketConnection {
    private OkHttpClient client ;
    private Request request;
    private WebSocket webSocket;
    private List<Parking> pList;

    private WebSocketListener listener = new WebSocketListener() {
        @Override
        public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
            super.onOpen(webSocket, response);
            webSocket.send(MessageGenerator.GET_ALL_PARKINGS);
            System.out.println("Connection established");
        }

        public void onMessage(WebSocket webSocket, String text) {
            System.out.println("Message received:" + text);
            System.out.println(text.startsWith(MessageGenerator.SEND_ALL_PARKINGS));
            if (text.startsWith(MessageGenerator.SEND_ALL_PARKINGS)) {
                System.out.println("Get all parkings:");
                pList = getAllParkings(text);
                for (Parking parking:pList) {
                    System.out.println(parking.toString());
                }
            }
            if (text.startsWith(MessageGenerator.SEND_PARKING)) {
                Parking parking = getParking(text);
            }
            if (text.startsWith(MessageGenerator.MESSAGE)) {
                getMessage(text);
            }
        }

        public void onClosed(WebSocket webSocket, int code, String reason){
            System.out.println("Connection closed");
            // здесь может быть ваш код
        }

        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            System.out.println("Connection failed");
            System.out.println(response);
            System.out.println(t);
            // здесь может быть ваш код
        }
    };

    public WebSocketConnection(String url) {
        client = new OkHttpClient();
        request = new Request.Builder().url(url).build();
    }

    public void init() {
        webSocket = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }

    private List<Parking> getAllParkings(String text) {
        System.out.println("All parkings sent");
        String json = text.replace(MessageGenerator.SEND_ALL_PARKINGS, "");
        Type type = new TypeToken<List<Parking>>(){}.getType();
        List<Parking> parkingList = new Gson().fromJson(json, type);
        this.pList = parkingList;
        return parkingList;
    }

    public List<Parking> getParkingList() {
        return this.pList;
    }

    private Parking getParking(String text) {
        String json = text.replace(MessageGenerator.SEND_PARKING, "");
        System.out.println("parking received: " + json);
        return Parking.fromJson(json);
    }

    public void sendOrder(Order order) {
        String message = MessageGenerator.SEND_ORDER;
        message += order.toString();
        webSocket.send(message);
        System.out.println("Order sent: " + message);
    }

    public void getMessage(String text) {
        String message = text.replace(MessageGenerator.MESSAGE, "");
        System.out.println("Message received: " + message);
    }
}