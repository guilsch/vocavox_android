package com.guilsch.multivoc;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutionException;

import life.sabujak.roundedbutton.RoundedButton;

public class ActivityTranslation extends AppCompatActivity {

    private RoundedButton OKButton;
    private RoundedButton cancelButton;
    private RoundedButton saveCardButton;

    private ImageView targetLanguageFlag;
    private ImageView userLanguageFlag;

    private EditText item1Text;
    private EditText item2Text;
    private EditText item1TextCheckCardLayout;
    private EditText item2TextCheckCardLayout;
    private EditText packText;
    private TextView tradCheckMsg;

    private TranslationAPI translator;
    private String translationResult;
    private Boolean userToTarget;

    private Card newCard;

    private Integer currentLayoutNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMainTranslationLayout();
    }

    private void initMainTranslationLayout() {
        setContentView(R.layout.activity_translation);
        currentLayoutNum = 1;

        OKButton = findViewById(R.id.ok_button);

        item1Text = findViewById(R.id.item1_text);
        item2Text = findViewById(R.id.item2_text);

        targetLanguageFlag = findViewById(R.id.targetLanguageFlag);
        userLanguageFlag = findViewById(R.id.userLanguageFlag);

        targetLanguageFlag.setImageDrawable(Param.FLAG_ICON_TARGET);
        userLanguageFlag.setImageDrawable(Param.FLAG_ICON_USER);

        OKButton.setOnClickListener(view -> {

            if (item1Text.getText().toString().isEmpty() && item2Text.getText().toString().isEmpty()) {
                Utils.showToast(ActivityTranslation.this, getString(R.string.toast_msg_empty_translation));
            }
            else {
                try {
                    setCheckCardLayout();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        item1Text.setOnTouchListener((view, motionEvent) -> onTouchItem1Side());
        item2Text.setOnTouchListener((view, motionEvent) -> onTouchItem2Side());

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

    private void setCheckCardLayout() throws ExecutionException, InterruptedException {
        setContentView(R.layout.activity_translation_check);
        currentLayoutNum = 2;

        item1TextCheckCardLayout = findViewById(R.id.userLanguageText);
        item2TextCheckCardLayout = findViewById(R.id.targetLanguageText);
        packText = findViewById(R.id.pack_text);
        targetLanguageFlag = findViewById(R.id.targetLanguageFlag);
        userLanguageFlag = findViewById(R.id.userLanguageFlag);
        tradCheckMsg = findViewById(R.id.trad_check_msg);

        targetLanguageFlag.setImageDrawable(Param.FLAG_ICON_TARGET);
        userLanguageFlag.setImageDrawable(Param.FLAG_ICON_USER);

        saveCardButton = findViewById(R.id.save_new_card);
        saveCardButton.setOnClickListener(view -> saveCardButtonClick());

        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(view -> initMainTranslationLayout());

        tradCheckMsg.setText(R.string.trans_check_header);

        String item1 = item1Text.getText().toString();
        String item2 = item2Text.getText().toString();

        item1Text.setEnabled(Boolean.FALSE);
        item2Text.setEnabled(Boolean.FALSE);

        String mainTranslation = translateWaitForIt(item1, Boolean.TRUE);

        System.out.println("item2 : " + item2);
        System.out.println("translation : " + mainTranslation);

        String item2WithoutWhitespace = item2.replaceAll("\\s", "");
        String mainTranslationWithoutWhitespace = mainTranslation.replaceAll("\\s", "");

        if (!item2WithoutWhitespace.equalsIgnoreCase(mainTranslationWithoutWhitespace)) {
            item2 = mainTranslation + ", " + item2;
        }

        item1TextCheckCardLayout.setText(item1);
        item2TextCheckCardLayout.setText(item2);

    }

    private Boolean initSides() {
        userToTarget = Boolean.TRUE;

        item1Text.setBackgroundColor(getResources().getColor(R.color.back_ground2));
        item1Text.setTextColor(getResources().getColor(R.color.navy1));

        item2Text.setBackgroundColor(getResources().getColor(R.color.white));
        item2Text.setTextColor(getResources().getColor(R.color.navy1));

        System.out.println(item1Text.getCurrentTextColor());

        return Boolean.FALSE;
    }

    private Boolean onTouchItem1Side() {
        userToTarget = Boolean.TRUE;

        item1Text.setBackgroundColor(getResources().getColor(R.color.back_ground2));
        item1Text.setTextColor(getResources().getColor(R.color.navy1));

        item2Text.setBackgroundColor(getResources().getColor(R.color.white));
        item2Text.setTextColor(getResources().getColor(R.color.navy1));

        return Boolean.FALSE;
    }

    private Boolean onTouchItem2Side() {
        userToTarget = Boolean.FALSE;

        item1Text.setBackgroundColor(getResources().getColor(R.color.white));
        item1Text.setTextColor(getResources().getColor(R.color.navy1));

        item2Text.setBackgroundColor(getResources().getColor(R.color.back_ground2));
        item2Text.setTextColor(getResources().getColor(R.color.navy1));

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

        System.out.println("userToTarget : ");
        System.out.println(userToTarget);

        if (userToTarget) {
            translator.execute(source, Param.USER_LANGUAGE_ISO, Param.TARGET_LANGUAGE_ISO);
        }
        else {
            translator.execute(source, Param.TARGET_LANGUAGE_ISO, Param.USER_LANGUAGE_ISO);
        }

        return translationResult;

    }

    public String translateWaitForIt(String source, Boolean toTarget) throws ExecutionException, InterruptedException {
        TranslationAPI translatorOneShot = new TranslationAPI();
        translatorOneShot.setOnTranslationCompleteListener(new TranslationAPI.OnTranslationCompleteListener() {
            @Override
            public void onStartTranslation() {}

            @Override
            public void onCompleted(String text) {
//                translationResultOneShot = text;
            }

            @Override
            public void onError(Exception e) {}
        }
        );

        String res;

        if (toTarget) {
            res = translatorOneShot.execute(source, Param.USER_LANGUAGE_ISO, Param.TARGET_LANGUAGE_ISO).get();
        }
        else {
            res = translatorOneShot.execute(source, Param.TARGET_LANGUAGE_ISO, Param.USER_LANGUAGE_ISO).get();
        }

        return res;

    }

    public void saveCardButtonClick() {
        String item1 = item1TextCheckCardLayout.getText().toString();
        String item2 = item2TextCheckCardLayout.getText().toString();

        if (item1.isEmpty() || item2.isEmpty()) {
            new AlertDialog.Builder(ActivityTranslation.this)
                    .setTitle("Unvalid card")
                    .setMessage("At least words in both languages have to be set")
                    .setNegativeButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }

        newCard = new Card(item1, item2, Param.TO_LEARN, packText.getText().toString(), Utils.giveCurrentDate(), Utils.giveCurrentDate(), 0, 0, 0, Utils.getNewUUID(), -1);
        Utils.manageCardCreation(newCard);

        Intent saveCardActivity = new Intent(getApplicationContext(), ActivityMenu.class);
        startActivity(saveCardActivity);
        finish();

        return;
    }

    @Override
    public void onBackPressed() {

        if (currentLayoutNum == 1) {
            Intent menuActivityIntent = new Intent(getApplicationContext(), ActivityMenu.class);
            menuActivityIntent.putExtra("FRAG_INDEX", 2);
            startActivity(menuActivityIntent);
            finish();
        }

        else if (currentLayoutNum == 2){
            initMainTranslationLayout();
        }
    }
}