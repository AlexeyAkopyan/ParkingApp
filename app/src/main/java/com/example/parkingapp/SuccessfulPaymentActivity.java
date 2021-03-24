package com.example.parkingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.parkingapp.objects.Order;

import java.util.Date;
import java.util.Random;

public class SuccessfulPaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_payment);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }

//    private void sendOrder(String carNum, Integer parkingId, Date start, Date finish, String paymentInfo) {
//
//        int parkingId = random.nextInt(connection.getParkingList().size());
//        String carNum = "car" + random.nextInt(100);
//        Date start = new Date(new Date().getTime() + random.nextInt(3600 * 1000 * 8) + 3600 * 1000);
//        Date finish = new Date(start.getTime() + 3600 * 1000);
//        String paymentInfo = "here comes some payment information " + random.nextInt(100);
//        Order order = new Order(null, (long) parkingId, carNum, start.getTime(),
//                finish.getTime(), paymentInfo);
//        connection.sendOrder(order);
//    }
}
