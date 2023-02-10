package com.guilsch.multivoc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditCardActivity extends AppCompatActivity {

    private Button saveCardButton;
    private Button deleteCardButton;
    private Button setStateButton;
    private EditText item1Text;
    private EditText item2Text;
    private EditText packText;
    private TextView nextDateText;
    private Card card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        String uuid = (String) getIntent().getSerializableExtra("UUID");
        card = Param.GLOBAL_DECK.getCardFromUuid(uuid);

        saveCardButton = findViewById(R.id.save_card_button);
        deleteCardButton = findViewById(R.id.delete_card_button);
        item1Text = findViewById(R.id.item1_text);
        item2Text = findViewById(R.id.item2_text);
        packText = findViewById(R.id.pack_text);
        nextDateText = findViewById(R.id.next_date_text);
        setStateButton = findViewById(R.id.set_state_button);

        item1Text.setText(card.getItem1());
        item2Text.setText(card.getItem2());
        packText.setText(card.getPack());
        nextDateText.setText(card.getNextPracticeDate().toString());
        setStateButton.setText(utils.getStringStateFromInt(card.getState()));

        setStateButton.setOnClickListener(v -> onStateButtonPressed());
        deleteCardButton.setOnClickListener(v -> onDeleteCardPressed());
        saveCardButton.setOnClickListener(v -> onSaveCardPressed());

    }

    private void onSaveCardPressed() {

        card.setItem1(item1Text.getText().toString());
        card.setItem2(item2Text.getText().toString());
        card.setPack(packText.getText().toString());
        card.setState(utils.getIntStateFromString((String) setStateButton.getText()));

        card.updateInDatabase();

//        ExploreActivity.deck.init();
        finish();
    }

    private void onDeleteCardPressed() {
        Param.GLOBAL_DECK.deleteCard(card.getUuid());

        finish();
    }

    private void onStateButtonPressed() {
        setStateButton.setText(utils.getStringStateFromInt(utils.nextStateForButton(card.getState())));
    }

    @Override
    public void onBackPressed() {
//        Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
//        startActivity(menuActivity);
        finish();
    }
}