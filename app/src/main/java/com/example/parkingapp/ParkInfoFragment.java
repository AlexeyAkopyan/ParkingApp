package com.example.parkingapp;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ParkInfoFragment extends Fragment implements View.OnClickListener {



    private ParkingPlace selected_place;
    private TextView txt_street;
    TextView txt_n_free_park_spaces;
    private EditText txt_time_from_hours;
    private EditText txt_time_from_minutes;
    private EditText txt_time_to_hours;
    private EditText txt_time_to_minutes;
    Button btn_go_on;
    private ImageButton btn_time_info;
    private List<Integer> selected_time;
    SendTime ST;

    interface SendTime {
        void sendData(List<Integer> message);
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


    public void changeSelectedPlace(ParkingPlace place) {
        selected_place = place;
        setValidTime();
        txt_street.setText(selected_place.getAddress());
        txt_n_free_park_spaces.setText(String.valueOf(selected_place.getNumberFreePlaces()));
        Log.i("TAG_start", selected_place.getAddress());

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
        View rootView = inflater.inflate(R.layout.fragment1, container, false);
        txt_street = rootView.findViewById(R.id.txt_street);
        txt_n_free_park_spaces = rootView.findViewById(R.id.txt_n_free_park_spaces);
        txt_time_from_hours = rootView.findViewById(R.id.edit_txt_time_from_hours);
        txt_time_from_minutes = rootView.findViewById(R.id.edit_txt_time_from_minutes);
        txt_time_to_hours = rootView.findViewById(R.id.edit_txt_time_to_hours);
        txt_time_to_minutes = rootView.findViewById(R.id.edit_txt_time_to_minutes);
        Button btn_scroll_down = rootView.findViewById(R.id.btn_scroll_down);
        btn_go_on = rootView.findViewById(R.id.btn_go_on);
        btn_time_info = rootView.findViewById(R.id.btn_time_info);
        TextWatcher txt_watcher =  new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Log.i("TAG_after_change", "start");
                checkIsTimeValid();
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        };

        setValidTime();

        txt_time_from_hours.addTextChangedListener(txt_watcher);
        txt_time_from_minutes.addTextChangedListener(txt_watcher);
        txt_time_to_hours.addTextChangedListener(txt_watcher);
        txt_time_to_minutes.addTextChangedListener(txt_watcher);

        btn_scroll_down.setOnClickListener(this);
        btn_go_on.setOnClickListener(this);
        btn_time_info.setOnClickListener(this);
        btn_time_info.setVisibility(View.INVISIBLE);

        checkIsTimeValid();

        txt_street.setText(selected_place.getAddress());
        txt_n_free_park_spaces.setText(String.valueOf(selected_place.getNumberFreePlaces()));
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
            btn_go_on.setBackgroundColor(getResources().getColor(R.color.megapurple));
            Log.i("TAG_check_valid", String.valueOf(btn_go_on.isClickable()));
            btn_time_info.setVisibility(View.INVISIBLE);
        }
        else
        {
            Log.i("TAG_check_valid", "invalid");
            btn_go_on.setClickable(false);
            btn_go_on.setBackgroundColor(getResources().getColor(R.color.button_unable));
            Log.i("TAG_check_valid", String.valueOf(btn_go_on.isClickable()));
            btn_time_info.setVisibility(View.VISIBLE);
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
        Log.i("TAG", selected_place.getAddress());
    }

    @Override
    public void onClick(View v) {
        if ((v.getId() == R.id.btn_go_on) || (v.getId() == R.id.btn_time_info)) {
            Log.i("TAG", selected_place.getAddress());
            Log.i("TAG_check_selected_time", TimeFormat(selected_time.get(0)) +
                    ":" + TimeFormat(selected_time.get(1)) + "   по " + TimeFormat(selected_time.get(2)) +
                    ":" + TimeFormat(selected_time.get(3)));
            ST.sendData(selected_time);
        }
        OnSelectedButtonListener listener = (OnSelectedButtonListener) getActivity();
        listener.onButtonSelected(v.getId());
        }

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

