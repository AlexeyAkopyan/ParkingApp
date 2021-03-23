package com.example.parkingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InfoTimeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_ok;
    private TextView txt_valid_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_time);
        btn_ok = findViewById(R.id.btn_ok);
        txt_valid_time = findViewById(R.id.txt_valid_time);
        btn_ok.setOnClickListener(this);
        Intent intent=getIntent();
        txt_valid_time.setText(intent.getStringExtra("working_hours").toString());
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_ok) {
            finish();
        }
    }
}