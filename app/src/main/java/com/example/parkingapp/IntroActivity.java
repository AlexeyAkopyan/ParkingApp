package com.example.parkingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    List<ScreenItem> mList;
    Button btn_skip, btn_next, btn_all_clear;
    TabLayout tab_indicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (restorePrefData()){
            loadNextActivity(false);
            Log.i("TAG_IntroWasOpened", String.valueOf(true));
        }
        setContentView(R.layout.activity_intro);

        tab_indicator = findViewById(R.id.tab_indicator);


        mList = new ArrayList<>();
        btn_next = findViewById(R.id.btn_next);
        btn_skip = findViewById(R.id.btn_skip);
        btn_all_clear = findViewById(R.id.btn_all_clear);
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
                    loadPage(true);
                }
                else{
                    loadPage(false);
                }
                if (position == mList.size() ){
                    loadNextActivity(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tab_indicator.setupWithViewPager(screenPager);
        btn_next.setOnClickListener(this);
        btn_skip.setOnClickListener(this);
        btn_all_clear.setOnClickListener(this);

    }



    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("IntroPref", MODE_PRIVATE);
        Boolean isIntroActivityOpenedBefore = pref.getBoolean("wasIntroOpened", false);
        return isIntroActivityOpenedBefore;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.btn_skip || v.getId() == R.id.btn_all_clear) {
            loadNextActivity(true);
        }
        if (v.getId() == R.id.btn_next){
                screenPager.setCurrentItem(screenPager.getCurrentItem() + 1);
        }
    }

    private void loadNextActivity(boolean savePrefData){
        Intent intent_car_number = new Intent(getApplicationContext(), CarNumberActivity.class);
        startActivity(intent_car_number);
        if (savePrefData) { savePrefsData();}
        finish();
    }

    private void loadPage(boolean last){
        if (last) {
            Animation anim_appear = AnimationUtils.loadAnimation(this, R.anim.button_all_clear);
            tab_indicator.setVisibility(View.INVISIBLE);
            btn_skip.setVisibility(View.INVISIBLE);
            btn_next.setVisibility(View.INVISIBLE);
            btn_all_clear.setVisibility(View.VISIBLE);
            btn_all_clear.startAnimation(anim_appear);
        }
        else {
            tab_indicator.setVisibility(View.VISIBLE);
            btn_skip.setVisibility(View.VISIBLE);
            btn_next.setVisibility(View.VISIBLE);
            btn_all_clear.setVisibility(View.INVISIBLE);

        }
    }


    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("IntroPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("wasIntroOpened", true);
        editor.commit();
    }
}