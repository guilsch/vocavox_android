package com.guilsch.multivoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.util.Date;

public class ActivityEditCard extends AppCompatActivity {

    private TextView saveCardButton;
//    private TextView deleteCardButton;
    private EditText item1Text;
    private EditText item2Text;
    private EditText packText;
    private TextView nextDateText;
    private TextView nextDateSentence;
    private TextView creationDateText;
    private Card card;
    private ConstraintLayout backLayout;
    private RadioGroup stateRadioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;

    private ImageView targetLanguageFlag;
    private ImageView userLanguageFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        backLayout = findViewById(R.id.back_layout);
        backLayout.setOnClickListener(v -> onBackPressed());

        String uuid = (String) getIntent().getSerializableExtra("UUID");
        card = Param.GLOBAL_DECK.getCardFromUuid(uuid);

        saveCardButton = findViewById(R.id.save_card_button);
//        deleteCardButton = findViewById(R.id.delete_card_button);
        item1Text = findViewById(R.id.item1_text);
        item2Text = findViewById(R.id.item2_text);
        packText = findViewById(R.id.pack_text);
        nextDateText = findViewById(R.id.next_date_text);
        nextDateSentence = findViewById(R.id.next_date_sentence);
        creationDateText = findViewById(R.id.creation_date_text);
        targetLanguageFlag = findViewById(R.id.targetLanguageFlag);
        userLanguageFlag = findViewById(R.id.userLanguageFlag);

        targetLanguageFlag.setImageDrawable(Param.FLAG_ICON_TARGET);
        userLanguageFlag.setImageDrawable(Param.FLAG_ICON_USER);
        item1Text.setText(card.getItem1());
        item2Text.setText(card.getItem2());
        packText.setText(card.getPack());
        creationDateText.setText(Utils.universalToLocalDate(card.getCreationDate().toString(), Param.USER_LANGUAGE_ISO));
        manageDateOrNowMessage(card.getNextPracticeDate());


        Utils.setTextViewTextColorChangeOnTouch(saveCardButton, R.color.std_text_button_on_click, R.color.std_text_button);
//        Utils.setTextViewTextColorChangeOnTouch(deleteCardButton, R.color.delete_text_button_on_click, R.color.delete_text_button);

        stateRadioGroup = findViewById(R.id.state_radio_group);
        radioButton1 = findViewById(R.id.radio_button_1);
        radioButton2 = findViewById(R.id.radio_button_2);
        radioButton3 = findViewById(R.id.radio_button_3);

        updateRadioButtonsFromState(card.getState());

        saveCardButton.setOnClickListener(v -> onSaveCardPressed());
        stateRadioGroup.setOnClickListener(v -> onChangeStateClick());
        radioButton1.setOnClickListener(v -> onChangeStateClick());
        radioButton2.setOnClickListener(v -> onChangeStateClick());
        radioButton3.setOnClickListener(v -> onChangeStateClick());
    }

    private void onChangeStateClick() {
        int newState = Utils.nextStateForButton(card.getState());
        updateRadioButtonsFromState(newState);
        card.setState(newState);
    }

    public void updateRadioButtonsFromState(int state) {

        switch (state) {

            case Param.INACTIVE:
                radioButton1.setChecked(true);
                radioButton2.setChecked(false);
                radioButton3.setChecked(false);
                break;

            case Param.ACTIVE:
                radioButton1.setChecked(false);
                radioButton2.setChecked(true);
                radioButton3.setChecked(false);
                break;

            case Param.IN_PAUSE:
                radioButton1.setChecked(false);
                radioButton2.setChecked(false);
                radioButton3.setChecked(true);
                break;

            default:
                radioButton1.setChecked(false);
                radioButton2.setChecked(false);
                radioButton3.setChecked(false);

        }
    }

    private void onSaveCardPressed() {

        card.setItem1(item1Text.getText().toString());
        card.setItem2(item2Text.getText().toString());
        card.setPack(packText.getText().toString());
        Utils.updateInDatabaseOnSeparateThreadOneShot(card);


        finish();
    }

    public void manageDateOrNowMessage(Date date) {
        if (date.before(Utils.giveCurrentDate())) {
            nextDateSentence.setText(getResources().getString(R.string.edit_card_date_sentence_now));
            nextDateText.setText(getResources().getString(R.string.edit_card_date_word_now));
        }
        else {
            nextDateSentence.setText(getResources().getString(R.string.edit_card_date_sentence_on_the));
            nextDateText.setText(Utils.universalToLocalDate(date.toString(), Param.USER_LANGUAGE_ISO));
        }
    }

    @Override
    public void onBackPressed() {
//        Intent menuActivity = new Intent(getApplicationContext(), ActivityMenu.class);
//        startActivity(menuActivity);
        finish();
    }
}