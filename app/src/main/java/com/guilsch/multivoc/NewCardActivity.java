package com.guilsch.multivoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Date;

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

        saveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newCard = new Card(item1Text.getText().toString(), item2Text.getText().toString(), Param.INACTIVE, packText.getText().toString(), utils.giveDate(), 0, 0, 0);
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