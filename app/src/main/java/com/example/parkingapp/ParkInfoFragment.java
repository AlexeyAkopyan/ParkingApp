package com.example.parkingapp;

import android.animation.Animator;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.DrawableRes;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static android.content.Context.MODE_PRIVATE;
import static com.example.parkingapp.R.*;

public class ParkInfoFragment extends Fragment implements View.OnClickListener {



    private ParkingPlace selected_place;
    private TextView txt_street;
    private EditText txt_time_from_hours,
            txt_time_from_minutes,
            txt_time_to_hours,
            txt_time_to_minutes;
    private TextView txt_time_slot, txt_amount;
    private  Button btn_go_on;
    private List<Integer> selected_time;
    private ImageButton btn_pay_type;
    SendTime ST;

    interface SendTime {
        void sendData(List<Integer> message, String amount);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            ST = (ParkInfoFragment.SendTime) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }



    public interface OnSelectedButtonListener {
        void onButtonSelected(int id);
    }

    public String TimeFormat(Integer i){
        return ((i<10)?"0":"") + i;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(layout.fragment1, container, false);
        txt_street = rootView.findViewById(id.txt_street);
        txt_amount = rootView.findViewById(id.txt_amount);
        txt_time_from_hours = rootView.findViewById(id.edit_txt_time_from_hours);
        txt_time_from_minutes = rootView.findViewById(id.edit_txt_time_from_minutes);
        txt_time_to_hours = rootView.findViewById(id.edit_txt_time_to_hours);
        txt_time_to_minutes = rootView.findViewById(id.edit_txt_time_to_minutes);
        txt_time_slot =rootView.findViewById(id.txt_time_slot);
        Button btn_scroll_down = rootView.findViewById(id.btn_scroll_down);
        btn_go_on = rootView.findViewById(id.btn_go_on);
        btn_pay_type = rootView.findViewById(id.btn_pay_type);
        TextWatcher txt_watcher =  new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Log.i("TAG_after_change", "start");
                checkIsTimeValid();
//                txt_amount.setText("360₽");
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        };

//        setValidTime();

        txt_time_from_hours.addTextChangedListener(txt_watcher);
        txt_time_from_minutes.addTextChangedListener(txt_watcher);
        txt_time_to_hours.addTextChangedListener(txt_watcher);
        txt_time_to_minutes.addTextChangedListener(txt_watcher);

        btn_scroll_down.setOnClickListener(this);
        btn_go_on.setOnClickListener(this);
        btn_pay_type.setOnClickListener(this);
        txt_time_slot.setOnClickListener(this);

        setSelectedPayType(restorePrefData());

//        checkIsTimeValid();

//        txt_street.setText(selected_place.getAddress());
//        txt_n_free_park_spaces.setText(String.valueOf(selected_place.getNumberFreePlaces()));
        return rootView;

    }

    private void setValidTime() {
        Calendar now = Calendar.getInstance();
        Integer current_hour = now.get(Calendar.HOUR_OF_DAY);
        Integer current_minute = now.get(Calendar.MINUTE);
        Log.i("TAG_WRK_HOURS", selected_place.getWorkingHours().toString());
        List<Integer> valid_time = new ArrayList<>(selected_place.getWorkingHours());
        if (current_hour > valid_time.get(0) ||
                (current_hour == valid_time.get(0) &&
                        current_minute >= valid_time.get(1)))
        {
            valid_time.set(0, current_hour);
            valid_time.set(1, current_minute);
        }
        if (valid_time.get(0) + 1 < valid_time.get(2) ||
                (valid_time.get(0) + 1 == valid_time.get(2) &&
                        valid_time.get(1) <= valid_time.get(3)))
        {
            valid_time.set(2, valid_time.get(0) + 1);
            valid_time.set(3, valid_time.get(1));
        }

        txt_time_from_hours.setText(TimeFormat(valid_time.get(0)));
        txt_time_from_minutes.setText(TimeFormat(valid_time.get(1)));
        txt_time_to_hours.setText(TimeFormat(valid_time.get(2)));
        txt_time_to_minutes.setText(TimeFormat(valid_time.get(3)));

        Log.i("TAG_WRK_HOURS", selected_place.getWorkingHours().toString());
    }

    public void checkIsTimeValid(){

        List<Integer> working_hours = selected_place.getWorkingHours();
        selected_time = new ArrayList<Integer>();

        if (txt_time_from_hours.getText().toString().trim().length() > 0){
            selected_time.add(Integer.valueOf(txt_time_from_hours.getText().toString())); }
        else{
            selected_time.add(0);
        }
        if (txt_time_from_minutes.getText().toString().trim().length() > 0){
            selected_time.add(Integer.valueOf(txt_time_from_minutes.getText().toString())); }
        else{
            selected_time.add(0);
        }
        if (txt_time_to_hours.getText().toString().trim().length() > 0){
            selected_time.add(Integer.valueOf(txt_time_to_hours.getText().toString())); }
        else{
            selected_time.add(23);
        }
        if (txt_time_to_minutes.getText().toString().trim().length() > 0){
            selected_time.add(Integer.valueOf(txt_time_to_minutes.getText().toString())); }
        else{
            selected_time.add(59);
        }
        Log.i("TAG_check_selected_time", TimeFormat(selected_time.get(0)) +
                ":" + TimeFormat(selected_time.get(1)) + "   по " + TimeFormat(selected_time.get(2)) +
                ":" + TimeFormat(selected_time.get(3)));
        Log.i("TAG_check_working_hours", TimeFormat(working_hours.get(0)) +
                ":" + TimeFormat(working_hours.get(1)) + "   по " + TimeFormat(working_hours.get(2)) +
                ":" + TimeFormat(working_hours.get(3)));
        if (
                (selected_time.get(0) > working_hours.get(0) ||
                        (selected_time.get(0) == working_hours.get(0) &&
                                selected_time.get(1) >= working_hours.get(1))) &&
                (selected_time.get(2) < working_hours.get(2) ||
                        (selected_time.get(2) == working_hours.get(2) &&
                                selected_time.get(3)<= working_hours.get(3)))
        )
        {
            Log.i("TAG_check_valid", "valid");
            btn_go_on.setClickable(true);
            btn_go_on.setBackgroundColor(getResources().getColor(color.megapurple));
            Log.i("TAG_check_valid", String.valueOf(btn_go_on.isClickable()));


            int cx = - txt_amount.getMeasuredWidth();
            int cy = txt_amount.getMeasuredHeight() / 2;
            int finalRadius = (int) Math.hypot(txt_amount.getWidth() * 2, txt_amount.getHeight());
            Animator anim_amount = ViewAnimationUtils.createCircularReveal(txt_amount, cx, cy, 0, finalRadius);
            anim_amount.setDuration(1000);
            txt_amount.setText("470₽");
            txt_amount.setVisibility(View.VISIBLE);
            anim_amount.start();
            MainActivity.selected_time = selected_time;
        }
        else
        {
            Log.i("TAG_check_valid", "invalid");
            btn_go_on.setClickable(false);
            btn_go_on.setBackgroundColor(getResources().getColor(color.button_unable));
            Log.i("TAG_check_valid", String.valueOf(btn_go_on.isClickable()));
        }
    }

//    public interface OnDataPass {
//        public void onDataPass(String from_hours,
//                               String from_minutes,
//                               String to_hours,
//                               String to_minutes);
//    }
//
//    OnDataPass dataPasser;
//
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        dataPasser = (OnDataPass) (String.valueOf(txt_time_from_hours.getText()),
//                String.valueOf(txt_time_from_minutes.getText()),
//                String.valueOf(txt_time_to_hours.getText()),
//                String.valueOf(txt_time_to_minutes.getText()))
//    }
//
//
//
//    public void passData(String from_hours,
//                         String from_minutes,
//                         String to_hours,
//                         String to_minutes) {
//        dataPasser.onDataPass(from_hours, from_minutes, to_hours, to_minutes);
//    }



    public void setSelectedPlace(ParkingPlace place) {
        selected_place = place;
//        setValidTime();
        txt_street.setText(selected_place.getAddress());
        List<Integer> time_slot = new ArrayList<>(selected_place.getWorkingHours());
        txt_time_slot.setText(TimeFormat(time_slot.get(0)) + ":" +
                TimeFormat(time_slot.get(1)) + " - " +
                TimeFormat(time_slot.get(2))  + ":" +
                TimeFormat(time_slot.get(3)));
        Log.i("TAG_start", selected_place.getAddress());
    }

//    public void changeSelectedPlace(ParkingPlace place) {
//        selected_place = place;
//        setValidTime();
//        txt_street.setText(selected_place.getAddress());
//        txt_n_free_park_spaces.setText(String.valueOf(selected_place.getNumberFreePlaces()));
//        Log.i("TAG_start", selected_place.getAddress());
//
//    }

    @Override
    public void onClick(View v) {
        if (v.getId() == id.btn_go_on) {
            Log.i("TAG", selected_place.getAddress());
            Log.i("TAG_check_selected_time", TimeFormat(selected_time.get(0)) +
                    ":" + TimeFormat(selected_time.get(1)) + "   по " + TimeFormat(selected_time.get(2)) +
                    ":" + TimeFormat(selected_time.get(3)));
            ST.sendData(selected_time, txt_amount.getText().toString());
        }
        if (v.getId() == id.txt_time_slot){
            List<Integer> valid_time = new ArrayList<>(selected_place.getWorkingHours());
            txt_time_from_hours.setText(TimeFormat(valid_time.get(0)));
            txt_time_from_minutes.setText(TimeFormat(valid_time.get(1)));
            txt_time_to_hours.setText(TimeFormat(valid_time.get(2)));
            txt_time_to_minutes.setText(TimeFormat(valid_time.get(3)));
            selected_time = valid_time;

        }
        OnSelectedButtonListener listener = (OnSelectedButtonListener) getActivity();
        listener.onButtonSelected(v.getId());
//        if (v.getId() == id.btn_pay_type){
//            RippleDrawable btnColor = (RippleDrawable) btn_go_on.getBackground();
//            if (btnColor.getColor() == getResources().getColor(R.color.button_unable)){
//                OnSelectedButtonListener listener = (OnSelectedButtonListener) getActivity();
//                listener.onButtonSelected(v.getId());
//            }
//        }

    }

    public void setSelectedPayType(Integer id){
        if (id == R.id.txt_google_pay){
            btn_pay_type.setImageResource(drawable.ic_gpay);
            btn_go_on.setBackgroundColor(getResources().getColor(R.color.megapurple));
            btn_pay_type.setBackgroundResource(0);
        }
//        if (id == R.id.txt_master_card){
//            btn_pay_type.setImageResource(drawable.ic_mc_card);
//            btn_go_on.setBackgroundColor(getResources().getColor(color.megapurple));
//            btn_pay_type.setBackgroundResource(drawable.card_box);

//        }
        if (id == R.id.txt_new_card){
            btn_pay_type.setImageResource(drawable.ic_card);
            btn_go_on.setBackgroundColor(getResources().getColor(color.megapurple));
            btn_pay_type.setBackgroundResource(drawable.card_box);
        }
    }

    private Integer restorePrefData() {
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("PaymentTypePref", MODE_PRIVATE);
        Integer lastSelectedPayType = pref.getInt("lastSelectedPayType", id.txt_new_card);
        return lastSelectedPayType;
    }

//
//    public TextView createTimeSlot(List<Integer> time_slot){
//        LinearLayout timeItem =  new LinearLayout(layout.item_time_slot);
//        timeItem.setText(TimeFormat(time_slot.get(0)) + ":" +
//                TimeFormat(time_slot.get(1)) + " - " +
//                TimeFormat(time_slot.get(2))  + ":" +
//                TimeFormat(time_slot.get(3)));
//        timeItem.setLayoutParams(new LinearLayout.LayoutParams(200, 56));
//        timeItem.setTextColor(getResources().getColor(color.black));
//
//        timeItem.setBackground(getResources().getDrawable(R.drawable.time_slot_box));
//    }


    }


//public class FragmentOne extends Fragment {
//
//    SendMessage SM;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View rootView = inflater.inflate(
//                R.layout.fragment_one, container, false);
//        return rootView;
//
//
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        Button btnPassData = (Button) view.findViewById(R.id.btnPassData);
//        final EditText inData = (EditText) view.findViewById(R.id.inMessage);
//        btnPassData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SM.sendData(inData.getText().toString().trim());
//            }
//        });
//
//    }
//
//    interface SendMessage {
//        void sendData(String message);
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        try {
//            SM = (SendMessage) getActivity();
//        } catch (ClassCastException e) {
//            throw new ClassCastException("Error in retrieving data. Please try again");
//        }
//    }
//}

