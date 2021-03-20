package com.example.parkingapp;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
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
import java.util.List;

public class ParkInfoFragment extends Fragment implements View.OnClickListener {



    public ParkingPlace selected_place;
    public TextView txt_street;
    TextView edit_txt_time_from;
    TextView edit_txt_time_to;
    TextView txt_n_free_park_spaces;
    public EditText txt_time_from_hours;
    public EditText txt_time_from_minutes;
    public EditText txt_time_to_hours;
    public EditText txt_time_to_minutes;
    public List<String> selected_time;
    SendTime ST;

    interface SendTime {
        void sendData(List<String> message);
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
        txt_street.setText(selected_place.getAddress());
        txt_n_free_park_spaces.setText(String.valueOf(selected_place.getNFreePlaces()));
        Log.i("TAG_start", selected_place.getAddress());

    }

    public interface OnSelectedButtonListener {
        void onButtonSelected(int id);
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
        Button btn_go_on = rootView.findViewById(R.id.btn_go_on);
        ImageButton btn_time_info = rootView.findViewById(R.id.btn_time_info);
        btn_scroll_down.setOnClickListener(this);
        btn_go_on.setOnClickListener(this);
        btn_time_info.setOnClickListener(this);
        Log.i("TAG_start", selected_place.getAddress());
        txt_street.setText(selected_place.getAddress());
        txt_n_free_park_spaces.setText(String.valueOf(selected_place.getNFreePlaces()));
        Log.i("TAG_end", (String) txt_street.getText());
        return rootView;

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
        int id;
        selected_time = new ArrayList<String>();
        selected_time.add(txt_time_from_hours.getText().toString());
        selected_time.add(txt_time_from_minutes.getText().toString());
        selected_time.add(txt_time_to_hours.getText().toString());
        selected_time.add(txt_time_to_minutes.getText().toString());
        ST.sendData(selected_time);
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

