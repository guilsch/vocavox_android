package com.guilsch.multivoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class ExploreActivity extends AppCompatActivity {

    ListView simpleList;

    Deck deck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        deck = new Deck();
        deck.init();

        deck.showCards();

        simpleList = (ListView) findViewById(R.id.deckListView);
        DeckAdapter adapter = new DeckAdapter(getApplicationContext(), deck);
        simpleList.setAdapter(adapter);


    }

    @Override
    public void onBackPressed() {
        Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(menuActivity);
        finish();
    }
}