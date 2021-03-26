package com.example.parkingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CarNumberActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_go;
    TextView txt_no_such_number;
    EditText car_number, car_number2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (restorePrefData()){
            Intent intent_main = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent_main);
            finish();
        }

        setContentView(R.layout.activity_car_number);

        btn_go = findViewById(R.id.btn_go);
        txt_no_such_number = findViewById(R.id.txt_no_such_number);
        txt_no_such_number.setClickable(true);
        car_number = findViewById(R.id.edit_txt_car_number);
        car_number2 = findViewById(R.id.edit_txt_car_number2);
        car_number2.setVisibility(View.INVISIBLE);
        car_number.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        car_number.addTextChangedListener(new TextWatcher() {
            InputFilter[] CapFilter = new InputFilter[] {new InputFilter.LengthFilter(10)};

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                switch (s.length()){
                    case 0: case 4: case 5:
                        car_number.setInputType(InputType.TYPE_CLASS_TEXT);
                        car_number.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
                        break;
                    case 1: case 2: case 3:
                        car_number.setInputType(InputType.TYPE_CLASS_NUMBER);
                        break;
                    case 6:
                        car_number.setInputType(InputType.TYPE_CLASS_NUMBER);
                        car_number.setFilters(CapFilter);
                        break;
                    case 7:
                        if (count > before) {
                            car_number.setText(
                                    String.format("%s %s",
                                            s.toString().substring(0, car_number.length() - 1),
                                            s.charAt(s.length() - 1)));}
                        if (count < before) {
                            car_number.setText(
                                    s.toString().substring(0, car_number.length() - 1));}
                        break;
                }
                car_number.setSelection(car_number.length());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btn_go.setOnClickListener(this);
        txt_no_such_number.setOnClickListener(this);
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(
                "CarNumberPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if (car_number.getVisibility() == View.VISIBLE){
            editor.putString("carNumber", car_number.getText().toString());}
        else {editor.putString("carNumber", car_number2.getText().toString());}
        editor.putBoolean("wasCarNumberStored", true);
        editor.commit();
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(
                "CarNumberPref", MODE_PRIVATE);
        Boolean isCarNumberStoredBefore = pref.getBoolean(
                "wasCarNumberStored", false);
        return isCarNumberStoredBefore;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_go) {
            if (car_number.length() >= 9) {
                Intent intent_main = new Intent(getApplicationContext(), MainActivity.class);
                savePrefsData();
                startActivity(intent_main);

                finish();
            } else {
                car_number.setError(getString(R.string.incorrect_car_number));
            }
        };
        if (v.getId() == R.id.txt_no_such_number){
            car_number.setVisibility(View.INVISIBLE);
            car_number2.setVisibility(View.VISIBLE);
            txt_no_such_number.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (car_number2.getVisibility() == View.VISIBLE){
            car_number.setVisibility(View.VISIBLE);
            car_number2.setVisibility(View.INVISIBLE);
            txt_no_such_number.setVisibility(View.VISIBLE);
        }
        else{
            super.onBackPressed();
        }
    }
}