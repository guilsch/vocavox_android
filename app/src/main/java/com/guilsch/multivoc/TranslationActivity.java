package com.guilsch.multivoc;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TranslationActivity extends AppCompatActivity {

    private Button nextNewCardButton;
    private Button saveCardButton;
    private Button translateButton;

    private EditText item1Text;
    private TextView item2Text;
    private EditText packText;

    private Card newCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);

        nextNewCardButton = (Button) findViewById(R.id.next_new_card);
        saveCardButton = (Button) findViewById(R.id.save_new_card);
        translateButton = (Button) findViewById(R.id.translate_button);

        item1Text = (EditText) findViewById(R.id.item1_text);
        item2Text = (TextView) findViewById(R.id.item2_text);
        packText = (EditText) findViewById(R.id.pack_text);

        saveCardButton.setOnClickListener(view -> saveCardButtonClick());
        translateButton.setOnClickListener(view -> translateButtonClick());

        findViewById(R.id.back_arrow).setOnClickListener(view -> onBackPressed());

    }

    public void translateButtonClick() {

        TranslationAPI translate = new TranslationAPI();
        translate.setOnTranslationCompleteListener(new TranslationAPI.OnTranslationCompleteListener() {
            @Override
            public void onStartTranslation() {
                // here you can perform initial work before translated the text like displaying progress bar
            }

            @Override
            public void onCompleted(String text) {
                // "text" variable will give you the translated text
                item2Text.setText(text);
            }

            @Override
            public void onError(Exception e) {

            }
        });

        translate.execute(item1Text.getText().toString(), "fr", "en");

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

        newCard = new Card(item1, item2, Param.INACTIVE, packText.getText().toString(), utils.giveCurrentDate(), 0, 0, 0, utils.getNewUUID());
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