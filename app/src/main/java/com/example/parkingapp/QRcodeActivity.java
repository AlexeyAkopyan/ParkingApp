package com.example.parkingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class QRcodeActivity extends AppCompatActivity {
    ImageView qr_code;
    Button btn_ok;
    TextView txt_address, txt_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        qr_code = findViewById(R.id.img_qr_code);
        btn_ok = findViewById(R.id.btn_ok_qr_code);
        txt_address = findViewById(R.id.txt_address_qr);
        txt_time = findViewById(R.id.txt_time_qr);

        txt_address.setText(MainActivity.selected_place.getAddress());
        List<Integer> selected_time = new ArrayList<Integer>(MainActivity.selected_time);

        txt_time.setText("c " + TimeFormat(selected_time.get(0)) +
                ":" + TimeFormat(selected_time.get(1)) + " по " + TimeFormat(selected_time.get(2)) +
                ":" + TimeFormat(selected_time.get(3)));
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