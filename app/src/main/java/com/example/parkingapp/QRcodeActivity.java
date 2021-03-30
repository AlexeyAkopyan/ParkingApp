package com.example.parkingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class QRcodeActivity extends AppCompatActivity {
    ImageView qr_code;
    Button btn_ok;
    TextView txt_address, txt_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_rcode);

        qr_code = findViewById(R.id.img_qr_code);
        btn_ok = findViewById(R.id.btn_ok_qr_code);
        txt_address = findViewById(R.id.txt_address_qr);
        txt_time = findViewById(R.id.txt_time_qr);

        txt_address.setText(getIntent().getStringExtra("address"));
//        int[] selected_time = getIntent().getIntArrayExtra("selected_time");

        txt_time.setText("c " + TimeFormat(17) +
                ":" + TimeFormat(30) + " по " + TimeFormat(23) +
                ":" + TimeFormat(59));
        Bitmap bitmap = null;
        try {
            bitmap = MainActivity.orderService.encodeAsBitmap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bitmap != null){
            qr_code.setImageBitmap(bitmap);
        }

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public String TimeFormat(Integer i) {
        return ((i < 10) ? "0" : "") + i;
    }
}