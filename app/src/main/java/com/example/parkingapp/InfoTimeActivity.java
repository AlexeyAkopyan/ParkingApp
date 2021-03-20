package com.example.parkingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InfoTimeActivity extends AppCompatActivity implements View.OnClickListener {

    public Button btn_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_time);
        btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_ok) {
            finish();
        }
    }
}