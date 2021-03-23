package com.example.parkingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener
{
    public Button btn_pay;
    public ImageButton btn_back;
    public TextView txt_street;
    public TextView txt_time_from_hours;
    public TextView txt_time_from_minutes;
    public TextView txt_time_to_hours;
    public TextView txt_time_to_minutes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        btn_pay = findViewById(R.id.btn_pay);
        btn_back = findViewById(R.id.btn_back);
        txt_street = findViewById(R.id.txt_street2);
        txt_time_from_hours = findViewById(R.id.txt_time_from_hours2);
        txt_time_from_minutes = findViewById(R.id.txt_time_from_minutes2);
        txt_time_to_hours = findViewById(R.id.txt_time_to_hours2);
        txt_time_to_minutes = findViewById(R.id.txt_time_to_minutes2);
        btn_pay.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        Intent intent=getIntent();
        txt_street.setText(intent.getStringExtra("address"));
        txt_time_from_hours.setText(TimeFormat(intent.getIntExtra("from_hours", 0)));
        txt_time_from_minutes.setText(TimeFormat(intent.getIntExtra("from_minutes", 0)));
        txt_time_to_hours.setText(TimeFormat(intent.getIntExtra("to_hours", 0)));
        txt_time_to_minutes.setText(TimeFormat(intent.getIntExtra("to_minutes", 0)));
    }

    public String TimeFormat(Integer i){
        return ((i<10)?"0":"") + i;
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_pay) {
            Intent intent = new Intent(this, AddCardActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.btn_back){
            this.finish();
        }
    }
}