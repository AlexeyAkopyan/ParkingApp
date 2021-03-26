package com.example.parkingapp.net;

import com.example.parkingapp.objects.Constants;
import com.example.parkingapp.objects.MessageGenerator;
import com.example.parkingapp.objects.Order;
import com.example.parkingapp.objects.Parking;

import okhttp3.*;
import okio.ByteString;

import java.io.*;
import java.util.List;

public class WebSocketConnection {
    private OkHttpClient client ;
    private Request request;
    private WebSocket webSocket;
    private ClientWebSocketListener listener;
    private ParkingService parkingService;
    private OrderService orderService;

    public WebSocketConnection(String url, ParkingService parkingService, OrderService orderService) {
        client = new OkHttpClient();
        request = new Request.Builder().url(url).build();
        listener = new ClientWebSocketListener(this);
        this.parkingService = parkingService;
        this.orderService = orderService;
        //parkingList = null;
    }

    public void init() {
        webSocket = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }

    public void send(String message) {
        webSocket.send(message);
        System.out.println("Text sent: " + message);
    }

    public void sendMessage(String message) {
        message = MessageGenerator.MESSAGE + message;
        webSocket.send(message);
        System.out.println("Message sent: " + message);
    }

    public void sendError(String error) {
        error = MessageGenerator.ERROR + error;
        webSocket.send(error);
        System.out.println("Error sent: " + error);
    }

    public void sendOrder(Order order) {
        String message = MessageGenerator.SEND_ORDER + order.toString();
        webSocket.send(message);
        System.out.println("Order sent: " + message);
    }

    public void onParkingListGet(List<Parking> parkingList) {
        // place here your code
        // this is an example
        System.out.println("Received all parkings:");
        for (int i = 0; i < parkingList.size(); i++) {
            System.out.println(parkingList.get(i).toString());
        }
        //this.parkingList = parkingList;
        parkingService.setParkingList(parkingList);
    }

    public void onParkingGet(Parking parking) {
        // place here your code
        // this is an example
        System.out.println("Received parking update:");
        System.out.println(parking.toString());
        parkingService.updateParking(parking);
    }

    public void onMessageGet(String message) {
        // place here your code
        // this is an example
        System.out.println("Received message:");
        System.out.println(message);
    }

    public void onOrderIdGet(String message) {
        int split = message.indexOf('|');
        Long oldId = Long.parseLong(message.substring(0, split));
        Long newId = Long.parseLong(message.substring(split + 1));
        String getQR = MessageGenerator.GET_QR + newId;
        this.send(getQR);
        System.out.println("send qr request: " + getQR);
    }

    public File onQRGet(ByteString bytes) {
        try {
            Long id = bytes.asByteBuffer().getLong();
            File dir = new File (Constants.PATH_TO_QR);
            dir.mkdir();
            File qrFile = new File(dir,"qr" + id + ".png");
            qrFile.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(qrFile, false);
            outputStream.write(bytes.toByteArray(), Long.BYTES, bytes.toByteArray().length - Long.BYTES);
            outputStream.flush();
            outputStream.close();
            System.out.println("QR is received and written at file: " + qrFile.getAbsolutePath());
            return qrFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void onError(String error) {
        // place here your code
        // this is an example
        System.out.println("Received error message");
        System.out.println(error);
    }

    public void close() {
        // place here your code
        // this is an example
        webSocket.close(1000, "");
        System.out.println("Connection closed");
    }

    public void reconnect() {
        // place here your code
    }

    public List<Parking> getParkingList() {
        //return this.parkingList;
        return null;
    }
}