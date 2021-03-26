package com.example.parkingapp;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parkingapp.objects.Order;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class SuccessfulPaymentActivity extends AppCompatActivity {
    ImageView qr_code;
    TextView txt_ready,
            txt_succ_payment,
            txt_scan_qr_code;
    Button btn_ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_payment);

        Intent intent_payment = getIntent();
        int parkingId = intent_payment.getIntExtra("parkingId", 0);
        List<Integer> selected_time = intent_payment.getIntegerArrayListExtra("selected_time");
        String carNum = "car" + MainActivity.car_number;
        Calendar start = new GregorianCalendar();
        start.set(Calendar.HOUR_OF_DAY, selected_time.get(0));
        start.set(Calendar.MINUTE, selected_time.get(1));
        Calendar finish = new GregorianCalendar();
        finish.set(Calendar.HOUR_OF_DAY, selected_time.get(2));
        finish.set(Calendar.MINUTE, selected_time.get(3));
        String paymentInfo = "here comes some payment information " + intent_payment.getIntExtra("paymentId", 0);
        com.example.parkingapp.objects.Order order = new com.example.parkingapp.objects.Order(null, (long) parkingId, carNum, start.getTimeInMillis(),
                finish.getTimeInMillis(), paymentInfo);
        MainActivity.connection.sendOrder(order);

        qr_code = findViewById(R.id.img_qr_code2);
        txt_ready = findViewById(R.id.txt_ready);
        txt_succ_payment = findViewById(R.id.txt_succ_payment);
        txt_scan_qr_code = findViewById(R.id.txt_scan_qr_code2);
        btn_ok = findViewById(R.id.btn_ok2);

        qr_code.setImageResource(R.drawable.button_light_box);
        txt_ready.setVisibility(View.INVISIBLE);
        txt_succ_payment.setVisibility(View.INVISIBLE);
        txt_scan_qr_code.setVisibility(View.VISIBLE);
        btn_ok.setVisibility(View.VISIBLE);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_main = new Intent(SuccessfulPaymentActivity.this, MainActivity.class);
                startActivity(intent_main);
                finish();
            }
        });




        }

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
