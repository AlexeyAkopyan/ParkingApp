package com.example.parkingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    List<ScreenItem> mList;
    Button btn_skip, btn_next;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (restorePrefData()){
            Intent intent_car_number = new Intent(getApplicationContext(), CarNumberActivity1.class);
            startActivity(intent_car_number);
            Log.i("TAG_IntroWasOpened", String.valueOf(true));
            finish();
        }
        setContentView(R.layout.activity_intro);

        mList = new ArrayList<>();
        btn_next = findViewById(R.id.btn_next);
        btn_skip = findViewById(R.id.btn_skip);
        mList.add(new ScreenItem(R.string.introduction_1, R.drawable.corvette));
        mList.add(new ScreenItem(R.string.introduction_2, R.drawable.canvas));
        mList.add(new ScreenItem(R.string.introduction_3, R.drawable.cherub));

        screenPager = findViewById(R.id.screen_view_pager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        screenPager.setAdapter(introViewPagerAdapter);
        screenPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == mList.size() - 1){
                    btn_skip.setVisibility(View.INVISIBLE);
                }
                else{
                    btn_skip.setVisibility(View.VISIBLE);
                }
                if (position == mList.size() ){
                    Intent intent_car_number = new Intent(getApplicationContext(), CarNumberActivity1.class);
                    startActivity(intent_car_number);
                    savePrefsData();
                    finish();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btn_next.setOnClickListener(this);
        btn_skip.setOnClickListener(this);

    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("IntroPref", MODE_PRIVATE);
        Boolean isIntroActivityOpenedBefore = pref.getBoolean("wasIntroOpened", false);
        return isIntroActivityOpenedBefore;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_skip
                || (v.getId() == R.id.btn_next && screenPager.getCurrentItem() == mList.size() - 1)){
            Intent intent_car_number = new Intent(getApplicationContext(), CarNumberActivity1.class);
            startActivity(intent_car_number);
            savePrefsData();
            finish();
        }
        else{
            if (v.getId() == R.id.btn_next) {
                position = screenPager.getCurrentItem();
                if (position < mList.size()) {
                    position++;
                    screenPager.setCurrentItem(position);
                }
                if (position == mList.size() - 1) {
                    btn_skip.setVisibility(View.INVISIBLE);

                }
            }
        }
    }

//        switch (v.getId()) {
//            case R.id.btn_next:
//                position = screenPager.getCurrentItem();
//                if (position < mList.size()) {
//                    position++;
//                    screenPager.setCurrentItem(position);
//                }
//                if (position == mList.size() - 1) {
//                    btn_skip.setVisibility(View.INVISIBLE);
//
//                }
//                if (position == mList.size()) {
//                    Intent intent_car_number = new Intent(getApplicationContext(), CarNumberActivity1.class);
//                    startActivity(intent_car_number);
//                    savePrefsData();
//                    finish();
//                }
//                break;
//            case R.id.btn_skip:
//                screenPager.setCurrentItem(screenPager.getCurrentItem() + 1);
//                screenPager.setCurrentItem(screenPager.getCurrentItem() + 1);
//                Intent intent_car_number = new Intent(getApplicationContext(), CarNumberActivity1.class);
//                startActivity(intent_car_number);
//                savePrefsData();
//                finish();
//        }
//    }


    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("IntroPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("wasIntroOpened", true);
        editor.commit();
    }
}