package com.example.parkingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.cloudipsp.android.Card;
import com.cloudipsp.android.CardInputView;
public class CardActivity extends BaseCardActivity {
    private CardInputView cardInput;
    private EditText editAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cardInput = findViewById(R.id.card_input);
        editAmount = findViewById(R.id.edit_amount);
        if (BuildConfig.DEBUG) {
            cardInput.setHelpedNeeded(true);
        }
        Intent intent=getIntent();

        Log.i("TAG_PRICE_2", intent.getStringExtra("amount"));
        editAmount.setText(intent.getStringExtra("amount"));
        Log.i("TAG_PRICE_3", intent.getStringExtra("amount"));
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_card;
    }

    @Override
    protected Card getCard() {
        return cardInput.confirm(new CardInputView.ConfirmationErrorHandler() {
            @Override
            public void onCardInputErrorClear(CardInputView view, EditText editText) {

            }

            @Override
            public void onCardInputErrorCatched(CardInputView view, EditText editText, String error) {

            }
        });
    }
}


