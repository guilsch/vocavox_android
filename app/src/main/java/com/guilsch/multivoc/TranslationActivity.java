package com.guilsch.multivoc;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class TranslationActivity extends AppCompatActivity {

    private Button nextNewCardButton;
    private Button saveCardButton;

    private EditText item1Text;
    private EditText item2Text;
    private EditText packText;

    private TranslationAPI translator;
    private String translationResult;
    private Boolean userToTarget;

    private Card newCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);

        nextNewCardButton = findViewById(R.id.next_new_card);
        saveCardButton = findViewById(R.id.save_new_card);

        item1Text = findViewById(R.id.item1_text);
        item2Text = findViewById(R.id.item2_text);
        packText = findViewById(R.id.pack_text);

        saveCardButton.setOnClickListener(view -> saveCardButtonClick());

        findViewById(R.id.back_arrow).setOnClickListener(view -> onBackPressed());

        item1Text.setOnTouchListener((view, motionEvent) -> onTouchItem1Side());
        item2Text.setOnTouchListener((view, motionEvent) -> onTouchItem2Side());

//        item1Text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(!hasFocus && item1Text.getText().equals(""))
//                {
//                    item1Text.setText("Write word in " + Param.USER_LANGUAGE);
//                    item1Text.setTypeface(null, Typeface.ITALIC);
//                    item1Text.setTextColor(getResources().getColor(R.color.font_hint));
//                }
//                else{
//                    item1Text.setTypeface(null, Typeface.NORMAL);
//                    item1Text.setTextColor(getResources().getColor(R.color.font_standard));
//                }
//            }
//        });

        item1Text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if (userToTarget && s.equals("")) {
                    item2Text.setText("");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (userToTarget) {
                    item2Text.setText(translate(item1Text.getText().toString()));
                }
            }

        });

        item2Text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if (!userToTarget && s.equals("")) {
                    item1Text.setText("");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!userToTarget) {
                    item1Text.setText(translate(item2Text.getText().toString()));
                }
            }
        });

        initSides();
    }

    private Boolean initSides() {
        userToTarget = Boolean.TRUE;

        item1Text.setBackgroundColor(getResources().getColor(R.color.navy1));
        item1Text.setTextColor(getResources().getColor(R.color.white));

        item2Text.setBackgroundColor(getResources().getColor(R.color.white));
        item2Text.setTextColor(getResources().getColor(R.color.navy1));

        System.out.println(item1Text.getCurrentTextColor());

        return Boolean.FALSE;
    }

    private Boolean onTouchItem1Side() {
        userToTarget = Boolean.TRUE;

        item1Text.setBackgroundColor(getResources().getColor(R.color.navy1));
        item1Text.setTextColor(getResources().getColor(R.color.white));

        item2Text.setBackgroundColor(getResources().getColor(R.color.white));
        item2Text.setTextColor(getResources().getColor(R.color.navy1));

        return Boolean.FALSE;
    }

    private Boolean onTouchItem2Side() {
        userToTarget = Boolean.FALSE;

        item1Text.setBackgroundColor(getResources().getColor(R.color.white));
        item1Text.setTextColor(getResources().getColor(R.color.navy1));

        item2Text.setBackgroundColor(getResources().getColor(R.color.navy1));
        item2Text.setTextColor(getResources().getColor(R.color.white));

        return userToTarget;
    }

    public String translate(String source) {
        translator = new TranslationAPI();
        translator.setOnTranslationCompleteListener(new TranslationAPI.OnTranslationCompleteListener() {
            @Override
            public void onStartTranslation() {}

            @Override
            public void onCompleted(String text) {
                translationResult = text;
            }

            @Override
            public void onError(Exception e) {}
        });

        if (userToTarget) {
            translator.execute(source, Param.USER_LANGUAGE_ISO, Param.TARGET_LANGUAGE_ISO);
        }
        else {
            translator.execute(source, Param.TARGET_LANGUAGE_ISO, Param.USER_LANGUAGE_ISO);
        }

        return translationResult;

    }

    public void saveCardButtonClick() {
        String item1 = item1Text.getText().toString();
        String item2 = item2Text.getText().toString();

        if (item1.isEmpty() || item2.isEmpty()) {
            new AlertDialog.Builder(TranslationActivity.this)
                    .setTitle("Unvalid card")
                    .setMessage("At least words in both languages have to be set")
                    .setNegativeButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }

        newCard = new Card(item1, item2, Param.TO_LEARN, packText.getText().toString(), utils.giveCurrentDate(), 0, 0, 0, utils.getNewUUID());
        newCard.addToDatabase();
        newCard.info();

        Intent saveCardActivity = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(saveCardActivity);
        finish();

        return;
    }

    @Override
    public void onBackPressed() {
        Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(menuActivity);
        finish();
    }
}