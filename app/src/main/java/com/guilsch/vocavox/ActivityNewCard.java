package com.guilsch.vocavox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import life.sabujak.roundedbutton.RoundedButton;

public class ActivityNewCard extends AppCompatActivity {

    private RoundedButton saveCard;

    private EditText item1Text;
    private EditText item2Text;
    private EditText packText;

    private ImageView targetLanguageFlag;
    private ImageView userLanguageFlag;

    private Card newCard;

    private ConstraintLayout backLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);

        saveCard = findViewById(R.id.save_card_button);
        item1Text = findViewById(R.id.item1_text);
        item2Text = findViewById(R.id.item2_text);
        packText = findViewById(R.id.pack_text);
        targetLanguageFlag = findViewById(R.id.targetLanguageFlag);
        userLanguageFlag = findViewById(R.id.userLanguageFlag);

        targetLanguageFlag.setImageDrawable(Param.FLAG_ICON_TARGET);
        userLanguageFlag.setImageDrawable(Param.FLAG_ICON_USER);

        backLayout = findViewById(R.id.back_layout);
        backLayout.setOnClickListener(v -> onBackPressed());

        saveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String item1 = item1Text.getText().toString();
                String item2 = item2Text.getText().toString();

                if (item1.isEmpty() || item2.isEmpty()) {
                    Utils.showToast(ActivityNewCard.this, getString(R.string.toast_msg_invalid_new_card));
                }
                else {
                    newCard = new Card(item1, item2, Param.INACTIVE, packText.getText().toString(), Utils.giveCurrentDate(), Utils.giveCurrentDate(), 0, 0, 0, Utils.getNewUUID(), -1);
                    Utils.manageCardCreation(newCard);

                    cleanAfterSaveCard();
                }
            }
        });
    }

    private void cleanAfterSaveCard() {
        item1Text.setText("");
        item2Text.setText("");
        packText.setText("");

        Utils.showToast(ActivityNewCard.this, getString(R.string.toast_msg_new_card_saved));
    }

    @Override
    public void onBackPressed() {
        Intent menuActivityIntent = new Intent(getApplicationContext(), ActivityMenu.class);
        menuActivityIntent.putExtra("FRAG_INDEX", 2);
        startActivity(menuActivityIntent);
        finish();
    }
}