package com.example.parkingapp;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parkingapp.objects.Order;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SuccessfulPaymentActivity extends AppCompatActivity {
    public static ImageView img_qr_code;
    TextView txt_ready,
            txt_succ_payment,
            txt_scan_qr_code;
    Button btn_ok;
    private static SuccessfulPaymentActivity context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_payment);
        context = this;
        Timer timer = new Timer();

        img_qr_code = findViewById(R.id.img_qr_code2);
        txt_ready = findViewById(R.id.txt_ready);
        txt_succ_payment = findViewById(R.id.txt_succ_payment);
        txt_scan_qr_code = findViewById(R.id.txt_scan_qr_code2);
        btn_ok = findViewById(R.id.btn_ok2);

        Intent intent_payment = getIntent();
        Long parkingId = intent_payment.getLongExtra("parkingId", 0);
        List<Integer> selected_time = intent_payment.getIntegerArrayListExtra("selected_time");
        String carNum = "car" + MainActivity.car_number;
        Calendar start = new GregorianCalendar();
        start.set(Calendar.HOUR_OF_DAY, selected_time.get(0));
        start.set(Calendar.MINUTE, selected_time.get(1));
        Calendar finish = new GregorianCalendar();
        finish.set(Calendar.HOUR_OF_DAY, selected_time.get(2));
        finish.set(Calendar.MINUTE, selected_time.get(3));
        Long paymentInfo = Long.valueOf(intent_payment.getLongExtra("paymentId", 0));
        Order order = new Order(parkingId, carNum, start.getTimeInMillis(),
                finish.getTimeInMillis(), paymentInfo);

        System.out.println(MainActivity.connection);

        System.out.println(order);
        System.out.println(order.getId());

        MainActivity.connection.sendOrder(order);

        timer.schedule(new TimerTask() {
                           @Override
                           public void run() {
                               Intent intent_qr_code = new Intent(SuccessfulPaymentActivity.getContext(), QRcodeActivity.class);
                               startActivity(intent_qr_code);
                               finish();
                    }
                }, 300);
        }

    public static SuccessfulPaymentActivity getContext() {
        return context;
    }
}
