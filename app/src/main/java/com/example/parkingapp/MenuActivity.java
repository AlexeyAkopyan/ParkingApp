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

    private ImageButton btn_close_menu;
    private TextView txt_car_number_menu;
    private TextView txt_booking_history;
    private TextView txt_payment_type;
    private TextView txt_help_chat;
    private TextView txt_about_app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        btn_close_menu = findViewById(R.id.btn_close_menu);
        txt_car_number_menu = findViewById(R.id.txt_car_number_menu);
        txt_booking_history = findViewById(R.id.txt_booking_history);
        txt_payment_type = findViewById(R.id.txt_payment_type);
        txt_help_chat = findViewById(R.id.txt_help_chat);
        txt_about_app = findViewById(R.id.txt_about_app);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(
                "CarNumberPref", MODE_PRIVATE);
        String car_number = pref.getString("carNumber",
                getResources().getString(R.string.type_car_number));
        txt_car_number_menu.setText(car_number);

        txt_booking_history.setClickable(true);
        txt_payment_type.setClickable(true);
        txt_help_chat.setClickable(true);
        txt_about_app.setClickable(true);

        btn_close_menu.setOnClickListener(this);
        txt_booking_history.setOnClickListener(this);
        txt_payment_type.setOnClickListener(this);
        txt_help_chat.setOnClickListener(this);
        txt_about_app.setOnClickListener(this);
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, "Paid "  + "\nPaymentId:" + "245", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
        toast.show();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.txt_booking_history:
                Intent intent_booking_history = new Intent(this, HistoryActivity.class);
                startActivity(intent_booking_history);
                break;

            case R.id.txt_payment_type:
                Intent intent_payment_type = new Intent(this, PayTypeActivity.class);
                startActivity(intent_payment_type);
                break;

            case R.id.txt_help_chat:
                Intent intent_help_chat = new Intent(this, HelpChatActivity.class);
                startActivity(intent_help_chat);
                break;

            case R.id.txt_about_app:
                Intent intent_about_app = new Intent(this, AboutAppActivity.class);
                startActivity(intent_about_app);
                break;

            case R.id.btn_close_menu:
                this.finish();
                break;
        }

    }
}