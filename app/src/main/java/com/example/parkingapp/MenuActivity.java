package com.example.parkingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton
            btn_close_menu,
            btn_change_car_number;
    private TextView
            txt_car_number_menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        btn_close_menu = findViewById(R.id.btn_close_menu);
        btn_change_car_number = findViewById(R.id.btn_change_car_number);
        txt_car_number_menu = findViewById(R.id.txt_car_number_menu);

        txt_car_number_menu.setText(getIntent().getStringExtra("car_number"));

        btn_close_menu.setOnClickListener(this);
        Context context = getApplicationContext();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_change_car_number:
                Intent intent_change_car_number = new Intent(this, CarNumberActivity.class);
                startActivity(intent_change_car_number);
                break;

            case R.id.btn_close_menu:
                this.finish();
                break;

        }

    }
}