package com.example.parkingapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Fragment1 extends Fragment implements View.OnClickListener {

    public interface OnSelectedButtonListener {
        void onButtonSelected(int id);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment1, container, false);
        Button btn_close = rootView.findViewById(R.id.button_remove);
        Button btn_order = rootView.findViewById(R.id.button_reserve);
        btn_close.setOnClickListener(this);
        btn_order.setOnClickListener(this);

        return rootView;

    }

    @Override
    public void onClick(View v) {
        int id;
        OnSelectedButtonListener listener = (OnSelectedButtonListener) getActivity();
        listener.onButtonSelected(v.getId());
        }


    }

