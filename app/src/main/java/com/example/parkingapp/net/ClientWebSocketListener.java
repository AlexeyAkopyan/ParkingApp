package com.example.parkingapp.net;

import com.example.parkingapp.objects.MessageGenerator;
import com.example.parkingapp.objects.Parking;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class ClientWebSocketListener extends WebSocketListener {
    private WebSocketConnection connection;

    public ClientWebSocketListener(WebSocketConnection connection) {
        this.connection = connection;
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        super.onOpen(webSocket, response);
        sendRequestAllParkings();
    }

    public void onMessage(WebSocket webSocket, String text) {
        parseMessage(text);
    }

    public void onMessage(WebSocket webSocket, ByteString bytes) {
        parseByteString(bytes);
    }

    public void onClosed(WebSocket webSocket, int code, String reason){
        System.out.println("Connection closed");
        System.out.println(code + ": " + reason);
    }

    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        System.out.println("Connection failed");
        t.printStackTrace();
        // place you code here
    }

    public void sendRequestAllParkings() {
        connection.send(MessageGenerator.GET_ALL_PARKINGS);
        System.out.println("Connection established");
    }

    public void parseMessage(String message) {
        if (message.startsWith(MessageGenerator.SEND_PARKING)) {
            String parkingJSON = message.replace(MessageGenerator.SEND_PARKING, "");
            Parking parking = Parking.fromJson(parkingJSON);
            connection.onParkingGet(parking);
        }
        if (message.startsWith(MessageGenerator.SEND_ALL_PARKINGS)) {
            String parkingListJSON = message.replace(MessageGenerator.SEND_ALL_PARKINGS, "");
            Type type = new TypeToken<List<Parking>>(){}.getType();
            List<Parking> parkingList = new Gson().fromJson(parkingListJSON, type);
            connection.onParkingListGet(parkingList);
        }
        if (message.startsWith(MessageGenerator.ERROR)) {
            String error = message.replace(MessageGenerator.ERROR, "");
            connection.onError(error);
        }
        if (message.startsWith(MessageGenerator.MESSAGE)) {
            message = message.replace(MessageGenerator.MESSAGE, "");
            connection.onMessageGet(message);
        }
        if (message.startsWith(MessageGenerator.SEND_ORDER_ID)) {
            message = message.replace(MessageGenerator.SEND_ORDER_ID, "");
            connection.onOrderIdGet(message);
        }
    }

    private void parseByteString(ByteString bytes) {
        connection.onQRGet(bytes);
    }

}