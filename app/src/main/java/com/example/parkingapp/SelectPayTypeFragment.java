package com.example.parkingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.w3c.dom.Text;

import static android.content.Context.MODE_PRIVATE;


public class SelectPayTypeFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    BottomSheetDialogListener bListener;
    TextView txt_google_pay,
    txt_master_card_pay,
    txt_new_card_pay;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_select_pay_type, container, false);
        txt_google_pay = rootView.findViewById(R.id.txt_google_pay);
        txt_master_card_pay = rootView.findViewById(R.id.txt_master_card);
        txt_new_card_pay = rootView.findViewById(R.id.txt_new_card);
        txt_google_pay.setOnClickListener(this);
        txt_master_card_pay.setOnClickListener(this);
        txt_new_card_pay.setOnClickListener(this);

        TextView last_selected = rootView.findViewById(restorePrefData());
        last_selected.setCompoundDrawables(last_selected.getCompoundDrawables()[0], null,
                getResources().getDrawable(R.drawable.ic_checkmark, getActivity().getTheme()), null);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        bListener.OnCardSelected(v.getId());
//        setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_checkmark, 0);
        savePrefsData(v.getId());
        dismiss();
    }

    public interface BottomSheetDialogListener {
        void OnCardSelected(Integer id);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
            bListener = (BottomSheetDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
    }
    }

    private void savePrefsData(Integer id) {
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("PaymentTypePref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("lastSelectedPayType", id);
        editor.commit();
    }

    private Integer restorePrefData() {
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("PaymentTypePref", MODE_PRIVATE);
        Integer lastSelectedPayType = pref.getInt("lastSelectedPayType", R.id.txt_new_card);
        return lastSelectedPayType;
    }
}