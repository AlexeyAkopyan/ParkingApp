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
        String amount = getIntent().getStringExtra("amount");
        editAmount.setText(amount.substring(0, amount.length() - 1));
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


