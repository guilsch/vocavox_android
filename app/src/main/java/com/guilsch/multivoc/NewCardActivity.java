package com.guilsch.multivoc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewCardActivity extends AppCompatActivity {

    private Button nextNewCard;
    private Button saveCard;

    private EditText item1Text;
    private EditText item2Text;
    private EditText packText;

    private Card newCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);

        nextNewCard = (Button) findViewById(R.id.next_new_card);
        saveCard = (Button) findViewById(R.id.save_new_card);

        item1Text = (EditText) findViewById(R.id.item1_text);
        item2Text = (EditText) findViewById(R.id.item2_text);
        packText = (EditText) findViewById(R.id.pack_text);

        findViewById(R.id.back_arrow).setOnClickListener(view -> onBackPressed());

        saveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String item1 = item1Text.getText().toString();
                String item2 = item2Text.getText().toString();

                if (item1.isEmpty() || item2.isEmpty()) {
                    new AlertDialog.Builder(NewCardActivity.this)
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