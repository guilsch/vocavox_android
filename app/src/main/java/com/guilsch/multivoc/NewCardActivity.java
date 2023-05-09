package com.guilsch.multivoc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import life.sabujak.roundedbutton.RoundedButton;

public class NewCardActivity extends AppCompatActivity {

    private RoundedButton saveCard;

    private EditText item1Text;
    private EditText item2Text;
    private EditText packText;

    private Card newCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);

        saveCard = findViewById(R.id.save_new_card);

        item1Text = findViewById(R.id.item1_text);
        item2Text = findViewById(R.id.item2_text);
        packText = findViewById(R.id.pack_text);

        saveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String item1 = item1Text.getText().toString();
                String item2 = item2Text.getText().toString();

                if (item1.isEmpty() || item2.isEmpty()) {
                    utils.showToast(NewCardActivity.this, getString(R.string.toast_msg_invalid_new_card));
                }
                else {
                    newCard = new Card(item1, item2, Param.TO_LEARN, packText.getText().toString(), utils.giveCurrentDate(), utils.giveCurrentDate(), 0, 0, 0, utils.getNewUUID(), -1);
                    Param.GLOBAL_DECK.add(newCard);
                    newCard.addToDatabaseOnSeparateThread();
                    newCard.info();

                    Intent saveCardActivity = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(saveCardActivity);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(menuActivity);
        finish();
    }
}