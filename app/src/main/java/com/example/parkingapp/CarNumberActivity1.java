package com.example.parkingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CarNumberActivity1 extends AppCompatActivity {
    Button btn_go;
    TextView edit_txt_car_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (restorePrefData()){
            Intent intent_main = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent_main);
            finish();
        }

        setContentView(R.layout.activity_car_number1);

//        TextWatcher txt_watcher =  new TextWatcher() {
//            public void afterTextChanged(Editable s) {
//            }
//
//            public void beforeTextChanged(CharSequence s, int start,
//                                          int count, int after) {
//            }
//
//            public void onTextChanged(CharSequence s, int start,
//                                      int before, int count) {
//            }
//        };

        btn_go = findViewById(R.id.btn_go);
        edit_txt_car_number = findViewById(R.id.edit_txt_car_number);
//        edit_txt_car_number.addTextChangedListener(txt_watcher);
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_main = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent_main);
                savePrefsData();
                finish();
            }
        });
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(
                "CarNumber", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("carNumber", edit_txt_car_number.getText().toString());
        editor.putBoolean("isCarNumberStored", true);
        editor.commit();
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(
                "CarNumber", MODE_PRIVATE);
        Boolean isCarNumberStoredBefore = pref.getBoolean(
                "isCarNumberStored", false);
        return isCarNumberStoredBefore;
    }
}